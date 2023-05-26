package io.github.mbrito.ponto.casoDeUso.usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByEmailAndSenha(String email, String senha);
}
