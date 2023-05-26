package io.github.mbrito.ponto.casoDeUso.usuario.dto;

public class CredenciaisDTO {
	private Integer id;
    private String email;
    private String senha;
    
    public CredenciaisDTO(String email, String senha) {
    	super();
    	this.email = email;
    	this.senha = senha;
    }
    
    public CredenciaisDTO(String email, String senha, Integer id) {
    	super();
    	this.id = id;
    	this.email = email;
    	this.senha = senha;
    }
    
    public CredenciaisDTO() {}
    
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CredenciaisDTO [id=" + id + ", email=" + email + ", senha=" + senha + "]";
	}
	
	
}