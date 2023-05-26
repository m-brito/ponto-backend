package io.github.mbrito.ponto.security.jwt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.mbrito.ponto.casoDeUso.usuario.entitie.Usuario;
import io.github.mbrito.ponto.casoDeUso.usuario.repository.UsuarioRepository;
import io.github.mbrito.ponto.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
	@Value("${security.jwt.expiracao}")
    private String expiracao;

    @Value("${security.jwt.chave-assinatura}")
    private String chaveAssinatura;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public String gerarToken( Usuario usuario ) throws ResourceNotFoundException{
        long expString = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(expString);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);

        HashMap<String, Object> claims = new HashMap<>();
        Optional<Usuario> users = usuarioRepository.findByEmail(usuario.getEmail());
        if(users.isPresent()) {
        	claims.put("idUsuario", users.get().getId());
		} else {
			throw new ResourceNotFoundException("Usuario", "Email", usuario.getEmail());
		}
        
        return Jwts
                .builder()
                .setSubject(usuario.getEmail())
                .setExpiration(data)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, chaveAssinatura)
                .compact();
    }

    private Claims obterClaims( String token ) throws ExpiredJwtException {
        return Jwts
                 .parser()
                 .setSigningKey(chaveAssinatura)
                 .parseClaimsJws(token)
                 .getBody();
    }

    public boolean tokenValido( String token ){
        try{
            Claims claims = obterClaims(token);
            Date dataExpiracao = claims.getExpiration();
            LocalDateTime data =
                    dataExpiracao.toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(data);
        }catch (Exception e){
            return false;
        }
    }
    
    public Long obterIdUsuario(String token) throws ExpiredJwtException{
        return obterClaims(token).get("idUsuario", Long.class);
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException{
        return (String) obterClaims(token).getSubject();
    }
}
