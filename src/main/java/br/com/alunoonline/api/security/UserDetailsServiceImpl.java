package br.com.alunoonline.api.security;

import br.com.alunoonline.api.enums.TipoUsuario;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.repository.AlunoRepository;
import br.com.alunoonline.api.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  
  @Autowired
  private AlunoRepository alunoRepository;
  
  @Autowired
  private ProfessorRepository professorRepository;
  
  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Tenta encontrar como aluno
    Aluno aluno = alunoRepository.findByEmail(username).orElse(null);
    if (aluno != null) {
      return new UserDetailsImpl(
          aluno.getId(),
          aluno.getNome(),
          aluno.getEmail(),
          aluno.getSenha(),
          TipoUsuario.ALUNO
      );
    }
    
    // Tenta encontrar como professor
    Professor professor = professorRepository.findByEmail(username).orElse(null);
    if (professor != null) {
      return new UserDetailsImpl(
          professor.getId(),
          professor.getNome(),
          professor.getEmail(),
          professor.getSenha(),
          TipoUsuario.PROFESSOR
      );
    }
    
    throw new UsernameNotFoundException("Usuário não encontrado: " + username);
  }
  
  public UserDetails loadUserByUsernameAndType(String username, TipoUsuario tipoUsuario)
      throws UsernameNotFoundException {
    logger.info("Buscando usuário: email={}, tipoUsuario={}", username, tipoUsuario);
    if (tipoUsuario == TipoUsuario.ALUNO) {
      Aluno aluno = alunoRepository.findByEmail(username)
          .orElseThrow(() -> {
            logger.warn("Aluno não encontrado: {}", username);
            return new UsernameNotFoundException("Aluno não encontrado");
          });
      logger.info("Aluno encontrado: {}", aluno.getEmail());
      return new UserDetailsImpl(
          aluno.getId(),
          aluno.getNome(),
          aluno.getEmail(),
          aluno.getSenha(),
          TipoUsuario.ALUNO
      );
    } else {
      Professor professor = professorRepository.findByEmail(username)
          .orElseThrow(() -> {
            logger.warn("Professor não encontrado: {}", username);
            return new UsernameNotFoundException("Professor não encontrado");
          });
      logger.info("Professor encontrado: {}", professor.getEmail());
      return new UserDetailsImpl(
          professor.getId(),
          professor.getNome(),
          professor.getEmail(),
          professor.getSenha(),
          TipoUsuario.PROFESSOR
      );
    }
  }
}
