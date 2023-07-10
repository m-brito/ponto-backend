package io.github.mbrito.ponto.casoDeUso.ponto.dto;

import java.time.LocalDate;

public class BuscarPontoDTO {
	LocalDate data;

	public BuscarPontoDTO(LocalDate data) {
		super();
		this.data = data;
	}
	
	public BuscarPontoDTO() {}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BuscarPontoDTO [data=" + data + "]";
	}
}
