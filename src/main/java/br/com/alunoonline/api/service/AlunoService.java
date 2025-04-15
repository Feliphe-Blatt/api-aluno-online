package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.relation.RelationServiceNotRegisteredException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {
  
  @Autowired
  AlunoRepository alunoRepository;
  
  public void criarAluno(Aluno aluno) {
    alunoRepository.save(aluno);
  }

  public List<Aluno> listarTodosAlunos() {
    return alunoRepository.findAll();
  }

  public Optional<Aluno> buscarAlunoPorId(Long id) {
    return alunoRepository.findById(id);
  }

  public void deletarAlunoPorId(Long id) {
    alunoRepository.deleteById(id);
  }

  public void atualizarAlunoPorId(Long id, Aluno aluno) {

    Optional<Aluno> alunoExistente = alunoRepository.findById(id);

    if (alunoExistente.isEmpty()) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              "Aluno não encontrado com o ID: " + id
      );
    }

    Aluno alunoAtualizado = alunoExistente.get();

    alunoAtualizado.setNome(aluno.getNome());
    alunoAtualizado.setCpf(aluno.getCpf());
    alunoAtualizado.setEmail(aluno.getEmail());

    alunoRepository.save(alunoAtualizado);
  }
}
