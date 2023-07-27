package io.github.mbrito.ponto.casoDeUso.ponto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.mbrito.ponto.casoDeUso.ponto.entities.Ponto;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;

public interface PontoRepository extends JpaRepository<Ponto, Integer> {
	List<Ponto> findByData(LocalDate data);
	
	List<Ponto> findByDataAndUsuarioOrderByData(LocalDate data, Usuario usuario);
	
	@Query("SELECT ponto FROM Ponto ponto JOIN ponto.usuario usuario WHERE usuario = :usuario AND ponto.data BETWEEN :dataInicial AND :dataFinal AND ponto.data < usuario.ultimaDataAprovada ORDER BY ponto.data")
	List<Ponto> findByUsuarioAndDataBetweenOrderByDataAprovada(Usuario usuario, LocalDate dataInicial, LocalDate dataFinal);

	List<Ponto> findByUsuarioAndDataBetweenOrderByData(Usuario usuario, LocalDate dataInicial, LocalDate dataFinal);

}
