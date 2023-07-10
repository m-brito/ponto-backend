package io.github.mbrito.ponto.casoDeUso.ponto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.mbrito.ponto.casoDeUso.ponto.entities.Ponto;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public interface PontoRepository extends JpaRepository<Ponto, Integer> {
	List<Ponto> findByData(LocalDate data);
	
	List<Ponto> findByDataAndUsuarioOrderByData(LocalDate data, Usuario usuario);
	
	List<Ponto> findByUsuarioAndDataBetweenOrderByData(Usuario usuario, LocalDate dataInicial, LocalDate dataFinal);

}
