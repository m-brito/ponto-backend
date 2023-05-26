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
	
	public ResponseEntity<Horario> salvar(Horario horario, Integer idUsuario) throws ResourceNotFoundException {
		GrupoHorario grupoHorario = grupoHorarioRepository.findById(horario.getGrupoHorario().getId())
				.orElseThrow(() -> new ResourceNotFoundException("GrupoHorario", "Id", horario.getGrupoHorario().getId().toString()));
		if(grupoHorario.getId() != idUsuario) {
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
		if(grupoHorario.getId() != idUsuario) {
			throw new PermissionDeniedException();
		} else {
			horarioRepository.delete(horario);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}
	
	public ResponseEntity<Horario> editarHorario(Horario novoHorario, Integer idUsuario, Integer idGrupoHorario) throws ResourceNotFoundException {
		GrupoHorario grupoHorario = grupoHorarioRepository.findById(idGrupoHorario)
				.orElseThrow(() -> new ResourceNotFoundException("GrupoHorario", "Id", idGrupoHorario.toString()));
		Horario horarioOld = horarioRepository.findById(novoHorario.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Horario", "Id", novoHorario.getId().toString()));
		if(grupoHorario.getId() != novoHorario.getGrupoHorario().getId()) {
			throw new PermissionDeniedException();
		} else {
			horarioOld.setNome(novoHorario.getNome() != null ? novoHorario.getNome() : horarioOld.getNome());
			horarioOld.setHora(novoHorario.getHora() != null ? novoHorario.getHora() : horarioOld.getHora());
			horarioRepository.save(horarioOld);
			return ResponseEntity.status(HttpStatus.OK).body(horarioOld);
		}
	}
}
