# API Aluno Online

## Introdução

Esta API gerencia informações de alunos, professores e disciplinas para uma instituição de ensino. Ela oferece operações CRUD completas para todas as entidades.

## Funcionalidades

> Os prints de todas as funcionalidades estão [AQUI](./docs)

### Aluno

O sistema permite as seguintes operações para a entidade Aluno:

- **Criar Aluno**: Cadastro de novos alunos no sistema
- **Listar Alunos**: Visualização de todos os alunos cadastrados
- **Buscar Aluno**: Consulta de detalhes de um aluno específico pelo ID
- **Atualizar Aluno**: Atualização de informações de um aluno existente
- **Remover Aluno**: Exclusão de um aluno pelo seu ID

### Professor

O sistema permite as seguintes operações para a entidade Professor:

- **Criar Professor**: Cadastro de novos professores no sistema
- **Listar Professores**: Visualização de todos os professores cadastrados
- **Buscar Professor**: Consulta de detalhes de um professor específico pelo ID
- **Atualizar Professor**: Atualização de informações de um professor existente
- **Remover Professor**: Exclusão de um professor pelo seu ID

### Disciplina

O sistema permite as seguintes operações para a entidade Disciplina:

- **Criar Disciplina**: Cadastro de novas disciplinas no sistema com nome e código
- **Listar Disciplinas**: Visualização de todas as disciplinas cadastradas
- **Buscar Disciplina**: Consulta de detalhes de uma disciplina específica pelo ID
- **Atualizar Disciplina**: Atualização de informações de uma disciplina existente
- **Remover Disciplina**: Exclusão de uma disciplina pelo seu ID

## API Endpoints

### Alunos

- **POST** `/alunos`: Cria um novo aluno
  - Body:
  ```json
  {
    "nome": "João Silva",
    "email": "joao.silva@email.com"
  }
  ```
  - Response: Status 201 (Created)

- **GET** `/alunos`: Lista todos os alunos cadastrados
  - Response: Status 200 (OK)
  ```json
  [
    {
      "id": 1,
      "nome": "João Silva",
      "email": "joao.silva@email.com"
    }
  ]
  ```

- **GET** `/alunos/{id}`: Busca um aluno pelo ID
  - Response: Status 200 (OK)
  ```json
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao.silva@email.com"
  }
  ```

- **PUT** `/alunos/{id}`: Atualiza um aluno pelo ID
  - Body:
  ```json
  {
    "nome": "João Silva Santos",
    "email": "joao.santos@email.com"
  }
  ```
  - Response: Status 204 (No Content)

- **DELETE** `/alunos/{id}`: Remove um aluno pelo ID
  - Response: Status 204 (No Content)

### Professores

- **POST** `/professores`: Cria um novo professor
  - Body:
  ```json
  {
    "nome": "Maria Oliveira",
    "email": "maria.oliveira@email.com"
  }
  ```
  - Response: Status 201 (Created)

- **GET** `/professores`: Lista todos os professores cadastrados
  - Response: Status 200 (OK)
  ```json
  [
    {
      "id": 1,
      "nome": "Maria Oliveira",
      "email": "maria.oliveira@email.com"
    }
  ]
  ```

- **GET** `/professores/{id}`: Busca um professor pelo ID
  - Response: Status 200 (OK)
  ```json
  {
    "id": 1,
    "nome": "Maria Oliveira",
    "email": "maria.oliveira@email.com"
  }
  ```

- **PUT** `/professores/{id}`: Atualiza um professor pelo ID
  - Body:
  ```json
  {
    "nome": "Maria Oliveira Santos",
    "email": "maria.santos@email.com"
  }
  ```
  - Response: Status 204 (No Content)

- **DELETE** `/professores/{id}`: Remove um professor pelo ID
  - Response: Status 204 (No Content)

### Disciplinas

- **POST** `/disciplinas`: Cria uma nova disciplina
  - Body:
  ```json
  {
    "nome": "Matemática",
    "codigo": "MAT101"
  }
  ```
  - Response: Status 201 (Created)

- **GET** `/disciplinas`: Lista todas as disciplinas cadastradas
  - Response: Status 200 (OK)
  ```json
  [
    {
      "id": 1,
      "nome": "Matemática",
      "codigo": "MAT101"
    }
  ]
  ```

- **GET** `/disciplinas/{id}`: Busca uma disciplina pelo ID
  - Response: Status 200 (OK)
  ```json
  {
    "id": 1,
    "nome": "Matemática",
    "codigo": "MAT101"
  }
  ```

- **PUT** `/disciplinas/{id}`: Atualiza uma disciplina pelo ID
  - Body:
  ```json
  {
    "nome": "Matemática Avançada",
    "codigo": "MAT201"
  }
  ```
  - Response: Status 204 (No Content)

- **DELETE** `/disciplinas/{id}`: Remove uma disciplina pelo ID
  - Response: Status 204 (No Content)

## Tecnologias utilizadas

- Spring Boot
- Spring Data JPA
- H2 Database (ambiente de desenvolvimento)
- Maven

## Como executar

1. Clone o repositório
2. Execute `mvn spring-boot:run`
3. A API estará disponível em `http://localhost:8080`
