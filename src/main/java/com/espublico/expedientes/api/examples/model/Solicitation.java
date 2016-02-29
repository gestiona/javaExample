package com.espublico.expedientes.api.examples.model;


import java.util.HashMap;


public class Solicitation {

	
	private HashMap<String, String> links;
	
	/**
	 * DNI
	 */
	private String dni;
	

	
	public void setLinks(HashMap<String, String> links) {
		this.links = links;
	}
	public HashMap<String, String> getLinks() {
		return links;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getDni() {
		return dni;
	}
	@Override
	public String toString() {
		return "Solicitation [links=" + links + ", dni=" + dni + "]";
	}
	
	
}
