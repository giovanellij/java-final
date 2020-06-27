package com.java.alquiler.business;

public class SearchServicioCriteria {
	public String fromDate;
	public String toDate;
	public Boolean alquileres;
	public Boolean devoluciones;
	public String searchBy;
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
}
