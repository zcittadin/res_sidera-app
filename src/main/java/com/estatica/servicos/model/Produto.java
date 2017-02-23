package com.estatica.servicos.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = 6912449479405564925L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "codigo")
	private int codigo;
	@Column(name = "lote")
	private int lote;
	@Column(name = "nome_reator")
	private String nomeReator;
	@Column(name = "operador")
	private String operador;
	@Column(name = "quantidade")
	private double quantidade;
	@Column(name = "dt_inicial")
	private Date dtInicial;
	@Column(name = "dt_final")
	private Date dtFinal;
	@Column(name = "temp_max")
	private double tempMax;
	@Column(name = "temp_min")
	private double tempMin;

	// @OneToMany(mappedBy = "i_produto", targetEntity = Processo.class, fetch =
	// FetchType.LAZY, cascade = CascadeType.ALL)
	// private List<Processo> processo;

	public Produto() {

	}

	public Produto(Long id, int codigo, int lote, String nomeReator, String operador, double quantidade, Date dtInicial,
			Date dtFinal, double tempMax, double tempMin) {
		this.id = id;
		this.codigo = codigo;
		this.lote = lote;
		this.nomeReator = nomeReator;
		this.operador = operador;
		this.quantidade = quantidade;
		this.dtInicial = dtInicial;
		this.dtFinal = dtFinal;
		this.tempMax = tempMax;
		this.tempMin = tempMin;
	}

	/*
	 * public Produto(Long id, int codigo, int lote, String nomeReator, String
	 * operador, double quantidade, Date dtInicial, Date dtFinal, List<Processo>
	 * processo) { this.id = id; this.codigo = codigo; this.lote = lote;
	 * this.nomeReator = nomeReator; this.operador = operador; this.quantidade =
	 * quantidade; this.dtInicial = dtInicial; this.dtFinal = dtFinal;
	 * this.processo = processo; }
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getLote() {
		return lote;
	}

	public void setLote(int lote) {
		this.lote = lote;
	}

	public String getNomeReator() {
		return nomeReator;
	}

	public void setNomeReator(String nomeReator) {
		this.nomeReator = nomeReator;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	/*
	 * public List<Processo> getProcesso() { return processo; }
	 * 
	 * public void setProcesso(List<Processo> processo) { this.processo =
	 * processo; }
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
