package br.com.alunoonline.api.service;

import br.com.alunoonline.api.enums.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import br.com.alunoonline.api.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MatriculaAlunoService {
  
  @Autowired
  MatriculaAlunoRepository matriculaAlunoRepository;
  
  @Autowired
  DisciplinaRepository disciplinaRepository;
  
  public void criarMatricula(MatriculaAluno matriculaAluno) {
    
    if (matriculaAluno.getNota1() != null && matriculaAluno.getNota2() != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é permitido fazer matrícula com nota preenchida. Por favor, deixe as notas em branco ao matricular.");
    }
    
    if (matriculaAluno.getDisciplina() == null || matriculaAluno.getDisciplina().getId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Disciplina não informada. Por favor, selecione uma disciplina válida.");
    }
    
    if (!disciplinaRepository.existsById(matriculaAluno.getDisciplina().getId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada. Por favor, selecione uma disciplina existente.");
    }
    
    matriculaAluno.setStatus(MatriculaAlunoStatusEnum.MATRICULADO);
    
    matriculaAlunoRepository.save(matriculaAluno);
  }
  
  public void trancarMatricula(Long id) {
    MatriculaAluno matriculaAluno = matriculaAlunoRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada com o ID: " + id));
    
    if (matriculaAluno.getStatus() != MatriculaAlunoStatusEnum.MATRICULADO) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A matrícula não pode ser trancada porque não está no status 'MATRICULADO'.");
    }
    
    matriculaAluno.setStatus(MatriculaAlunoStatusEnum.TRANCADO);
    matriculaAlunoRepository.save(matriculaAluno);
  }
  
}
