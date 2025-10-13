# Sistema de Autenticação - Aluno Online API

## 🔐 Autenticação JWT Implementada

O sistema agora possui autenticação JWT com dois tipos de usuários:
- **ALUNO**: Pode ver e editar seus próprios dados, ver seu histórico
- **PROFESSOR**: Pode gerenciar disciplinas, professores, matrículas e notas

## 📋 Endpoints de Autenticação

### 1. Registrar Aluno
```http
POST /auth/registro/aluno
Content-Type: application/json

{
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "senha": "senha123"
}
```

### 2. Registrar Professor
```http
POST /auth/registro/professor
Content-Type: application/json

{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@email.com",
  "senha": "senha123"
}
```

### 3. Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "senha123",
  "tipoUsuario": "ALUNO"
}
```

**Tipos de usuário**: `ALUNO` ou `PROFESSOR`

**Resposta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "tipoUsuario": "ALUNO"
}
```

## 🔑 Usando o Token

Após o login, use o token em todas as requisições protegidas:

```http
GET /alunos/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🛡️ Níveis de Acesso

### Endpoints Públicos (sem autenticação)
- `POST /auth/login`
- `POST /auth/registro/aluno`
- `POST /auth/registro/professor`
- `/swagger-ui/**`
- `/v3/api-docs/**`

### Endpoints para ALUNO
- `GET /alunos/{id}` - Ver próprio perfil
- `PUT /alunos/{id}` - Editar próprio perfil
- `GET /matriculas-aluno/historico/{id}` - Ver próprio histórico

### Endpoints para PROFESSOR
- Todos os endpoints de `/professores/**`
- Todos os endpoints de `/disciplinas/**`
- `POST /matriculas-aluno` - Criar matrícula
- `PATCH /matriculas-aluno/trancar/{id}` - Trancar matrícula
- `PATCH /matriculas-aluno/atualizar-notas/{id}` - Atualizar notas

### Endpoints para AMBOS
- `GET /alunos/{id}` - Professores podem ver alunos

## 🔧 Configuração

As configurações JWT estão em `application.properties`:

```properties
jwt.secret=minha-chave-secreta-super-segura-para-jwt-authentication-aluno-online-2024-minimum-256-bits
jwt.expiration=86400000  # 24 horas
```

## 🚀 Como Testar

### 1. Registrar um Aluno
```bash
curl -X POST http://localhost:8080/auth/registro/aluno \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "senha": "senha123"
  }'
```

### 2. Fazer Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "senha123",
    "tipoUsuario": "ALUNO"
  }'
```

### 3. Usar o Token
```bash
curl -X GET http://localhost:8080/alunos/1 \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## 📝 Notas Importantes

1. **Senhas são criptografadas** usando BCrypt antes de serem salvas
2. **Tokens expiram em 24 horas** (configurável)
3. **CPF e Email são únicos** no sistema
4. **O token contém**: id, nome, email e tipo de usuário
5. **Não é possível** um aluno acessar dados de outro aluno
6. **Professores têm acesso total** ao sistema

## 🔄 Fluxo de Autenticação

1. Usuário se registra ou faz login
2. Sistema valida credenciais
3. Sistema gera token JWT
4. Cliente armazena o token
5. Cliente envia token em cada requisição
6. Sistema valida token e permissões
7. Sistema processa a requisição se autorizado

## 🛠️ Tecnologias Utilizadas

- Spring Security
- JWT (JSON Web Token)
- BCrypt para criptografia de senhas
- JJWT library (io.jsonwebtoken)

