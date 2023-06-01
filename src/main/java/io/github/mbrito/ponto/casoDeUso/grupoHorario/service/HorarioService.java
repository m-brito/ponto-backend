package io.github.mbrito.ponto.casoDeUso.grupoHorario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.Horario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.repository.GrupoHorarioRepository;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.repository.HorarioRepository;
import io.github.mbrito.ponto.casoDeUso.usuario.repository.UsuarioRepository;
import io.github.mbrito.ponto.exceptions.PermissionDeniedException;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;

@Service
public class HorarioService {
	
	@Autowired
	HorarioRepository horarioRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	GrupoHorarioRepository grupoHorarioRepository;
	
	public ResponseEntity<Horario> salvar(Horario horario, Integer idUsuario, Integer idGrupoHorario) throws ResourceNotFoundException {
		System.out.println(idGrupoHorario);
		GrupoHorario grupoHorario = grupoHorarioRepository.findById(idGrupoHorario)
				.orElseThrow(() -> new ResourceNotFoundException("GrupoHorario", "Id", horario.getGrupoHorario().getId().toString()));
		horario.setGrupoHorario(grupoHorario);
		if(grupoHorario.getUsuario().getId() != idUsuario) {
			throw new PermissionDeniedException();
		} else {
			horarioRepository.save(horario);
			return ResponseEntity.status(HttpStatus.CREATED).body(horario);
		}
	}
	
	public ResponseEntity<Horario> deletar(Integer id, Integer idUsuario) throws ResourceNotFoundException {
		Horario horario = horarioRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Horario", "Id", id.toString()));
		GrupoHorario grupoHorario = grupoHorarioRepository.findById(horario.getGrupoHorario().getId())
				.orElseThrow(() -> new ResourceNotFoundException("GrupoHorario", "Id", horario.getGrupoHorario().getId().toString()));
		if(grupoHorario.getUsuario().getId() != idUsuario) {
			throw new PermissionDeniedException();
		} else {
			horarioRepository.delete(horario);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}
	
	public ResponseEntity<Horario> editarHorario(Horario novoHorario, Integer idUsuario, Integer id) throws ResourceNotFoundException {
		Horario oldHorario = horarioRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Horario", "Id", id.toString()));
		GrupoHorario grupoHorario = grupoHorarioRepository.findById(oldHorario.getGrupoHorario().getId())
				.orElseThrow(() -> new ResourceNotFoundException("GrupoHorario", "Id", oldHorario.getGrupoHorario().getId().toString()));
		if(grupoHorario.getUsuario().getId() != idUsuario) {
			throw new PermissionDeniedException();
		} else {
			oldHorario.setNome(novoHorario.getNome() != null ? novoHorario.getNome() : oldHorario.getNome());
			oldHorario.setHora(novoHorario.getHora() != null ? novoHorario.getHora() : oldHorario.getHora());
			horarioRepository.save(oldHorario);
			return ResponseEntity.status(HttpStatus.OK).body(oldHorario);
		}
	}
}
