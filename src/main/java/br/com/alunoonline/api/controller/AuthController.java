package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.LoginRequestDTO;
import br.com.alunoonline.api.dtos.LoginResponseDTO;
import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Professor;
import br.com.alunoonline.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação e registro")
public class AuthController {
  
  @Autowired
  private AuthService authService;
  
  @PostMapping("/login")
  @Operation(summary = "Login de usuário", description = "Autentica um aluno ou professor e retorna um token JWT")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
    try {
      LoginResponseDTO response = authService.login(loginRequest);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, String> error = new HashMap<>();
      error.put("erro", "Credenciais inválidas");
      error.put("mensagem", e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
  }
  
  @PostMapping("/registro/aluno")
  @Operation(summary = "Registrar novo aluno", description = "Cria uma nova conta de aluno")
  public ResponseEntity<?> registrarAluno(@Valid @RequestBody Aluno aluno) {
    try {
      LoginResponseDTO response = authService.registrarAluno(aluno);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
      Map<String, String> error = new HashMap<>();
      error.put("erro", "Erro ao registrar aluno");
      error.put("mensagem", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
  }
  
  @PostMapping("/registro/professor")
  @Operation(summary = "Registrar novo professor", description = "Cria uma nova conta de professor")
  public ResponseEntity<?> registrarProfessor(@Valid @RequestBody Professor professor) {
    try {
      LoginResponseDTO response = authService.registrarProfessor(professor);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
      Map<String, String> error = new HashMap<>();
      error.put("erro", "Erro ao registrar professor");
      error.put("mensagem", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
  }
}
