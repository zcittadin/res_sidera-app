package com.estatica.servicos.service.impl;

import java.util.List;

import com.estatica.servicos.dao.ProcessoDAO;
import com.estatica.servicos.dao.impl.ProcessoDAOImpl;
import com.estatica.servicos.model.Processo;
import com.estatica.servicos.service.ProcessoDBService;

public class ProcessoDBServiceImpl implements ProcessoDBService {

	private ProcessoDAO dao = new ProcessoDAOImpl();

	@Override
	public void saveProcesso(Processo processo) {
		dao.saveProcesso(processo);

	}

	@Override
	public List<Processo> findByLote(int lote) {
		// TODO Auto-generated method stub
		return null;
	}

}
