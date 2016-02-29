package com.espublico.expedientes.api.examples;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

	public final static String METHOD_NAME = "GET";

	public HttpGetWithEntity(URI url) {
		setURI(url);
	}

	public HttpGetWithEntity(String url) throws URISyntaxException {
		setURI(new URI(url));
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}
}
