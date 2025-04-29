package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Disciplina;
import br.com.alunoonline.api.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplinaService {
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    public void criarDisciplina(Disciplina disciplina) {
        disciplinaRepository.save(disciplina);
    }
    
    public List<Disciplina> listarTodasDisciplinas() {
        return disciplinaRepository.findAll();
    }
    
    public Optional<Disciplina> buscarDisciplinaPorId(Long id) {
        return disciplinaRepository.findById(id);
    }
    
    public void deletarDisciplinaPorId(Long id) {
        disciplinaRepository.deleteById(id);
    }
    
    public void atualizarDisciplinaPorId(Long id, Disciplina disciplina) {
        Optional<Disciplina> disciplinaExistente = disciplinaRepository.findById(id);
        
        if (disciplinaExistente.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Disciplina não encontrada com o ID: " + id
            );
        }
        
        Disciplina disciplinaAtualizada = disciplinaExistente.get();
        
        disciplinaAtualizada.setNome(disciplina.getNome());
        disciplinaAtualizada.setCargaHoraria(disciplina.getCargaHoraria());
        disciplinaAtualizada.setProfessor(disciplina.getProfessor());
        
        disciplinaRepository.save(disciplinaAtualizada);
    }
}