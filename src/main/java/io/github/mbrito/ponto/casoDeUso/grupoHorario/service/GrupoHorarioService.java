package io.github.mbrito.ponto.casoDeUso.grupoHorario.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.dto.GrupoHorarioDTO;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.dto.HorarioDTO;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.Horario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.repository.GrupoHorarioRepository;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.repository.HorarioRepository;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;
import io.github.mbrito.ponto.casoDeUso.usuario.repository.UsuarioRepository;
import io.github.mbrito.ponto.exceptions.PermissionDeniedException;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class GrupoHorarioService {
	
	@Autowired
	GrupoHorarioRepository grupoHorarioRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	HorarioRepository horarioRepository;
	
	@Transactional
    public ResponseEntity<GrupoHorario> salvar(GrupoHorarioDTO grupoHorarioDto) throws ResourceNotFoundException {
		Usuario usuario = usuarioRepository.findById(grupoHorarioDto.getUsuario())
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", grupoHorarioDto.getUsuario().toString()));
		
		GrupoHorario grupoHorario = new GrupoHorario();
		grupoHorario.setUsuario(usuario);
		grupoHorario.setNome(grupoHorarioDto.getNome());
		
		List<Horario> horarios = converterHorarios(grupoHorario, grupoHorarioDto.getHorarios());
		grupoHorarioRepository.save(grupoHorario);
		horarioRepository.saveAll(horarios);
		grupoHorario.setHorarios(horarios);
		return ResponseEntity.status(HttpStatus.CREATED).body(grupoHorario);
	}
	
	@Transactional
	public ResponseEntity<GrupoHorario> deletar(Integer id, Integer idUsuario) throws ResourceNotFoundException {
		GrupoHorario grupoHorario = grupoHorarioRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Grupo Horario", "Id", id.toString()));
		if(grupoHorario.getUsuario().getId() != idUsuario) {
			throw new PermissionDeniedException();
		} else {
			horarioRepository.deleteAll(grupoHorario.getHorarios());
			grupoHorarioRepository.delete(grupoHorario);
			return ResponseEntity.status(HttpStatus.OK).body(null);			
		}
	}
	
	public ResponseEntity<GrupoHorario> buscarGrupoHorarioId(Integer id, Integer idUsuario) throws ResourceNotFoundException {
	    Usuario usuario = usuarioRepository.findById(idUsuario)
	            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
	    Optional<GrupoHorario> grupoHorario = grupoHorarioRepository.findByIdAndUsuario(id, usuario);
	    if (grupoHorario.isPresent()) {
	        return ResponseEntity.status(HttpStatus.OK).body(grupoHorario.get());
	    } else {
	        throw new ResourceNotFoundException("GrupoHorario", "Id", id.toString());
	    }
	}
	
	public ResponseEntity<List<GrupoHorario>> buscarGruposHorariosUsuario(Integer idUsuario) throws ResourceNotFoundException {
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
		List<GrupoHorario> gruposHorarios = grupoHorarioRepository.findByUsuario(usuario);
		return ResponseEntity.status(HttpStatus.OK).body(gruposHorarios);
	}
	
	public ResponseEntity<Page<GrupoHorario>> buscarGruposHorariosUsuarioPaginado(Integer idUsuario, Integer numeroPagina, Integer qtdePagina) throws ResourceNotFoundException {
		if(qtdePagina >=5) qtdePagina = 5;
		Pageable page = PageRequest.of(numeroPagina, qtdePagina);
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
		Page<GrupoHorario> gruposHorarios = grupoHorarioRepository.findByUsuario(usuario, page);
		return ResponseEntity.status(HttpStatus.OK).body(gruposHorarios);
	}
	
	public List<Horario> converterHorarios(GrupoHorario grupoHorario, List<HorarioDTO> horarios) {
		List<Horario> horariosConvertidos = new ArrayList<>();
		
		for(int i=0; i<horarios.size(); i++) {
			Horario h = new Horario(grupoHorario, horarios.get(i).getNome(), horarios.get(i).getHora());
			horariosConvertidos.add(h);
		}
		
		return horariosConvertidos;
	}
}
