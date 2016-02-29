package com.espublico.expedientes.api.examples;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.espublico.expedientes.api.examples.model.Anotacion;
import com.espublico.expedientes.api.examples.model.OficinaRegistro;
import com.espublico.expedientes.api.examples.model.RestError;
import com.espublico.expedientes.api.examples.model.RestRegistryOfficeFilter;

public class AddDocAnnotationsByApi {

	private static CloseableHttpClient httpClient = null;

	/**
	 * URL de la API
	 */
	private static String serverURL = "https://02.g3stiona.com/rest/";
	
	/**
	 * Addon que se va a utilizar (rellenar con el necesario)
	 */
	private static String addon = "AQUI_SU_ADDON_TOKEN";

	/**
	 * Bookmarks de los recursos de la API
	 */
	private static HashMap<String, String> recursos = new HashMap<String, String>();

	/**
	 * Token para obtener la autorización (dejar a null para que cree uno nuevo)
	 */
	private static String token = null;

	/**
	 * Access-token autorizado (dejar a null para que cree uno nuevo, y poner el 'tokenAutorizado' a false)
	 */
	private static String accessToken = null;
	private static boolean tokenAutorizado = false;

	private static JSONParser parser = new JSONParser();

	public static void main(String[] args) throws Exception {

		try {
			httpClient = newHttpClient();

			// Obtenemos los Bookmarks de los recursos de la API, para a partir
			// de ellos empezar a 'navegar' haciendo las peticiones.
			getRecursos();

			// Creamos un token de acceso, al crearlo estará en estado
			// 'pendiente de autorizar' a la espera de que nos logeemos con un
			// usuario y lo autoricemos
			if (token == null)
				token = createToken();

			// Comprobamos el estado del token
			while (tokenAutorizado == false) {
				tokenAutorizado = comprobarToken(token);
			}

			log("============================================ LOGIN CORRECTO ===========================================");

			// Obtenemos la oficina de registro en la que queremos crear la
			// anotación (sustituir "RE" por una existente)
			OficinaRegistro or = getOficinaRegistro("RC");

			for (int i = 0; i < 3; i++) {
				// Rellenamos los datos de la anotación a crear. Obligatorios: Forma
				// de Envío, Tipo de Documento y Resumen
				Anotacion anotacion = new Anotacion();
				anotacion.setIncomeType("PRESENTIAL");
				anotacion.setShortDescription("API prueba rendimiento");
				anotacion.setClassification("REQUERIMENT");
				anotacion.setLongDescription("Aquí van las observaciones de la anotación");
				anotacion.setOriginCode("C0D-O4161N");
				anotacion = crearAnotacion(or.getLinks().get("input"), anotacion);
				log("LLEVAMOS YA " + i + " PASADAS");
			}

		} finally {
			if (httpClient != null)
				httpClient.close();
		}

	}

	/**
	 * Rellena el hasmap 'recursos' con todos los bookmarks de la API
	 */
	private static void getRecursos() {
		// Accedemos al recurso raiz de la API
		HttpGet getRequest = new HttpGet(serverURL);

		// Mandamos la cabecera del Addon que vamos a utilizar en la API
		getRequest.addHeader("X-Gestiona-Addon-Token", addon);

		try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
			// Si no devuelve un código 200 de estado correcto
			if (response.getStatusLine().getStatusCode() != 200) {
				// TODO controlar los status code error
				log("ERROR");
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			Object obj;

			try (InputStreamReader reader = new InputStreamReader(response.getEntity().getContent())) {
				obj = parser.parse(reader);

				// Lo convertimos en un JSON Object
				JSONObject jsonObject = (JSONObject) obj;

				// Cogemos los links del JSON y lo convertimos a un array
				JSONArray msg = (JSONArray) jsonObject.get("links");
				Iterator<JSONObject> iterator = msg.iterator();

				// Cada elemento del array lo metemos en el HashMap de recursos
				while (iterator.hasNext()) {
					JSONObject o = iterator.next();

					recursos.put((String) o.get("rel"), (String) o.get("href"));
				}

			} catch (ParseException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			// TODO controlar las excepciones
			log("ERROR");
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Crea el token con el que tendremos que logearnos para obtener la autorizacion
	 * 
	 * @return String token
	 */
	private static String createToken() {
		// Accedemos al recurso del bookmark 'vnd.gestiona.addon.authorizations'
		HttpPost postRequest = new HttpPost(recursos.get("vnd.gestiona.addon.authorizations"));

		// Mandamos la cabecera del Addon que vamos a utilizar en la API
		postRequest.addHeader("X-Gestiona-Addon-Token", addon);

		try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
			if (response.getStatusLine().getStatusCode() == 201) {
				// accessToken creado correctamente
				String location = response.getHeaders("Location")[0].getValue();
				token = location.substring(location.lastIndexOf('/') + 1);
				log("::TOKEN ==> " + token);

				// Devolvemos el token
				return token;

			} else if (response.getStatusLine().getStatusCode() == 403) {
				throw new RuntimeException("Error al crear el accessToken, no se encuentra el addon " + addon);
			} else {
				throw new RuntimeException("Error al crear el accessToken: " + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			// TODO controlar las excepciones
			log("ERROR");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Comprueba que le token que se le pasa como parámetro esté en estado autorizado, en caso de estar pendiente de autorización nos devuelve la URL en la que
	 * nos debemos loggear con un usuario y contraseña para autorizar ese token
	 * 
	 * @param String
	 *            token
	 * 
	 * @return boolean true cuando está autorizado y false en caso contrario
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ParseException
	 */
	private static boolean comprobarToken(final String token) throws IOException {
		HttpGet getRequest = new HttpGet(recursos.get("vnd.gestiona.addon.authorizations") + "/" + token);
		getRequest.addHeader("X-Gestiona-Addon-Token", addon);

		try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
			if (response.getStatusLine().getStatusCode() == 200) {
				// Token autorizado
				// Leemos el contenido del JSON devuelto para guardarnos el valor
				// del access-token, que es lo que se le pasará como cabecera para
				// las peticiones de
				// la API que tienen control de acceso
				JSONParser parser = new JSONParser();

				Object obj;

				try (InputStreamReader reader = new InputStreamReader(response.getEntity().getContent())) {
					obj = parser.parse(reader);
					JSONObject jsonObject = (JSONObject) obj;

					accessToken = (String) jsonObject.get("access_token");
					System.err.println("token: " + token + " accessToken: " + accessToken);

				} catch (ParseException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				return true;

			} else if (response.getStatusLine().getStatusCode() == 401) {
				String urlLogin = response.getHeaders("Location")[0].getValue();
				log(String.format(
						"Entre en esta URL y logeese con su usuario y contraseña para validar el token: \n%s\n [Pulse intro cuando ya lo haya actualizado]",
						urlLogin));
				System.in.read(); // para esperar a que se logee y pulse intro
				return false;
			} else {
				Object obj;

				try (InputStreamReader reader = new InputStreamReader(response.getEntity().getContent())) {
					obj = parser.parse(reader);
					JSONObject o = (JSONObject) obj;

					RestError restError = convertRestErrorFromJSON(o);

					throw new RuntimeException("Error al comprobar autorización " + restError.getDescription());
				} catch (ParseException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		} catch (Exception e) {
			// TODO controlar las excepciones
			log("ERROR");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dado el link de una anotación existente, hará la petición GET y nos devolverá los datos de dicha anotación mapeados en el objeto Anotacion que nos hemos
	 * creado
	 * 
	 * @param String
	 *            uri: recurso sobre el que se hará el GET para obtener los datos de la anotación
	 * 
	 * @return Anotacion
	 */
	private static Anotacion getAnotacion(final String uri) {
		HttpGet getRequest = new HttpGet(uri);

		// Mandamos la cabecera con el access-token autorizado
		getRequest.addHeader("X-Gestiona-Access-Token", accessToken);

		try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
			if (response.getStatusLine().getStatusCode() == 200) {
				// Leemos el JSON y lo mapeamos al objeto Anotacion que nos hemos
				// creado.
				Anotacion anotacion = new Anotacion();

				JSONParser parser = new JSONParser();
				Object obj;

				try (InputStreamReader reader = new InputStreamReader(response.getEntity().getContent())) {
					obj = parser.parse(reader);
					JSONObject o = (JSONObject) obj;

					anotacion.setAnnulledDate((String) o.get("annulled_date"));
					anotacion.setAnnulledReason((String) o.get("annulled_reason"));
					anotacion.setClassification((String) o.get("classification"));
					anotacion.setCode((String) o.get("code"));
					anotacion.setDate((String) o.get("date"));
					anotacion.setDeliveryType((String) o.get("delivery_type"));
					anotacion.setIncomeType((String) o.get("income_type"));
					anotacion.setLongDescription((String) o.get("obs"));
					anotacion.setOriginCode((String) o.get("origin_code"));
					anotacion.setOriginDate((String) o.get("origin_date"));
					anotacion.setOriginOrganization((String) o.get("origin_organization"));
					anotacion.setOriginRegistryOffice((String) o.get("origin_registry_office"));
					anotacion.setShortDescription((String) o.get("short_description"));
					anotacion.setState((String) o.get("state"));
					anotacion.setType((String) o.get("type"));
					anotacion.setLinks(getLinksValue(o));
					anotacion.setCategory((String) o.get("category"));

					return anotacion;

				} catch (ParseException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

			} else {
				// TODO se debe mapear el restError (application/vnd.gestiona.error para tratar los errores como corresponda
				throw new RuntimeException("Error al obtener anotacion: " + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			// TODO controlar las excepciones
			log("ERROR");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Hará el POST sobre la uri que le pasamos para crear la anotación con los datos que le pasamos en el objeto Anotacion
	 * 
	 * @param String
	 *            uri: será el link 'input' o 'output' de la oficina de registro en la que vamos a crear la anotación
	 * @param Anotacion
	 *            anotacion
	 * 
	 * @return Anotacion con todos los datos y los links de la anotación
	 */
	private static Anotacion crearAnotacion(final String uri, final Anotacion anotacion) {
		HttpPost postRequest = new HttpPost(uri);

		// Mandamos al cabecera con el access-token autorizado
		postRequest.addHeader("X-Gestiona-Access-Token", accessToken);
		postRequest.addHeader("Content-Type", "application/vnd.gestiona.registry-annotation");

		if (anotacion != null) {
			// Enviamos el JSON de la anotación en los parámetros
			postRequest.setEntity(new StringEntity(anotacion.toJSON(), "UTF-8"));
		}

		try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
			JSONParser parser = new JSONParser();
			Object obj;

			if (response.getStatusLine().getStatusCode() == 201) {
				// anotation creada correctamente
				// Devuelve un status 201 - Created y un Locatioon con el recurso de
				// la anotación que acaba de crearse
				String location = response.getHeaders("Location")[0].getValue();

				// hacemos el GET del Location que nos devuelve para obtener la
				// anotación completa y mapeada en Anotacion
				return getAnotacion(location);

			} else {
				// TODO se debe mapear el restError (application/vnd.gestiona.error para tratar los errores como corresponda
				throw new RuntimeException("Error al crearAnotacion: " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			// TODO controlar las excepciones
			log("ERROR");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Buscar oficina de registro según el código de la oficina que se le pasa como parámetro
	 * 
	 * @param String
	 *            code: código de la oficina a buscar
	 * 
	 * @return OficinaRegistro: datos completos de la oficina
	 * @throws URISyntaxException
	 */
	private static OficinaRegistro getOficinaRegistro(final String code) throws IOException, URISyntaxException {
		RestRegistryOfficeFilter restRegistryOfficeFilter = new RestRegistryOfficeFilter();
		restRegistryOfficeFilter.setCode(code);

		String uri = recursos.get("vnd.gestiona.registry.offices");

		// OPCION 1: Como payload del GET
		// HttpGetWithEntity getRequest = new HttpGetWithEntity(uri);
		//
		// // Mandamos la cabecera con el access.token autorizado
		// getRequest.addHeader("X-Gestiona-Access-Token", accessToken);
		// getRequest.addHeader("Content-Type", "application/vnd.gestiona.filter.registry-offices");
		//
		// getRequest.setEntity(new StringEntity(restRegistryOfficeFilter.toJSON(), "UTF-8"));

		// OPCION 2: Como FilterView en la URL
		String b64 = Base64.encodeBase64URLSafeString(restRegistryOfficeFilter.toJSON().getBytes(Charset.forName("UTF-8")));

		if (uri.contains("?"))
			uri += "&" + "filter-view" + "=" + b64;
		else
			uri += "?" + "filter-view" + "=" + b64;

		HttpGetWithEntity getRequest = new HttpGetWithEntity(uri);
		getRequest.addHeader("X-Gestiona-Access-Token", accessToken);
		getRequest.addHeader("Content-Type", "application/vnd.gestiona.filter.registry-offices");

		try (CloseableHttpResponse response = httpClient.execute(getRequest)) {
			if (response.getStatusLine().getStatusCode() == 200) {
				// Leemos el JSON que nos devuelve para mapear los datos de la
				// oficina de registro en el objeto OficinaRegistro
				OficinaRegistro oficina = new OficinaRegistro();
				JSONParser parser = new JSONParser();
				Object obj;

				try (InputStreamReader reader = new InputStreamReader(response.getEntity().getContent())) {
					obj = parser.parse(reader);
					JSONObject jsonObject = (JSONObject) obj;

					oficina.setCode(code);
					JSONArray msg = (JSONArray) jsonObject.get("content");
					Iterator<JSONObject> iterator = msg.iterator();

					while (iterator.hasNext()) {
						JSONObject o = iterator.next();
						oficina.setNombre((String) o.get("name"));
						oficina.setLinks(getLinksValue(o));
					}

				} catch (ParseException e) {
					throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				return oficina;

			} else {
				// TODO se debe mapear el restError (application/vnd.gestiona.error para tratar los errores como corresponda
				throw new RuntimeException("Error al obtener la oficina de registro: " + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			// TODO controlar las excepciones
			log("ERROR");
			throw new RuntimeException(e);
		}
	}

	private static HashMap<String, String> getLinksValue(JSONObject object) {
		HashMap<String, String> list = new HashMap<String, String>();
		JSONArray msg = (JSONArray) object.get("links");
		Iterator<JSONObject> iterator = msg.iterator();
		while (iterator.hasNext()) {
			JSONObject o = iterator.next();

			list.put((String) o.get("rel"), (String) o.get("href"));
		}
		return list;
	}

	private static void log(String message) {
		System.out.println(message);
	}

	private static CloseableHttpClient newHttpClient() {
		// Establecemos los parámetros de timeout:
		//
		// ConnectionTimeout - Determina el tiempo máximo que espera hasta que la conexión se establece
		// SocketTimeout - Máximo período de inactividad entre dos paquetes de datos consecutivos
		//
		// El importante en nuestro caso es el SocketTimeout. Da igual la librería que se use para conectar, habria que buscar siempre la propiedad de timeout
		// que haga referencia al ReadTimeout (en este caso SocketTimeout hace referencia a lectura y escritura). Recomendamos que el timeout sea de 5 minutos.
		//
		RequestConfig config = RequestConfig.custom().setConnectTimeout(300 * 1000).setSocketTimeout(300 * 1000).build();
		
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(config);

		try {
			SSLContextBuilder sslBuilder = new SSLContextBuilder();
			sslBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslBuilder.build());
			builder = HttpClients.custom().setSSLSocketFactory(sslsf);
			
			return builder.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Mapea el objeto JSON <RestError> para convertirlo en un objeto de tipo RestError
	 * 
	 * @param Element
	 *            e: Objeto JSON(con la estructura de RestError)
	 * 
	 * @return RestError: datos completos del RestError
	 */
	private static RestError convertRestErrorFromJSON(JSONObject o) {
		RestError restError = new RestError();
		restError.setCode((Long) o.get("code"));
		restError.setDescription((String) o.get("description"));
		restError.setDetails((List<String>) o.get("details"));
		restError.setInternalCodeError((String) o.get("internal_code_error"));
		restError.setName((String) o.get("name"));
		restError.setTechnicalDetails((String) o.get("technical_details"));

		return restError;
	}

}
