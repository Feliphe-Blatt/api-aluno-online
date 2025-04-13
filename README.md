# api-aluno-online
 
# API de Gerenciamento de Alunos

Este projeto implementa uma API REST para gerenciamento de alunos utilizando Spring Boot.

## Tecnologias Utilizadas

- Java
- Spring Boot
- Spring Data JPA
- Maven
- Lombok
- Banco de Dados Relacional (JPA/Hibernate)

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas:

- **Model**: Entidades JPA que representam as tabelas do banco de dados
- **Repository**: Interfaces que estendem JpaRepository para operações de persistência
- **Service**: Classes que implementam a lógica de negócio
- **Controller**: Endpoints REST que recebem as requisições HTTP

## Funcionalidades

- Cadastro de alunos

## Endpoints

### Alunos

- **POST** `/alunos`: Cria um novo aluno
  - Body:
    ```json
    {
      "nome": "Nome do Aluno",
      "cpf": "12345678900",
      "email": "aluno@email.com"
    }
    ```

## Como Executar

1. Clone o repositório
2. Configure o banco de dados no arquivo `application.properties` localizado em `resources`
3. Execute o comando: `mvn spring-boot:run`

## Pré-requisitos

- Java 17+
- Maven 3.6+

# Exemplos de Uso

## Testando a API com Insomnia

### Criação de Aluno (POST)
![Insomnia - Criação de Aluno](https://exemplo.com/caminho/para/imagem-insomnia-post.png)

Nesta imagem, podemos ver a requisição POST para `/alunos` com o seguinte corpo:
```json
{
  "nome": "Maria Silva",
  "cpf": "12345678900",
  "email": "maria@email.com"
}
```

O servidor retorna o status HTTP 201 (Created), confirmando o sucesso da operação.

## Visualização no Banco de Dados (DBeaver)

### Tabela de Alunos
![DBeaver - Tabela de Alunos](https://exemplo.com/caminho/para/imagem-dbeaver-tabela.png)

Nesta captura, podemos ver a tabela `aluno` no banco de dados contendo os registros inseridos através da API. Os campos `id`, `nome`, `cpf` e `email` são exibidos com os valores correspondentes.

### Consulta SQL
![DBeaver - Consulta SQL](https://exemplo.com/caminho/para/imagem-dbeaver-consulta.png)

Exemplo de uma consulta SQL executada no DBeaver mostrando os dados persistidos no banco:
```sql
SELECT * FROM aluno;
```

## Observações

- As capturas de tela demonstram o fluxo completo desde a requisição HTTP até o armazenamento no banco de dados
- A estrutura da tabela reflete exatamente o modelo definido na classe `Aluno.java`
- O status 201 confirma que o recurso foi criado com sucesso

## Próximos Passos

- Implementação de operações de busca, atualização e exclusão de alunos

## Autor

Feliphe-Blatt
