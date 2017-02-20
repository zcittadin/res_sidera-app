package com.estatica.servicos.dao;

import java.util.List;

import com.estatica.servicos.model.Processo;

public interface ProcessoDAO {

	public void saveProcesso(Processo processo);
	
	public List<Processo> findByLote(int lote);
}
