package com.estatica.servicos.objectproperties;

import com.estatica.servicos.model.Produto;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProcessoConfigParams {

	private SimpleIntegerProperty codigoProperty = new SimpleIntegerProperty();
	private SimpleIntegerProperty loteProperty = new SimpleIntegerProperty();
	private SimpleStringProperty nameProperty = new SimpleStringProperty();
	private SimpleStringProperty operadorProperty = new SimpleStringProperty();
	private SimpleDoubleProperty quantidadeProperty = new SimpleDoubleProperty();
	private SimpleObjectProperty<Produto> produtoProperty = new SimpleObjectProperty<>();

	public ProcessoConfigParams(String name) {
		nameProperty.set(name);
	}

	public SimpleStringProperty nameProperty() {
		return nameProperty;
	}

	public String getName() {
		return nameProperty().get();
	}

	public void setName(String name) {
		nameProperty().set(name);
	}

	public SimpleIntegerProperty codigoProperty() {
		return codigoProperty;
	}

	public Integer getCodigo() {
		return codigoProperty().get();
	}

	public void setCodigo(Integer codigo) {
		codigoProperty().set(codigo);
	}

	public SimpleIntegerProperty loteProperty() {
		return loteProperty;
	}

	public Integer getLote() {
		return loteProperty().get();
	}

	public void setLote(Integer lote) {
		loteProperty().set(lote);
	}

	public SimpleStringProperty operadorProperty() {
		return operadorProperty;
	}

	public String getOperador() {
		return operadorProperty().get();
	}

	public void setOperador(String operador) {
		operadorProperty().set(operador);
	}

	public SimpleDoubleProperty quantidadeProperty() {
		return quantidadeProperty;
	}

	public Double getQuantidade() {
		return quantidadeProperty().get();
	}

	public void setQuantidade(Double quantidade) {
		quantidadeProperty().set(quantidade);
	}

	public SimpleObjectProperty<Produto> produtoProperty() {
		return produtoProperty;
	}

	public Produto getProduto() {
		return (Produto) produtoProperty().get();
	}

	public void setProduto(Produto produto) {
		produtoProperty().set(produto);
	}
}
