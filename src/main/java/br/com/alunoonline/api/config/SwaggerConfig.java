package br.com.alunoonline.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  
  /*
  Vai ficar disponível a doc swagger na url: http://localhost:8080/swagger-ui/index.html
  */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Aluno Online API")
            .version("1.0")
            .description("API REST para gerenciamento de alunos online")
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")));
  }
}