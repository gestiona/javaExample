package com.espublico.expedientes.api.examples.model;


import java.util.HashMap;


public class OficinaRegistro {

	
	private HashMap<String, String> links;
	
	/**
	 * Código
	 */
	private String code;
	
	/**
	 * Nombre
	 */
	private String nombre;

	
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return nombre;
	}
	public void setLinks(HashMap<String, String> links) {
		this.links = links;
	}
	public HashMap<String, String> getLinks() {
		return links;
	}
	
	
}
