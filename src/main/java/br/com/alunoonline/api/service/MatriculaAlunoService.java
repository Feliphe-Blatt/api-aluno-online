package br.com.alunoonline.api.service;

import br.com.alunoonline.api.dtos.AtualizarNotasRequestDTO;
import br.com.alunoonline.api.dtos.DisciplinasAlunoResponseDTO;
import br.com.alunoonline.api.dtos.HistoricoAlunoResponseDTO;
import br.com.alunoonline.api.enums.MatriculaAlunoStatusEnum;
import br.com.alunoonline.api.model.MatriculaAluno;
import br.com.alunoonline.api.repository.MatriculaAlunoRepository;
import br.com.alunoonline.api.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MatriculaAlunoService {
  
  private static final Double MEDIA_APROVACAO = 7.0;
  
  private static final Integer QUANTIDADE_NOTAS = 2;
  
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
  
  public void atualizarNotas(Long matriculaAlunoId, AtualizarNotasRequestDTO atualizarNotasRequestDTO) {
   
    MatriculaAluno matriculaAluno = buscarMatriculaOuFalhar(matriculaAlunoId);
    
    if (atualizarNotasRequestDTO.getNota1() != null) {
      matriculaAluno.setNota1(atualizarNotasRequestDTO.getNota1());
    }
    if (atualizarNotasRequestDTO.getNota2() != null) {
      matriculaAluno.setNota2(atualizarNotasRequestDTO.getNota2());
    }
    
    calcularMediaEModificarStatus(matriculaAluno);
    matriculaAlunoRepository.save(matriculaAluno);
  }
  
  public HistoricoAlunoResponseDTO emitirHistoricoAluno(Long alunoId){
    
    List<MatriculaAluno> matriculasAluno = matriculaAlunoRepository.findByAlunoId(alunoId);
    
    if (matriculasAluno.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma matrícula encontrada para o aluno com ID: " + alunoId);
    }
    
    HistoricoAlunoResponseDTO historicoAluno = new HistoricoAlunoResponseDTO();
    
    historicoAluno.setNomeAluno(matriculasAluno.get(0).getAluno().getNome());
    historicoAluno.setEmailAluno(matriculasAluno.get(0).getAluno().getEmail());
    historicoAluno.setCpfAluno(matriculasAluno.get(0).getAluno().getCpf());
    
    List<DisciplinasAlunoResponseDTO> disciplinas = matriculasAluno.stream()
        .map(this::mapearParaDisciplinasAlunoResponseDTO)
        .toList();
    
    historicoAluno.setDisciplinas(disciplinas);
    
    return historicoAluno;
  }
  
  private MatriculaAluno buscarMatriculaOuFalhar(Long matriculaAlunoId) {
    return matriculaAlunoRepository.findById(matriculaAlunoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada com o ID: " + matriculaAlunoId));
  }
  
  private Double calcularMedia(Double nota1, Double nota2) {
    
    return (nota1 != null && nota2 != null) ? (nota1 + nota2) / QUANTIDADE_NOTAS : null;
  }
  
  private void calcularMediaEModificarStatus(MatriculaAluno matriculaAluno) {
    
      Double media = calcularMedia(matriculaAluno.getNota1(), matriculaAluno.getNota2());
      
      if (media != null) {
          matriculaAluno.setStatus(media >= MEDIA_APROVACAO ? MatriculaAlunoStatusEnum.APROVADO : MatriculaAlunoStatusEnum.REPROVADO);
      }
  }
  
  private DisciplinasAlunoResponseDTO mapearParaDisciplinasAlunoResponseDTO(MatriculaAluno matriculaAluno) {
    
    DisciplinasAlunoResponseDTO response = new DisciplinasAlunoResponseDTO();
    response.setNomeDisciplina(matriculaAluno.getDisciplina().getNome());
    response.setNomeProfessor(matriculaAluno.getDisciplina().getProfessor().getNome());
    response.setNota1(matriculaAluno.getNota1());
    response.setNota2(matriculaAluno.getNota2());
    response.setMedia(calcularMedia(matriculaAluno.getNota1(), matriculaAluno.getNota2()));
    response.setStatusMatriculaAluno(matriculaAluno.getStatus());
    
    return response;
  }
}
