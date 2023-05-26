package io.github.mbrito.ponto.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.mbrito.ponto.casoDeUso.usuario.service.UsuarioService;
import io.github.mbrito.ponto.security.jwt.JwtAuthFilter;
import io.github.mbrito.ponto.security.jwt.JwtService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter(){
        return new JwtAuthFilter(jwtService, usuarioService);
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(usuarioService)
            .passwordEncoder(passwordEncoder());
    }
    
    @Bean
    protected SecurityFilterChain configure( HttpSecurity http ) throws Exception {
        http
        	.cors().and().csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/usuario/**")
                	.permitAll()
            	.requestMatchers(HttpMethod.GET, "/api/usuario/**")
                	.hasAnyRole("USER", "GESTOR")
                .requestMatchers(HttpMethod.PATCH, "/api/usuario/**")
                	.hasAnyRole("USER", "GESTOR")
            	.requestMatchers(HttpMethod.PUT, "/api/usuario/**")
                	.hasAnyRole("USER", "GESTOR")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore( jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/webjars/**");
    }
    
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS", "PATCH"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}