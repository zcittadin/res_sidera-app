package com.estatica.servicos.service;

import java.util.List;

import com.estatica.servicos.model.Produto;

public interface ProdutoDBService {

	public void saveProduto(Produto produto);

	public boolean isLoteExists(int lote);

	public List<Produto> findByLote(int lote);
}
