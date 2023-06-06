package io.github.mbrito.ponto.casoDeUso.usuario.dto;

import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public class UsuarioEditarDTO {
	private Usuario usuario;
	private Integer idGrupoHorario;
	
	public UsuarioEditarDTO(Usuario usuario, Integer idGrupoHorario) {
		super();
		this.usuario = usuario;
		this.idGrupoHorario = idGrupoHorario;
	}
	
	public UsuarioEditarDTO() {}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
