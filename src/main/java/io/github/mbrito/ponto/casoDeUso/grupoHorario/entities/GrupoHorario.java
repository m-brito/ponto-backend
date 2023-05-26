package io.github.mbrito.ponto.casoDeUso.grupoHorario.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.mbrito.ponto.casoDeUso.ponto.entities.Ponto;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@JsonIgnoreProperties({"usuario", "pontos"})
public class GrupoHorario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	private String nome;
	
	@OneToMany(mappedBy = "grupoHorario")
	private List<Horario> horarios;
	
	@OneToMany(mappedBy = "grupoHorario")
	private List<Ponto> pontos;
	
	public GrupoHorario(Integer id, Usuario usuario, String nome) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.nome = nome;
	}
	
	public GrupoHorario(Usuario usuario, String nome) {
		super();
		this.usuario = usuario;
		this.nome = nome;
	}
	
	public GrupoHorario() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	@Override
	public String toString() {
		return "GrupoHorario [id=" + id + ", usuario=" + usuario + ", nome=" + nome + ", horarios=" + horarios + "]";
	}
}
