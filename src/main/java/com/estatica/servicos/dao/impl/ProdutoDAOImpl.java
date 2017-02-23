package com.estatica.servicos.dao.impl;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;

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
		return false;
	}

	@Override
	public Produto findByLote(int lote) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		String hql = "SELECT p FROM Produto p WHERE p.lote = " + lote;
		Query query = session.createQuery(hql);
		Produto p = (Produto) query.getSingleResult();
		session.close();
		return p;
	}

	@Override
	public void updateDataInicial(Produto produto) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Produto set dtInicial = :dtIni WHERE lote = :lote");
		query.setParameter("dtIni", produto.getDtInicial());
		query.setParameter("lote", produto.getLote());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	@Override
	public Produto findById(Long id) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		// String consulta = "SELECT p FROM Produto p WHERE p.id = :id";
		session.close();
		return null;
	}

	@Override
	public void updateDataFinal(Produto produto) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Produto set dtFinal = :dtFim WHERE lote = :lote");
		query.setParameter("dtFim", produto.getDtFinal());
		query.setParameter("lote", produto.getLote());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	@Override
	public void updateTemperaturaMax(Produto produto) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Produto set tempMax = :tempMax WHERE lote = :lote");
		query.setParameter("tempMax", produto.getTempMax());
		query.setParameter("lote", produto.getLote());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	@Override
	public void updateTemperaturaMin(Produto produto) {
		Session session = HibernateUtil.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("UPDATE Produto set tempMin = :tempMin WHERE lote = :lote");
		query.setParameter("tempMin", produto.getTempMin());
		query.setParameter("lote", produto.getLote());
		query.executeUpdate();
		tx.commit();
		session.close();
	}

}
