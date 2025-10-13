package br.com.alunoonline.api.service;

import br.com.alunoonline.api.dtos.LoginRequestDTO;
import br.com.alunoonline.api.dtos.LoginResponseDTO;
import br.com.alunoonline.api.enums.TipoUsuario;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.repository.AlunoRepository;
import br.com.alunoonline.api.repository.ProfessorRepository;
import br.com.alunoonline.api.security.JwtService;
import br.com.alunoonline.api.security.UserDetailsImpl;
import br.com.alunoonline.api.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  
  @Autowired
  private AuthenticationManager authenticationManager;
  
  @Autowired
  private JwtService jwtService;
  
  @Autowired
  private UserDetailsServiceImpl userDetailsService;
  
  @Autowired
  private AlunoRepository alunoRepository;
  
  @Autowired
  private ProfessorRepository professorRepository;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  public LoginResponseDTO login(LoginRequestDTO loginRequest) {
    // Carrega o usuário específico pelo tipo
    UserDetails userDetails = userDetailsService.loadUserByUsernameAndType(
        loginRequest.getEmail(),
        loginRequest.getTipoUsuario()
    );
    
    // Autentica o usuário
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getSenha()
        )
    );
    
    // Gera o token JWT
    String token = jwtService.generateToken(userDetails);
    
    // Monta a resposta
    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
    return new LoginResponseDTO(
        token,
        userDetailsImpl.getId(),
        userDetailsImpl.getNome(),
        userDetailsImpl.getEmail(),
        userDetailsImpl.getTipoUsuario()
    );
  }
  
  public LoginResponseDTO registrarAluno(Aluno aluno) {
    // Verifica se o email já existe
    if (alunoRepository.findByEmail(aluno.getEmail()).isPresent()) {
      throw new RuntimeException("Email já cadastrado");
    }
    
    // Criptografa a senha
    aluno.setSenha(passwordEncoder.encode(aluno.getSenha()));
    
    // Salva o aluno
    Aluno alunoSalvo = alunoRepository.save(aluno);
    
    // Cria o UserDetails
    UserDetailsImpl userDetails = new UserDetailsImpl(
        alunoSalvo.getId(),
        alunoSalvo.getNome(),
        alunoSalvo.getEmail(),
        alunoSalvo.getSenha(),
        TipoUsuario.ALUNO
    );
    
    // Gera o token
    String token = jwtService.generateToken(userDetails);
    
    return new LoginResponseDTO(
        token,
        alunoSalvo.getId(),
        alunoSalvo.getNome(),
        alunoSalvo.getEmail(),
        TipoUsuario.ALUNO
    );
  }
  
  public LoginResponseDTO registrarProfessor(Professor professor) {
    // Verifica se o email já existe
    if (professorRepository.findByEmail(professor.getEmail()).isPresent()) {
      throw new RuntimeException("Email já cadastrado");
    }
    
    // Criptografa a senha
    professor.setSenha(passwordEncoder.encode(professor.getSenha()));
    
    // Salva o professor
    Professor professorSalvo = professorRepository.save(professor);
    
    // Cria o UserDetails
    UserDetailsImpl userDetails = new UserDetailsImpl(
        professorSalvo.getId(),
        professorSalvo.getNome(),
        professorSalvo.getEmail(),
        professorSalvo.getSenha(),
        TipoUsuario.PROFESSOR
    );
    
    // Gera o token
    String token = jwtService.generateToken(userDetails);
    
    return new LoginResponseDTO(
        token,
        professorSalvo.getId(),
        professorSalvo.getNome(),
        professorSalvo.getEmail(),
        TipoUsuario.PROFESSOR
    );
  }
}

