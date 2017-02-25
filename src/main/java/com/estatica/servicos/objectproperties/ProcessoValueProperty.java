package com.estatica.servicos.objectproperties;

import javafx.beans.property.SimpleDoubleProperty;

public class ProcessoValueProperty {

	private static SimpleDoubleProperty tempReator1 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty spReator1 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty tempReator2 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty spReator2 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty tempReator3 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty spReator3 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty tempReator4 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty spReator4 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty tempReator5 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty spReator5 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty tempReator6 = new SimpleDoubleProperty();
	private static SimpleDoubleProperty spReator6 = new SimpleDoubleProperty();

	public static SimpleDoubleProperty tempReator1Property() {
		return tempReator1;
	}

	public static Double getTempReator1() {
		return tempReator1Property().get();
	}

	public static void setTempReator1(Double temp) {
		tempReator1Property().set(temp);
	}

	public static SimpleDoubleProperty spReator1Property() {
		return spReator1;
	}

	public static Double getSpReator1() {
		return spReator1Property().get();
	}

	public static void setSpReator1(Double temp) {
		spReator1Property().set(temp);
	}

	// ----------------------------------------------------------

	public static SimpleDoubleProperty tempReator2Property() {
		return tempReator2;
	}

	public static Double getTempReator2() {
		return tempReator2Property().get();
	}

	public static void setTempReator2(Double temp) {
		tempReator2Property().set(temp);
	}

	public static SimpleDoubleProperty spReator2Property() {
		return spReator2;
	}

	public static Double getSpReator2() {
		return spReator2Property().get();
	}

	public static void setSpReator2(Double temp) {
		spReator2Property().set(temp);
	}
}
