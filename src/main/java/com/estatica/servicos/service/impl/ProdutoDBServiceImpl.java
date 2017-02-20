package com.estatica.servicos.service.impl;

import java.util.List;

import com.estatica.servicos.dao.ProdutoDAO;
import com.estatica.servicos.dao.impl.ProdutoDAOImpl;
import com.estatica.servicos.model.Produto;
import com.estatica.servicos.service.ProdutoDBService;

public class ProdutoDBServiceImpl implements ProdutoDBService {

	private ProdutoDAO dao = new ProdutoDAOImpl();

	@Override
	public void saveProduto(Produto produto) {
		dao.saveProduto(produto);

	}

	@Override
	public boolean isLoteExists(int lote) {
		return dao.isLoteExists(lote);
	}

	@Override
	public List<Produto> findByLote(int lote) {
		return dao.findByLote(lote);
	}

}
