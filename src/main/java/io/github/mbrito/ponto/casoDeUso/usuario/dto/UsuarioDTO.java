package io.github.mbrito.ponto.casoDeUso.usuario.dto;

import java.time.LocalDate;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;

public class UsuarioDTO {
	private Integer id;
	private String nome;
    private String email;
    private String tipo;
    private String foto;
    private Integer diaFechamentoPonto;
    private LocalDate ultimaDataAprovada;
    private LocalDate dataCriacao;
    private GrupoHorario grupoHorario;
    
	public UsuarioDTO(Integer id, String nome, String email, String tipo, String foto, GrupoHorario grupoHorario, Integer diaFechamentoPonto, LocalDate ultimaDataAprovada, LocalDate dataCriacao) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.tipo = tipo;
		this.foto = foto;
		this.diaFechamentoPonto = diaFechamentoPonto;
		this.ultimaDataAprovada = ultimaDataAprovada;
		this.dataCriacao = dataCriacao;
		this.grupoHorario = grupoHorario;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getFoto() {
		return foto;
	}
	
	public void setFoto(String foto) {
		this.foto = foto;
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

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public GrupoHorario getGrupoHorario() {
		return grupoHorario;
	}
	
	public void setGrupoHorario(GrupoHorario grupoHorario) {
		this.grupoHorario = grupoHorario;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [id=" + id + ", nome=" + nome + ", email=" + email + ", tipo=" + tipo + ", foto=" + foto
				+ ", grupoHorario=" + grupoHorario + "]";
	}
}
