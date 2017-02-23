package com.estatica.servicos.dao;

import com.estatica.servicos.model.Produto;

public interface ProdutoDAO {

	public void saveProduto(Produto produto);

	public boolean isLoteExists(int lote);

	public Produto findByLote(int lote);

	public Produto findById(Long id);

	// public void removeProduto(Long id);

	public void updateDataInicial(Produto produto);
	
	public void updateDataFinal(Produto produto);
	
	public void updateTemperaturaMax(Produto produto);
	
	public void updateTemperaturaMin(Produto produto);
}
