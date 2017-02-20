package com.estatica.servicos.dto;

import eu.hansolo.medusa.LcdDesign;

public class StyleClockDTO {

	private static LcdDesign style;

	public static LcdDesign getStyle() {
		return style;
	}

	public static void setStyle(LcdDesign style) {
		StyleClockDTO.style = style;
	}

}
