package io.github.mbrito.ponto.casoDeUso.grupoHorario.entities;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIgnoreProperties({"grupoHorario"})
public class Horario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	private GrupoHorario grupoHorario;
	
	private String nome;
	
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime hora;

	public Horario(GrupoHorario grupoHorario, Integer id, String nome, LocalTime hora) {
		super();
		this.id = id;
		this.nome = nome;
		this.hora = hora;
		this.grupoHorario = grupoHorario;
	}
	
	public Horario(GrupoHorario grupoHorario, String nome, LocalTime hora) {
		super();
		this.nome = nome;
		this.hora = hora;
		this.grupoHorario = grupoHorario;
	}
	
	public Horario() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public GrupoHorario getGrupoHorario() {
		return grupoHorario;
	}

	public void setGrupoHorario(GrupoHorario grupoHorario) {
		this.grupoHorario = grupoHorario;
	}

	@Override
	public String toString() {
		return "Horario [id=" + id + ", grupoHorario=" + grupoHorario + ", nome=" + nome + ", hora=" + hora + "]";
	}
}
