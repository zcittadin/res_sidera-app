package com.estatica.servicos.dto;

import com.estatica.servicos.model.Produto;

public class Reator1DTO {

	public static String codProduto;
	public static String lote;
	public static String quantidade;
	public static String operador;
	public static Boolean confirmation;

	public static Produto produto;

	public static Produto getProduto() {
		return produto;
	}

	public static void setProduto(Produto produto) {
		Reator1DTO.produto = produto;
	}

	public static String getCodProduto() {
		return codProduto;
	}

	public static void setCodProduto(String codProduto) {
		Reator1DTO.codProduto = codProduto;
	}

	public static String getLote() {
		return lote;
	}

	public static void setLote(String lote) {
		Reator1DTO.lote = lote;
	}

	public static String getQuantidade() {
		return quantidade;
	}

	public static void setQuantidade(String quantidade) {
		Reator1DTO.quantidade = quantidade;
	}

	public static String getOperador() {
		return operador;
	}

	public static void setOperador(String operador) {
		Reator1DTO.operador = operador;
	}

	public static Boolean getConfirmation() {
		return confirmation;
	}

	public static void setConfirmation(Boolean confirmation) {
		Reator1DTO.confirmation = confirmation;
	}

}
