package io.github.mbrito.ponto.casoDeUso.grupoHorario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.Horario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.service.HorarioService;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;

@RestController
@RequestMapping("/api/horario")
public class HorarioController {
	
	@Autowired
	HorarioService horarioService;
	
	@PostMapping("/grupohorario/{idGrupoHorario}")
	ResponseEntity<Horario> novoHorario(@RequestBody Horario horario, @PathVariable Integer idGrupoHorario) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return horarioService.salvar(horario, userId.intValue(), idGrupoHorario);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<Horario> deletarHorario(@PathVariable Integer id) throws ResourceNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = (Long) authentication.getPrincipal();
		return horarioService.deletar(id, userId.intValue());
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Horario> editarHorario(@PathVariable Integer id,  @RequestBody Horario horario) throws ResourceNotFoundException {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
		return horarioService.editarHorario(horario, userId.intValue(), id);
	}
}
