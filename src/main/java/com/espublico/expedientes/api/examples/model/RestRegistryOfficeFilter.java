package com.espublico.expedientes.api.examples.model;

public class RestRegistryOfficeFilter extends MyJSONParse {

	private static final long serialVersionUID = 1L;

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Devuelve el JSON (vnd.gestiona.filter.registry-offices+json)
	 * 
	 * @return String: String JSON en utf8
	 */
	public String toJSON() {
		StringBuilder json = initJSONObject();

		addKeyValueString(json, "code", code);

		endJSONObject(json);

		return json.toString();
	}

}
