package com.espublico.expedientes.api.examples.model;


import java.util.HashMap;


public class Anotacion extends MyJSONParse{

	
	private HashMap<String, String> links;
	
	private String code;
	/** 
	 * Fecha de creación
	 */
	private String date;
	
	/** 
	 * Estado de la anotación
	 */
	private String state;
	
	/**
	 * Fecha de la original
	 */
	private String originDate;
	
	/**
	 * Código/Número del expediente original
	 */
	private String originCode; 
	
	/**
	 * Organización del expediente original
	 */
	private String originOrganization; 
	
	/**
	 * Oficna del expediente original
	 */
	private String originRegistryOffice; 
	
	/**
	 * Resumen
	 */
	private String shortDescription; 
	
	/**
	 * Observaciones
	 */
	private String longDescription; 
	
	/**
	 * Tipo de documento: OTHER, CERTICATE, TENDER, ANOUNCE, APPLY, INFORM, AGREEMENT, AVAL, INCOME, DEMAND, PUBLICITY, RESOURCE, COMUNICATION, PROVIDENCE,
	 * BILL, REQUERIMENT, OFFICE, ACT, MOTION, PURPOSE, EDICT, LIQUIDATION, NOTIFICATION, ALEGATION, RECEIPT, DICTAMEN, DOCUMENTATION, DECREE
	 */
	private String classification;
	
	/**
	 * Forma de presentación: PRESENTIAL, ORDINARY_POSTALMAIL, CERTIFIED_POSTALMAIL, MESSENGER, EMAIL, BUROFAX, FAX, NOTARIAL, TELEMATIC, OTHER,
	 * ADMINISTRATIVE_MAIL
	 */
	private String incomeType;
	private String deliveryType;
	
	/**
	 * Tipo de anotación (input/output)
	 */
	private String type;
	
	/**
	 * Fecha de anulación
	 */
	private String annulledDate;
	
	/**
	 * Motivo de la anulación
	 */
	private String annulledReason;
	
	/**
	 * Categoria
	 */
	private String category;
	
	
	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOriginDate() {
		return originDate;
	}

	public void setOriginDate(String originDate) {
		this.originDate = originDate;
	}

	public String getOriginCode() {
		return originCode;
	}

	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}

	public String getOriginOrganization() {
		return originOrganization;
	}

	public void setOriginOrganization(String originOrganization) {
		this.originOrganization = originOrganization;
	}

	public String getOriginRegistryOffice() {
		return originRegistryOffice;
	}

	public void setOriginRegistryOffice(String originRegistryOffice) {
		this.originRegistryOffice = originRegistryOffice;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getAnnulledDate() {
		return annulledDate;
	}

	public void setAnnulledDate(String annulledDate) {
		this.annulledDate = annulledDate;
	}

	public String getAnnulledReason() {
		return annulledReason;
	}

	public void setAnnulledReason(String annulledReason) {
		this.annulledReason = annulledReason;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	public void setLinks(HashMap<String, String> links) {
		this.links = links;
	}

	public HashMap<String, String> getLinks() {
		return links;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Devuelve el JSON con la estructura de anotación (application/vnd.gestiona.registry-annotation+json)
	 * 
	 * @return String: String JSON en utf8
	 */
	public String toJSON() {
		StringBuilder json = initJSONObject();

		addKeyValueString(json, "code", code);
		addKeyValueNumber(json, "date", getDate());
		addKeyValueNumber(json, "annulled_date", annulledDate);
		addKeyValueNumber(json, "annulled_reason", annulledReason);
		addKeyValueString(json, "classification", classification);
		addKeyValueString(json, "delivery_type", deliveryType);
		addKeyValueString(json, "income_type", incomeType);
		addKeyValueString(json, "obs", longDescription);
		addKeyValueString(json, "origin_code", originCode);
		addKeyValueNumber(json, "origin_date", originDate);
		addKeyValueString(json, "origin_organization", originOrganization);
		addKeyValueString(json, "origin_registry_office", originRegistryOffice);
		addKeyValueString(json, "summary", shortDescription);
		addKeyValueString(json, "state", state);
		addKeyValueString(json, "type", type);
		addKeyValueString(json, "category", category);
		
		endJSONObject(json);
		
		return json.toString(); 
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	
}
