package com.haulmont.testtask.entity.enumeration;

public enum Priority {
	NORMAL, CITO, STATIM;
		
	public String getCaption() {
		switch (this) {
		case NORMAL:
			return "Нормальный";
		case CITO:
			return "Cito";
		case STATIM:
			return "Статим";
		}
		return "";
	}
}