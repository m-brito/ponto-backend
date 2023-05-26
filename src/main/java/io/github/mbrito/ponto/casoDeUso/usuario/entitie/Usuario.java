package io.github.mbrito.ponto.casoDeUso.usuario.entitie;

import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.ponto.entities.Ponto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "usuario")
@JsonIgnoreProperties({"gruposHorarios", "pontos"})
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotEmpty(message = "{nome.not.empty}")
	private String nome;
	
	@NotEmpty(message = "{email.not.empty}")
	@Column(unique = true)
	private String email;
	
	@NotEmpty(message = "{senha.not.empty}")
	private String senha;
	
	private String tipo;
	
	private String foto;
	
	@OneToMany(mappedBy = "usuario")
	private List<GrupoHorario> gruposHorarios;
	
	@OneToOne
	private GrupoHorario grupoHorario;
	
	@OneToMany(mappedBy = "usuario")
	private List<Ponto> pontos;

	public Usuario(int id, String nome, String email, String senha, String foto) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.foto = foto;
	}
	
	public Usuario(String nome, String email, String senha) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}
	
	public Usuario(String email, String senha) {
		super();
		this.email = email;
		this.senha = senha;
	}
	
	public Usuario() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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

	public List<GrupoHorario> getGruposHorarios() {
		return gruposHorarios;
	}

	public void setGruposHorarios(List<GrupoHorario> gruposHorarios) {
		this.gruposHorarios = gruposHorarios;
	}

	public GrupoHorario getGrupoHorario() {
		return grupoHorario;
	}

	public void setGrupoHorario(GrupoHorario grupoHorario) {
		this.grupoHorario = grupoHorario;
	}

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}
	
	public static class Builder {
		private String login;
		private String senha;
		
		public Builder withLogin(String login) {
			this.login = login;
			return this;
		}
		
		public Builder withSenha(String senha) {
			this.senha = senha;
			return this;
		}
		
		public Usuario build() {
			return new Usuario(login, senha);
		}
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + "]";
	}
}
