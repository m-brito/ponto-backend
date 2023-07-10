package io.github.mbrito.ponto.casoDeUso.ponto.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public class PontoDTO {
	private LocalTime hora;
    private LocalDate data;
	private Integer grupoHorario;
	
	public PontoDTO(LocalTime hora, LocalDate data, Integer grupoHorario) {
		super();
		this.hora = hora;
		this.data = data;
		this.grupoHorario = grupoHorario;
	}
	
	public PontoDTO() {
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
	
	public Integer getGrupoHorario() {
		return grupoHorario;
	}

	public void setGrupoHorario(Integer grupoHorario) {
		this.grupoHorario = grupoHorario;
	}

	@Override
	public String toString() {
		return "PontoDTO [hora=" + hora + ", data=" + data + ", grupoHorario=" + grupoHorario
				+ "]";
	}	
}
