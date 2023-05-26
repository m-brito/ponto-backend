package io.github.mbrito.ponto.casoDeUso.grupoHorario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public interface GrupoHorarioRepository extends JpaRepository<GrupoHorario, Integer> {
	Optional<GrupoHorario> findByIdAndUsuario(Integer id, Usuario usuario);
	
	public List<GrupoHorario> findByUsuario(Usuario usuario);
	
	public Page<GrupoHorario> findByUsuario(Usuario usuario, Pageable page);
}
