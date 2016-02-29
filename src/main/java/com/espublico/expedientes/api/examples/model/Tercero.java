package com.espublico.expedientes.api.examples.model;


import java.util.HashMap;


public class Tercero extends MyJSONParse{

	
	private HashMap<String, String> links;
	
	/**
	 * Nombre
	 */
	private String name;
	
	/**
	 * NIF
	 */
	private String nif;
	
	/**
	 * Tipo de persona: PHISIC, JURIDICAL
	 */
	private String personType;
	
	/**
	 * Relación
	 */
	private String relation;
	
	/**
	 * Medio de notificación: PAPER, TELEMATIC
	 */
	private String notificationChannel;
	
	/**
	 * Dirección
	 */
	private String address;
	
	/**
	 * País
	 */
	private String country;
	
	/**
	 * Provincia: ALAVA, ALBACETE, ALICANTE, ALMERIA, ASTURIAS, AVILA, BADAJOZ, BALEARES, BARCELONA, BURGOS, CACERES, CADIZ, CANTABRIA, CASTELLON, CEUTA,
	 * CIUDAD_REAL, CORDOBA, CORUNA, , GIPUZKOA, GIRONA, GRANADA, GUADALAJARA, HUELVA, HUESCA, JAEN, LAS_PALMAS, LEON, LLEIDA, LUGO, MADRID, MALAGA, MELILLA,
	 * MURCIA, NAVARRA, OURENSE, PALENCIA, PONTEVEDRA, RIOJA, SALAMANCA, SEGOVIA, SEVILLA, SORIA, TARRAGONA, TENERIFE, TERUEL, TOLEDO, VALENCIA, VALLADOLID,
	 * VIZCAYA, ZAMORA, ZARAGOZA
	 */
	private String province;

	/**
	 * Código Postal
	 */
	private String zipCode;
	
	/**
	 * Zona
	 */
	private String zone;
	
	/**
	 * 
	 */
	private String departament;
	
	/**
	 * Email
	 */
	private String email;
	
	/**
	 * Teléfono
	 */
	private String phone;
	
	/**
	 * Fax
	 */
	private String fax;
	
	/** 
	 * Móvil
	 */
	private String mobile;
	
	/**
	 * Notas
	 */
	private String notes;
	
	

	public String getAddress() {
		return address;
	}

	public String getCountry() {
		return country;
	}

	public String getDepartament() {
		return departament;
	}

	public String getEmail() {
		return email;
	}

	public String getFax() {
		return fax;
	}

	public String getMobile() {
		return mobile;
	}

	public String getName() {
		return name;
	}

	public String getNif() {
		return nif;
	}

	public String getNotes() {
		return notes;
	}

	public String getNotificationChannel() {
		return notificationChannel;
	}

	public String getPersonType() {
		return personType;
	}

	public String getPhone() {
		return phone;
	}

	public String getProvince() {
		return province;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getZone() {
		return zone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setNotificationChannel(String notificationChannel) {
		this.notificationChannel = notificationChannel;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	
	public void setLinks(HashMap<String, String> links) {
		this.links = links;
	}
	public HashMap<String, String> getLinks() {
		return links;
	}
	
	/**
	 * Devuelve el JSON con la estructura de tercero (application/vnd.gestiona.annotation.thirdparties+json)
	 * 
	 * @return String: String JSON en utf8
	 */
	public String toJSON() {
		StringBuilder json = initJSONObject();

		addKeyValueString(json, "address", address);
		addKeyValueString(json, "country", country);
		addKeyValueString(json, "departament", departament);
		addKeyValueString(json, "email", email);
		addKeyValueString(json, "fax", fax);
		addKeyValueString(json, "mobile", mobile);
		addKeyValueString(json, "name", name);
		addKeyValueString(json, "nif", nif);
		addKeyValueString(json, "notification_channel", notificationChannel);
		addKeyValueString(json, "type", personType);
		addKeyValueString(json, "phone", phone);
		addKeyValueString(json, "province", province);
		addKeyValueString(json, "relation", relation);
		addKeyValueString(json, "zip", zipCode);
		addKeyValueString(json, "zone", zone);
		
		endJSONObject(json);
		
		return json.toString(); 
	}
	
}
