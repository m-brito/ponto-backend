package io.github.mbrito.ponto.casoDeUso.usuario.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByEmailAndSenha(String email, String senha);
	
	@Query("SELECT usuario FROM Usuario usuario WHERE usuario.tipo = 'USER'")
	public Page<Usuario> obterTodosUsuariosPageable(Pageable pageble);
	
	@Query("SELECT usuario FROM Usuario usuario WHERE usuario.tipo = 'USER' AND usuario.nome LIKE %:pesquisa%")
	public Page<Usuario> obterTodosUsuariosNomePageable(String pesquisa, Pageable pageble);
}
