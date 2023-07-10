package io.github.mbrito.ponto.casoDeUso.usuario.dto;

import java.time.LocalDate;

import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public class UsuarioEditarDTO {
	private Usuario usuario;
	private Integer idGrupoHorario;
	private Integer diaFechamentoPonto;
	private LocalDate ultimaDataAprovada;
	
	public UsuarioEditarDTO(Usuario usuario, Integer idGrupoHorario) {
		super();
		this.usuario = usuario;
		this.idGrupoHorario = idGrupoHorario;
	}
	
	public UsuarioEditarDTO(Usuario usuario, Integer diaFechamentoPonto, LocalDate ultimaDataAprovada) {
		super();
		this.usuario = usuario;
		this.diaFechamentoPonto = diaFechamentoPonto;
		this.ultimaDataAprovada = ultimaDataAprovada;
	}
	
	public UsuarioEditarDTO() {}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Integer getDiaFechamentoPonto() {
		return diaFechamentoPonto;
	}

	public void setDiaFechamentoPonto(Integer diaFechamentoPonto) {
		this.diaFechamentoPonto = diaFechamentoPonto;
	}

	public LocalDate getUltimaDataAprovada() {
		return ultimaDataAprovada;
	}

	public void setUltimaDataAprovada(LocalDate ultimaDataAprovada) {
		this.ultimaDataAprovada = ultimaDataAprovada;
	}

	public Integer getIdGrupoHorario() {
		return idGrupoHorario;
	}

	public void setIdGrupoHorario(Integer idGrupoHorario) {
		this.idGrupoHorario = idGrupoHorario;
	}

	@Override
	public String toString() {
		return "UsuarioEditarDTO [usuario=" + usuario + ", idGrupoHorario=" + idGrupoHorario + "]";
	}
}
