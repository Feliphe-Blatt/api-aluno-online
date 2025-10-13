package br.com.alunoonline.api.dtos;

import br.com.alunoonline.api.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
  
  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  private String email;
  
  @NotBlank(message = "Senha é obrigatória")
  private String senha;
  
  @NotNull(message = "Tipo de usuário é obrigatório")
  private TipoUsuario tipoUsuario;
}

