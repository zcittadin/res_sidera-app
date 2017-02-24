package com.estatica.servicos.dto;

import com.estatica.servicos.model.Produto;

public class ReatorDTO {

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
		ReatorDTO.produto = produto;
	}

	public static String getCodProduto() {
		return codProduto;
	}

	public static void setCodProduto(String codProduto) {
		ReatorDTO.codProduto = codProduto;
	}

	public static String getLote() {
		return lote;
	}

	public static void setLote(String lote) {
		ReatorDTO.lote = lote;
	}

	public static String getQuantidade() {
		return quantidade;
	}

	public static void setQuantidade(String quantidade) {
		ReatorDTO.quantidade = quantidade;
	}

	public static String getOperador() {
		return operador;
	}

	public static void setOperador(String operador) {
		ReatorDTO.operador = operador;
	}

	public static Boolean getConfirmation() {
		return confirmation;
	}

	public static void setConfirmation(Boolean confirmation) {
		ReatorDTO.confirmation = confirmation;
	}

}
