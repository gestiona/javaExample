package com.espublico.expedientes.api.examples.model;

import java.util.List;

public class RestError {
	private static final long serialVersionUID = 1L;
	public static transient final String mediaType = "application/vnd.gestiona.error+json";

	private Long code;
	private String name;
	private String description;
	private List<String> details;
	private String internalCodeError;
	private String technicalDetails;

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTechnicalDetails(String technicalDetails) {
		this.technicalDetails = technicalDetails;
	}

	public String getTechnicalDetails() {
		return technicalDetails;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Long getCode() {
		return code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	public String getInternalCodeError() {
		return internalCodeError;
	}

	public void setInternalCodeError(String internalCodeError) {
		this.internalCodeError = internalCodeError;
	}

}
