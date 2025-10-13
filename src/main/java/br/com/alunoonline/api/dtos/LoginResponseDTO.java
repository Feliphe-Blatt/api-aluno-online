package br.com.alunoonline.api.dtos;

import br.com.alunoonline.api.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
  
  private String token;
  private String tipo = "Bearer";
  private Long id;
  private String nome;
  private String email;
  private TipoUsuario tipoUsuario;
  
  public LoginResponseDTO(String token, Long id, String nome, String email, TipoUsuario tipoUsuario) {
    this.token = token;
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.tipoUsuario = tipoUsuario;
  }
}

