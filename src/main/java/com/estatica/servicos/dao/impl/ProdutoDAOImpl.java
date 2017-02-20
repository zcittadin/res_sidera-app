package com.estatica.servicos.dao.impl;

import java.util.List;

import org.hibernate.Session;

import com.estatica.servicos.dao.ProdutoDAO;
import com.estatica.servicos.model.Produto;
import com.estatica.servicos.util.HibernateUtil;

public class ProdutoDAOImpl implements ProdutoDAO {

	@Override
	public void saveProduto(Produto produto) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(produto);
		session.getTransaction().commit();
		session.close();

	}

	@Override
	public boolean isLoteExists(int lote) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Produto> findByLote(int lote) {
		// TODO Auto-generated method stub
		return null;
	}

}
