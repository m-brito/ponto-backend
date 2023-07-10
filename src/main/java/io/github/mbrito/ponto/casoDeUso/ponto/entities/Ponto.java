package io.github.mbrito.ponto.casoDeUso.ponto.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.Horario;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIgnoreProperties({"usuario"})
public class Ponto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime hora;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;
	
	@ManyToOne
	private Usuario usuario;
	
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime horaTemplate;

	public Ponto() {
		
	}
	
	public Ponto(int id, LocalTime hora, LocalDate data, Usuario usuario, LocalTime horaTemplate) {
		super();
		this.id = id;
		this.hora = hora;
		this.data = data;
		this.usuario = usuario;
		this.horaTemplate = horaTemplate;
	}
	
	public Ponto(LocalTime hora, LocalDate data, Usuario usuario, LocalTime horaTemplate) {
		super();
		this.hora = hora;
		this.data = data;
		this.usuario = usuario;
		this.horaTemplate = horaTemplate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalTime getHoraTemplate() {
		return horaTemplate;
	}

	public void setHoraTemplate(LocalTime horaTemplate) {
		this.horaTemplate = horaTemplate;
	}

	@Override
	public String toString() {
		return "Ponto [id=" + id + ", hora=" + hora + ", data=" + data + ", usuario=" + usuario + ", horaTemplate="
				+ horaTemplate + "]";
	}
	
}
