package com.estatica.servicos.dao.impl;

import java.util.List;

import org.hibernate.Session;

import com.estatica.servicos.dao.ProcessoDAO;
import com.estatica.servicos.model.Processo;
import com.estatica.servicos.util.HibernateUtil;

public class ProcessoDAOImpl implements ProcessoDAO {

	@Override
	public void saveProcesso(Processo processo) {
		Session session = HibernateUtil.openSession();
		session.beginTransaction();
		session.save(processo);
		session.getTransaction().commit();
		session.close();

	}

	@Override
	public List<Processo> findByLote(int lote) {
		// TODO Auto-generated method stub
		return null;
	}

}
