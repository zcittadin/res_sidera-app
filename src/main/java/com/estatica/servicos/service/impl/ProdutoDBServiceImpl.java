package com.estatica.servicos.service.impl;

import java.util.Calendar;

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
	public Produto findByLote(int lote) {
		return dao.findByLote(lote);
	}

	@Override
	public void updateDataInicial(int lote) {
		Produto produto = findByLote(lote);
		produto.setDtInicial(Calendar.getInstance().getTime());
		dao.updateDataInicial(produto);
	}

	@Override
	public Produto findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public void updateDataFinal(int lote) {
		Produto produto = findByLote(lote);
		produto.setDtInicial(Calendar.getInstance().getTime());
		dao.updateDataFinal(produto);
	}

}
