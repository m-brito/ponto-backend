package io.github.mbrito.ponto.casoDeUso.grupoHorario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.dto.GrupoHorarioDTO;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.service.GrupoHorarioService;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/grupohorario")
public class GrupoHorarioController {
	
	@Autowired
	GrupoHorarioService grupoHorarioService;
	
	@PostMapping
	ResponseEntity<GrupoHorario> novoGrupoHorario(@RequestBody GrupoHorarioDTO grupoHorarioDto) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        grupoHorarioDto.setUsuario(userId.intValue());
        return grupoHorarioService.salvar(grupoHorarioDto);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<GrupoHorario> deletarGrupo(@PathVariable Integer id) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return grupoHorarioService.deletar(id, userId.intValue());
	}
	
	@GetMapping("/{id}")
	ResponseEntity<GrupoHorario> buscarGrupoHorarioId(@PathVariable Integer id) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return grupoHorarioService.buscarGrupoHorarioId(id, userId.intValue());
	}
	
	@GetMapping()
	ResponseEntity<List<GrupoHorario>> buscarGrupoHorario() throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return grupoHorarioService.buscarGruposHorariosUsuario(userId.intValue());
	}
	
	@GetMapping("/paginado/{numeroPagina}/{qtdePagina}")
	ResponseEntity<Page<GrupoHorario>> buscarGrupoHorarioPaginado(@PathVariable Integer numeroPagina, @PathVariable Integer qtdePagina) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return grupoHorarioService.buscarGruposHorariosUsuarioPaginado(userId.intValue(), numeroPagina, qtdePagina);
	}
}
