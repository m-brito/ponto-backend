package io.github.mbrito.ponto.casoDeUso.grupoHorario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Integer> {
	
}
