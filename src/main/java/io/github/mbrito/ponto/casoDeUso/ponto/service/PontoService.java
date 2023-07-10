package io.github.mbrito.ponto.casoDeUso.ponto.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.Horario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.repository.GrupoHorarioRepository;
import io.github.mbrito.ponto.casoDeUso.ponto.dto.PontoDTO;
import io.github.mbrito.ponto.casoDeUso.ponto.entities.Ponto;
import io.github.mbrito.ponto.casoDeUso.ponto.repository.PontoRepository;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;
import io.github.mbrito.ponto.casoDeUso.usuario.repository.UsuarioRepository;
import io.github.mbrito.ponto.exceptions.PermissionDeniedException;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PontoService {
	@Autowired
	PontoRepository pontoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	GrupoHorarioRepository grupoHorarioRepository;
		
	@Transactional
    public ResponseEntity<Ponto> salvar(PontoDTO pontoDto, Integer idUsuario) throws Exception {
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
		
		GrupoHorario grupoHorario = grupoHorarioRepository.findById(pontoDto.getGrupoHorario())
				.orElseThrow(() -> new ResourceNotFoundException("GrupoHorario", "Id", pontoDto.getGrupoHorario().toString()));
		
		List<Ponto> pontos = buscarPontoDia(idUsuario, pontoDto.getData()).getBody();
		
		int countPontos = 0;
		
		for(Ponto p : pontos) {
			if(p.getHora() != null) {
				countPontos++;
			}
		}
		
		if(countPontos >= grupoHorario.getHorarios().size()) {
			throw new Exception("A folha de ponto ja foi totalmente preenchida");
		} else if(countPontos != 0) {
			Ponto pontoNovo = pontos.get(countPontos);
			pontoNovo.setHora(pontoDto.getHora());
			pontoRepository.save(pontoNovo);
			return ResponseEntity.status(HttpStatus.CREATED).body(pontoNovo);			
		} else {
			for(Horario h : grupoHorario.getHorarios()) {
				Ponto ponto = new Ponto(null, pontoDto.getData(), usuario, h.getHora());
				pontoRepository.save(ponto);
			}
			List<Ponto> pontosAtualizados = buscarPontoDia(idUsuario, pontoDto.getData()).getBody();
			Ponto pontoNovo = pontosAtualizados.get(0);
			pontoNovo.setHora(pontoDto.getHora());
			pontoRepository.save(pontoNovo);
			return ResponseEntity.status(HttpStatus.CREATED).body(pontoNovo);			
		}
		
	}
	
	public ResponseEntity<List<Ponto>> buscarPontoDia(Integer idUsuario, LocalDate data) throws ResourceNotFoundException {
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
		List<Ponto> pontos = pontoRepository.findByDataAndUsuarioOrderByData(data, usuario);
		return ResponseEntity.status(HttpStatus.OK).body(pontos);
	}
	
	public ResponseEntity<List<Ponto>> deletarPontoDia(Integer idUsuario, LocalDate data) throws ResourceNotFoundException {
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
		List<Ponto> pontos = pontoRepository.findByDataAndUsuarioOrderByData(data, usuario);
		pontoRepository.deleteAll(pontos);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	public ResponseEntity<Ponto> editarPonto(Integer idUsuario, Integer idPonto, Ponto novoPonto) throws ResourceNotFoundException {
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
		Ponto oldPonto= pontoRepository.findById(idPonto)
				.orElseThrow(() -> new ResourceNotFoundException("Ponto", "Id", idPonto.toString()));
		if(!oldPonto.getUsuario().getId().equals(usuario.getId())) {
			throw new PermissionDeniedException("Voce nao tem permissao para esta acao!");
		} else {
			oldPonto.setHora(novoPonto.getHora() != null ? novoPonto.getHora() : oldPonto.getHora());
			oldPonto.setHoraTemplate(novoPonto.getHoraTemplate() != null ? novoPonto.getHoraTemplate() : oldPonto.getHoraTemplate());
			pontoRepository.save(oldPonto);
			return ResponseEntity.status(HttpStatus.OK).body(oldPonto);
		}
	}
	
	public ResponseEntity<List<Ponto>> buscarPontoPeriodo(Integer idUsuario, Integer idUsuarioConsulta, LocalDate dataInicial, LocalDate dataFinal) throws ResourceNotFoundException {
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuario.toString()));
		Usuario usuarioConsulta = usuarioRepository.findById(idUsuarioConsulta)
				.orElseThrow(() -> new ResourceNotFoundException("Usuario", "Id", idUsuarioConsulta.toString()));
		if(usuario.getTipo().equals("USER") && idUsuario != idUsuarioConsulta) {
			throw new PermissionDeniedException();
		} else {
			List<Ponto> pontos = pontoRepository.findByUsuarioAndDataBetweenOrderByData(usuarioConsulta, dataInicial, dataFinal);
			return ResponseEntity.status(HttpStatus.OK).body(pontos);			
		}
	}
}
