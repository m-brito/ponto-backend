package io.github.mbrito.ponto.casoDeUso.usuario.dto;

public class TokenDTO {
    private String login;
    private String tipo;
    private String token;
    
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public TokenDTO(String login, String token, String tipo) {
		super();
		this.login = login;
		this.token = token;
		this.tipo = tipo;
	}
	
	public TokenDTO() {}
}
