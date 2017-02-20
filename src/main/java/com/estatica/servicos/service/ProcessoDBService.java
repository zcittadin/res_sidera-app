package com.estatica.servicos.service;

import java.util.List;

import com.estatica.servicos.model.Processo;

public interface ProcessoDBService {

	public void saveProcesso(Processo processo);

	public List<Processo> findByLote(int lote);
}
