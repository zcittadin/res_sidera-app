package com.estatica.servicos.service;

import com.estatica.servicos.model.Produto;

public interface ProdutoDBService {

	public void saveProduto(Produto produto);

	public Produto findById(Long id);

	public boolean isLoteExists(int lote);

	public Produto findByLote(int lote);

	public void updateDataInicial(int lote);
	
	public void updateDataFinal(int lote);
}
