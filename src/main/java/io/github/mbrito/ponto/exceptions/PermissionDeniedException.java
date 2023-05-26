package io.github.mbrito.ponto.exceptions;

public class PermissionDeniedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public PermissionDeniedException(String mensagem) {
        super(mensagem);
    }
	
	public PermissionDeniedException() {
		super("Voce não em permissao para realizar esta acao");
	}
}
