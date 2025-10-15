package br.com.alunoonline.api.config;

import br.com.alunoonline.api.security.JwtAuthenticationFilter;
import br.com.alunoonline.api.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/auth/**").permitAll()
                // Swagger/OpenAPI endpoints
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/configuration/**",
                    "/webjars/**"
                ).permitAll()
                
                // Endpoints apenas para ALUNOS
                .requestMatchers(HttpMethod.GET, "/alunos/{id}").hasAnyRole("ALUNO", "PROFESSOR")
                .requestMatchers(HttpMethod.PUT, "/alunos/{id}").hasRole("ALUNO")
                
                // Endpoints apenas para PROFESSORES
                .requestMatchers("/professores/**").hasRole("PROFESSOR")
                .requestMatchers("/disciplinas/**").hasRole("PROFESSOR")
                .requestMatchers(HttpMethod.POST, "/matriculas").hasRole("PROFESSOR")
                .requestMatchers(HttpMethod.PATCH, "/matriculas/trancar/**").hasRole("PROFESSOR")
                .requestMatchers(HttpMethod.PATCH, "/matriculas/atualizar-notas/**").hasRole("PROFESSOR")
                
                // Endpoints que aluno pode acessar seus próprios dados
                .requestMatchers(HttpMethod.GET, "/matriculas/historico-aluno/**").hasRole("ALUNO")
                
                // Outros endpoints requerem autenticação
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
