package com.estatica.servicos.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "processo")
public class Processo implements Serializable {

	private static final long serialVersionUID = 7209053364487200761L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "i_produto")
	private Produto produto;
	@Column(name = "dt_processo")
	private Date dtProcesso;
	@Column(name = "t_reator")
	private double tempReator;
	@Column(name = "sp_reator")
	private double spReator;

	public Processo() {

	}

	public Processo(Long id, Produto produto, Date dtProcesso, double tempReator, double spReator) {
		this.id = id;
		this.produto = produto;
		this.dtProcesso = dtProcesso;
		this.tempReator = tempReator;
		this.spReator = spReator;
	}

	public Processo(Long id, Date dtProcesso, double tempReator, double spReator) {
		this.id = id;
		this.dtProcesso = dtProcesso;
		this.tempReator = tempReator;
		this.spReator = spReator;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Date getDtProcesso() {
		return dtProcesso;
	}

	public void setDtProcesso(Date dtProcesso) {
		this.dtProcesso = dtProcesso;
	}

	public double getTempReator() {
		return tempReator;
	}

	public void setTempReator(double tempReator) {
		this.tempReator = tempReator;
	}

	public double getSpReator() {
		return spReator;
	}

	public void setSpReator(double spReator) {
		this.spReator = spReator;
	}

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
		Processo other = (Processo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
