package io.github.mbrito.ponto.casoDeUso.grupoHorario.dto;

import java.util.List;

import com.google.firebase.database.annotations.NotNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class GrupoHorarioDTO {
	private Integer usuario;
	
	@NotBlank
	@NotNull
	private String nome;
	
	@NotBlank
	@NotEmpty
	@NotNull
	private List<HorarioDTO> horarios;
	
	public GrupoHorarioDTO(String nome, List<HorarioDTO> horarios, Integer usuario) {
		super();
		this.nome = nome;
		this.horarios = horarios;
		this.usuario = usuario;
	}
	
	public GrupoHorarioDTO(String nome, List<HorarioDTO> horarios) {
		super();
		this.nome = nome;
		this.horarios = horarios;
	}
	
	public GrupoHorarioDTO() {}

	public Integer getUsuario() {
		return usuario;
	}

	public void setUsuario(Integer usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<HorarioDTO> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<HorarioDTO> horarios) {
		this.horarios = horarios;
	}

	@Override
	public String toString() {
		return "GrupoHorarioDTO [usuario=" + usuario + ", nome=" + nome + ", horarios=" + horarios + "]";
	}
}
