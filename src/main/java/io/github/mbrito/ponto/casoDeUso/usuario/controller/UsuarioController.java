package io.github.mbrito.ponto.casoDeUso.usuario.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.github.mbrito.ponto.casoDeUso.usuario.dto.CredenciaisDTO;
import io.github.mbrito.ponto.casoDeUso.usuario.dto.TokenDTO;
import io.github.mbrito.ponto.casoDeUso.usuario.dto.UsuarioDTO;
import io.github.mbrito.ponto.casoDeUso.usuario.dto.UsuarioEditarDTO;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;
import io.github.mbrito.ponto.casoDeUso.usuario.service.UsuarioService;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;
import io.github.mbrito.ponto.exceptions.SenhaInvalidaException;
import io.github.mbrito.ponto.security.jwt.JwtService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
	
	@Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@Valid Usuario usuario, MultipartFile fotoUsuario){
         return usuarioService.salvar(usuario, fotoUsuario);
    }
    
    @PatchMapping()
	public ResponseEntity<UsuarioDTO> editarUsuario(@RequestBody UsuarioEditarDTO dto) throws Exception {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
		return usuarioService.editarUsuarioParcial(dto.getUsuario(), dto.getIdGrupoHorario(), userId.intValue());
	}
    
    @PutMapping("/foto")
    public ResponseEntity<UsuarioDTO> editarFoto(MultipartFile fotoUsuario) throws ResourceNotFoundException{
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return usuarioService.editarFoto(fotoUsuario, userId.intValue());
    }
    
    @GetMapping("/{id}")
	public ResponseEntity<Usuario> buscarUsuario(@PathVariable int id) throws ResourceNotFoundException {
//    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = (Long) authentication.getPrincipal();
//        System.out.println(userId);
		return usuarioService.obterUsuarioId(id);
	}
    
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> buscarPerfil() throws ResourceNotFoundException {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
    	return usuarioService.obterPerfilId(userId.intValue());
    }
    
    @GetMapping("/perfis")
    public ResponseEntity<Page<UsuarioDTO>> buscarPerfis(@RequestParam(name = "pagina", defaultValue = "0") int pagina,
			@RequestParam(name = "maxPage", defaultValue = "50") int maxPage) throws ResourceNotFoundException {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Long userId = (Long) authentication.getPrincipal();
    	return usuarioService.obterPerfis(userId.intValue(), pagina, maxPage);
    }
    
    @GetMapping("/perfis/{pesquisa}")
    public ResponseEntity<Page<UsuarioDTO>> buscarPerfisPesquisa(@PathVariable String pesquisa, @RequestParam(name = "pagina", defaultValue = "0") int pagina,
    		@RequestParam(name = "maxPage", defaultValue = "50") int maxPage) throws ResourceNotFoundException {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Long userId = (Long) authentication.getPrincipal();
    	return usuarioService.obterPerfisPesquisa(userId.intValue(), pesquisa, pagina, maxPage);
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenDTO> autenticar(@RequestBody CredenciaisDTO credenciais) throws Exception{
            Usuario usuario = new Usuario.Builder()
                    .withLogin(credenciais.getEmail())
                    .withSenha(credenciais.getSenha()).build();
            return usuarioService.autenticar(usuario);
    }

}