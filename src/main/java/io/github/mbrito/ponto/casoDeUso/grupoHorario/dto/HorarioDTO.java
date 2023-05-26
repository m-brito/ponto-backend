package io.github.mbrito.ponto.casoDeUso.grupoHorario.dto;

import java.time.LocalTime;

public class HorarioDTO {
	private String nome;
	private LocalTime hora;
	
	public HorarioDTO(String nome, LocalTime hora) {
		super();
		this.nome = nome;
		this.hora = hora;
	}
	
	public HorarioDTO() {}	
	
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

	@Override
	public String toString() {
		return "HorarioDTO [nome=" + nome + ", hora=" + hora + "]";
	}
}
