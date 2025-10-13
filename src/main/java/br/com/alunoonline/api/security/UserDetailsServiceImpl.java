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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  
  @Autowired
  private AlunoRepository alunoRepository;
  
  @Autowired
  private ProfessorRepository professorRepository;
  
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
    
    if (tipoUsuario == TipoUsuario.ALUNO) {
      Aluno aluno = alunoRepository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("Aluno não encontrado"));
      return new UserDetailsImpl(
          aluno.getId(),
          aluno.getNome(),
          aluno.getEmail(),
          aluno.getSenha(),
          TipoUsuario.ALUNO
      );
    } else {
      Professor professor = professorRepository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException("Professor não encontrado"));
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

