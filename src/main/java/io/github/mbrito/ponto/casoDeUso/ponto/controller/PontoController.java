package io.github.mbrito.ponto.casoDeUso.ponto.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.Horario;
import io.github.mbrito.ponto.casoDeUso.ponto.dto.BuscarPontoDTO;
import io.github.mbrito.ponto.casoDeUso.ponto.dto.PontoDTO;
import io.github.mbrito.ponto.casoDeUso.ponto.entities.Ponto;
import io.github.mbrito.ponto.casoDeUso.ponto.service.PontoService;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/ponto")
public class PontoController {
	@Autowired
	PontoService pontoService;
	
	@PostMapping
	ResponseEntity<Ponto> novoPonto(@RequestBody PontoDTO pontoDto) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return pontoService.salvar(pontoDto, userId.intValue());
	}
	
	@GetMapping("/{data}")
	ResponseEntity<List<Ponto>> buscarPontoDia(@PathVariable LocalDate data) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return pontoService.buscarPontoDia(userId.intValue(), data);
	}
	
	@GetMapping("/historico/{idUsuarioConsulta}")
	ResponseEntity<List<Ponto>> buscarPontoPeriodo(@PathVariable Integer idUsuarioConsulta, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return pontoService.buscarPontoPeriodo(userId.intValue(), idUsuarioConsulta, dataInicial, dataFinal);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Ponto> editarPonto(@PathVariable Integer id,  @RequestBody Ponto ponto) throws ResourceNotFoundException {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
		return pontoService.editarPonto(userId.intValue(), id, ponto);
	}
	
	@DeleteMapping("/{data}")
	ResponseEntity<List<Ponto>> deletarPonto(@PathVariable LocalDate data) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return pontoService.deletarPontoDia(userId.intValue(), data);
	}
}
