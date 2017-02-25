package com.estatica.servicos.objectproperties;

import javafx.beans.property.SimpleIntegerProperty;

public class ProcessoValueProperty {

	private static SimpleIntegerProperty tempReator1 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty spReator1 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty tempReator2 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty spReator2 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty tempReator3 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty spReator3 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty tempReator4 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty spReator4 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty tempReator5 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty spReator5 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty tempReator6 = new SimpleIntegerProperty();
	private static SimpleIntegerProperty spReator6 = new SimpleIntegerProperty();

	public static SimpleIntegerProperty tempReator1Property() {
		return tempReator1;
	}

	public static Integer getTempReator1() {
		return tempReator1Property().get();
	}

	public static void setTempReator1(Integer temp) {
		tempReator1Property().set(temp);
	}

	public static SimpleIntegerProperty spReator1Property() {
		return spReator1;
	}

	public static Integer getSpReator1() {
		return spReator1Property().get();
	}

	public static void setSpReator1(Integer temp) {
		spReator1Property().set(temp);
	}
	
	//----------------------------------------------------------
	
	public static SimpleIntegerProperty tempReator2Property() {
		return tempReator2;
	}

	public static Integer getTempReator2() {
		return tempReator2Property().get();
	}

	public static void setTempReator2(Integer temp) {
		tempReator2Property().set(temp);
	}

	public static SimpleIntegerProperty spReator2Property() {
		return spReator2;
	}

	public static Integer getSpReator2() {
		return spReator2Property().get();
	}

	public static void setSpReator2(Integer temp) {
		spReator2Property().set(temp);
	}
}
