package com.estatica.servicos.dao;

import java.util.List;

import com.estatica.servicos.model.Produto;

public interface ProdutoDAO {

	public void saveProduto(Produto produto);

	public boolean isLoteExists(int lote);

	public List<Produto> findByLote(int lote);

	// public void removeProduto(Long id);

	// public void updateProduto(Produto produto);
}
