package io.github.mbrito.ponto.casoDeUso.usuario.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.mbrito.ponto.casoDeUso.grupoHorario.entities.GrupoHorario;
import io.github.mbrito.ponto.casoDeUso.grupoHorario.service.GrupoHorarioService;
import io.github.mbrito.ponto.casoDeUso.usuario.dto.TokenDTO;
import io.github.mbrito.ponto.casoDeUso.usuario.dto.UsuarioDTO;
import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;
import io.github.mbrito.ponto.casoDeUso.usuario.repository.UsuarioRepository;
import io.github.mbrito.ponto.exceptions.PermissionDeniedException;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;
import io.github.mbrito.ponto.exceptions.SenhaInvalidaException;
import io.github.mbrito.ponto.security.jwt.JwtService;
import io.github.mbrito.ponto.service.FirebaseService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	@Lazy
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private GrupoHorarioService grupoHorarioService;
    
    @Autowired
    private FirebaseService firebaseService;
    
    @Autowired
    private JwtService jwtService;
    
    String localSalvarImagem = "firebase";
    
    @Transactional
    public Usuario salvar(Usuario usuario, MultipartFile file){
    	usuario.setTipo("USER");
    	if (existeUsuarioComEmail(usuario.getEmail())) {
    		throw new IllegalArgumentException("E-mail já cadastrado");
    	} else {
    		String senhaCriptografada = encoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);
            String foto = "";
    		try {
    			if(file != null) {
    				BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
    				if (image == null) {
    					throw new IllegalArgumentException("O arquivo enviado não é uma imagem válida.");
    				} else {
    					if(localSalvarImagem == "firebase") {
    						byte[] imageData = file.getBytes();
    						String fileName = UUID.randomUUID().toString();
    						foto = firebaseService.saveImage(imageData, fileName);					
    					} else if(localSalvarImagem == "servidor") {
    						String filename = file.getOriginalFilename();
    						Path destPath = Paths.get("src", "main", "java", "io", "github", "mbrito", "ponto", "imagens");
    						String dest = destPath.toAbsolutePath().toString();
    						Path destination = Paths.get(dest, filename);
    						Files.write(destination, file.getBytes());
    						foto = destination.toString();
    					}
    				}
    			}
    			usuario.setFoto(foto);
    			usuario.setDiaFechamentoPonto(15);
    			usuario.setUltimaDataAprovada(LocalDate.now());
    			usuario.setDataCriacao(LocalDate.now());
    			Usuario usuarioSalvo = repository.save(usuario);
    			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/" + usuarioSalvo.getId()).build().toUri();
    			return usuarioSalvo;	
    	    } catch (IOException e) {
    	        throw new RuntimeException("Erro ao salvar o arquivo de imagem.", e);
    	    }
    	}
    }
    
    @Transactional
    public ResponseEntity<UsuarioDTO> editarFoto(MultipartFile file, Integer idUsuario) throws ResourceNotFoundException {
    	ResponseEntity<Usuario> oldUsuario = obterUsuarioId(idUsuario);
		Usuario u = oldUsuario.getBody();
        String foto = "";
		try {
			if(file != null) {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
				if (image == null) {
					throw new IllegalArgumentException("O arquivo enviado não é uma imagem válida.");
				} else {
					if(localSalvarImagem == "firebase") {
//						firebaseService.deleteImageByLink(u.getFoto());
						byte[] imageData = file.getBytes();
						URL imageUrl = new URL(u.getFoto());
						String encodedFileName = java.net.URLDecoder.decode(imageUrl.getPath(), StandardCharsets.UTF_8);
						String fileName = encodedFileName.substring(encodedFileName.lastIndexOf('/') + 1);
//						fileName = UUID.randomUUID().toString();
						foto = firebaseService.saveImage(imageData, fileName);					
					} else if(localSalvarImagem == "servidor") {
						String filename = file.getOriginalFilename();
						Path destPath = Paths.get("src", "main", "java", "io", "github", "mbrito", "ponto", "imagens");
						String dest = destPath.toAbsolutePath().toString();
						Path destination = Paths.get(dest, filename);
						Files.write(destination, file.getBytes());
						foto = destination.toString();
					}
					u.setFoto(foto);
					Usuario usuarioSalvo = repository.save(u);
					UsuarioDTO usuarioDTO = new UsuarioDTO(u.getId(), u.getNome(), u.getEmail(), u.getTipo(), u.getFoto(), u.getGrupoHorario(), u.getDiaFechamentoPonto(), u.getUltimaDataAprovada(), u.getDataCriacao());
					URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/" + usuarioSalvo.getId()).build().toUri();
					return ResponseEntity.status(HttpStatus.OK).location(uri).body(usuarioDTO);
				}
			}
			return null;
	    } catch (IOException e) {
	        throw new RuntimeException("Erro ao editar o arquivo de imagem.", e);
	    }
    }
    
    @Transactional
    public ResponseEntity<UsuarioDTO> editarUsuarioParcial(Usuario novoUsuario, Integer idGrupoHorario, Integer idUsuario) throws Exception {
		ResponseEntity<Usuario> oldUsuario = obterUsuarioId(idUsuario);
		Usuario u = oldUsuario.getBody();
		if(novoUsuario != null) {
			u.setNome(novoUsuario.getNome() != null ? novoUsuario.getNome() : u.getNome());			
			u.setDiaFechamentoPonto(novoUsuario.getDiaFechamentoPonto() != null ? novoUsuario.getDiaFechamentoPonto() : u.getDiaFechamentoPonto());			
			u.setUltimaDataAprovada(novoUsuario.getUltimaDataAprovada() != null ? novoUsuario.getUltimaDataAprovada() : u.getUltimaDataAprovada());			
		}
		if(idGrupoHorario != null) {
			GrupoHorario grupoHorario = grupoHorarioService.buscarGrupoHorarioId(idGrupoHorario, idUsuario).getBody();
			if(grupoHorario.getHorarios().size() < 2) {
				throw new Exception("Grupo Horario não tem horarios suficiente!");
			} else {
				u.setGrupoHorario(grupoHorario);
			}			
		}
		repository.save(u);
		UsuarioDTO usuarioDTO = new UsuarioDTO(u.getId(), u.getNome(), u.getEmail(), u.getTipo(), u.getFoto(), u.getGrupoHorario(), u.getDiaFechamentoPonto(), u.getUltimaDataAprovada(), u.getDataCriacao());
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.status(HttpStatus.OK).location(uri).body(usuarioDTO);
	}
    
    public ResponseEntity<Usuario> obterUsuarioId(Integer id) throws ResourceNotFoundException {
		Optional<Usuario> usuarios= repository.findById(id); 
		if(usuarios.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(usuarios.get());
		} else {
			throw new ResourceNotFoundException("Usuarios", "Id", id.toString());
		}
	}
    
    public ResponseEntity<UsuarioDTO> obterPerfilId(Integer id) throws ResourceNotFoundException {
    	Optional<Usuario> usuarios= repository.findById(id); 
    	if(usuarios.isPresent()) {
    		UsuarioDTO usuarioDTO = new UsuarioDTO(usuarios.get().getId(), usuarios.get().getNome(), usuarios.get().getEmail(), usuarios.get().getTipo(), usuarios.get().getFoto(), usuarios.get().getGrupoHorario(), usuarios.get().getDiaFechamentoPonto(), usuarios.get().getUltimaDataAprovada(), usuarios.get().getDataCriacao());
    		return ResponseEntity.status(HttpStatus.OK).body(usuarioDTO);
    	} else {
    		throw new ResourceNotFoundException("Usuarios", "Id", id.toString());
    	}
    }
    
    public ResponseEntity<Page<UsuarioDTO>> obterPerfis(Integer id, int pagina, int maxPage) throws ResourceNotFoundException {
        Optional<Usuario> usuarioRequisicao = repository.findById(id);
        List<String> permissoes = new ArrayList<>(List.of("GERENTE", "RH", "ADM"));
        
        if (usuarioRequisicao.isPresent()) {
            if (permissoes.contains(usuarioRequisicao.get().getTipo())) {
                if (maxPage > 50) {
                    maxPage = 50;
                }
                
                Page<Usuario> page = repository.obterTodosUsuariosPageable(PageRequest.of(pagina, maxPage));
                Page<UsuarioDTO> usuariosDTO = page.map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTipo(),
                        usuario.getFoto(),
                        usuario.getGrupoHorario(),
                        usuario.getDiaFechamentoPonto(),
                        usuario.getUltimaDataAprovada(),
                        usuario.getDataCriacao()
                ));
                
                return ResponseEntity.status(HttpStatus.OK).body(usuariosDTO);
            } else {
                throw new PermissionDeniedException();
            }
        } else {
            throw new ResourceNotFoundException("Usuarios", "Id", id.toString());
        }
    }
    
    public ResponseEntity<Page<UsuarioDTO>> obterPerfisPesquisa(Integer id, String pesquisa, int pagina, int maxPage) throws ResourceNotFoundException {
    	Optional<Usuario> usuarioRequisicao = repository.findById(id);
    	List<String> permissoes = new ArrayList<>(List.of("GERENTE", "RH", "ADM"));
    	
    	if (usuarioRequisicao.isPresent()) {
    		if (permissoes.contains(usuarioRequisicao.get().getTipo())) {
    			if (maxPage > 50) {
    				maxPage = 50;
    			}
    			
    			Page<Usuario> page = repository.obterTodosUsuariosNomePageable(pesquisa, PageRequest.of(pagina, maxPage));
    			Page<UsuarioDTO> usuariosDTO = page.map(usuario -> new UsuarioDTO(
    					usuario.getId(),
    					usuario.getNome(),
    					usuario.getEmail(),
    					usuario.getTipo(),
    					usuario.getFoto(),
    					usuario.getGrupoHorario(),
    					usuario.getDiaFechamentoPonto(),
    					usuario.getUltimaDataAprovada(),
    					usuario.getDataCriacao()
    					));
    			
    			return ResponseEntity.status(HttpStatus.OK).body(usuariosDTO);
    		} else {
    			throw new PermissionDeniedException();
    		}
    	} else {
    		throw new ResourceNotFoundException("Usuarios", "Id", id.toString());
    	}
    }
    
    public ResponseEntity<Usuario> obterUsuarioEmail(String email) throws ResourceNotFoundException {
		Optional<Usuario> usuarios= repository.findByEmail(email); 
		if(usuarios.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(usuarios.get());
		} else {
			throw new ResourceNotFoundException("Usuarios", "Email", email);
		}
	}
    
    private boolean existeUsuarioComEmail(String email) {
    	boolean existe = repository.findByEmail(email).isEmpty();
    	return !existe;
    }

    public ResponseEntity<TokenDTO> autenticar(Usuario usuario) throws ResourceNotFoundException{
        UserDetails user = loadUserByUsername(usuario.getEmail());
        boolean senhasBatem = encoder.matches(usuario.getSenha(), user.getPassword() );
        if(senhasBatem){
        	try{
                String token = jwtService.gerarToken(usuario);
                Optional<Usuario> usuarioCompleto = repository.findByEmail(usuario.getEmail());
                TokenDTO tokenDto = new TokenDTO(usuarioCompleto.get().getEmail(), token, usuarioCompleto.get().getTipo());
                return ResponseEntity.status(HttpStatus.OK).body(tokenDto);
            } catch (UsernameNotFoundException | SenhaInvalidaException e ){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }

        throw new SenhaInvalidaException();
    }
    
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        String[] roles = usuario.getTipo() == "GERENTE" ?
                new String[]{"GERENTE", "USER"} : new String[]{"USER"};

        return User
                .builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

}
