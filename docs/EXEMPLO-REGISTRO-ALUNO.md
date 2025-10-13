# Exemplo de Requisição para Registrar Aluno

## ✅ FORMATO CORRETO

```json
POST http://localhost:8080/auth/registro/aluno
Content-Type: application/json

{
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "senha": "senha123"
}
```

## ⚠️ VALIDAÇÕES IMPORTANTES

### Nome
- ✅ Obrigatório
- ✅ Não pode ser vazio

### CPF
- ✅ Obrigatório
- ✅ Deve ter EXATAMENTE 11 caracteres (sem pontos ou traços)
- ✅ Deve ser único (não pode já existir no banco)

### Email
- ✅ Obrigatório
- ✅ Deve ser um email válido (formato: exemplo@dominio.com)
- ✅ Deve ser único (não pode já existir no banco)

### Senha
- ✅ Obrigatória
- ✅ Deve ter no mínimo 6 caracteres

## ❌ ERROS COMUNS

### 1. CPF com formato errado
```json
// ❌ ERRADO - CPF com pontos e traço
{
  "cpf": "123.456.789-01"
}

// ✅ CORRETO - Apenas números
{
  "cpf": "12345678901"
}
```

### 2. Email inválido
```json
// ❌ ERRADO
{
  "email": "joao@email"  // Falta o .com
}

// ✅ CORRETO
{
  "email": "joao@email.com"
}
```

### 3. Senha muito curta
```json
// ❌ ERRADO
{
  "senha": "123"  // Menos de 6 caracteres
}

// ✅ CORRETO
{
  "senha": "123456"
}
```

### 4. Campos faltando
```json
// ❌ ERRADO - Faltando o campo "nome"
{
  "cpf": "12345678901",
  "email": "joao@email.com",
  "senha": "senha123"
}

// ✅ CORRETO - Todos os campos presentes
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "senha": "senha123"
}
```

## 📝 RESPOSTA DE ERRO

Se houver erro de validação, você receberá uma resposta assim:

```json
{
  "erro": "Erro de validação",
  "campos": {
    "cpf": "CPF deve ter 11 caracteres",
    "email": "Email inválido",
    "senha": "Senha deve ter no mínimo 6 caracteres"
  }
}
```

## ✅ RESPOSTA DE SUCESSO

Se tudo estiver correto, você receberá:

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

## 🧪 TESTE NO SWAGGER

1. Acesse: http://localhost:8080/swagger-ui/index.html
2. Procure por "Autenticação"
3. Clique em "POST /auth/registro/aluno"
4. Clique em "Try it out"
5. Cole o JSON de exemplo
6. Clique em "Execute"

## 🧪 TESTE NO POSTMAN/INSOMNIA

1. Método: POST
2. URL: http://localhost:8080/auth/registro/aluno
3. Headers: Content-Type: application/json
4. Body (raw JSON):
```json
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "email": "joao@email.com",
  "senha": "senha123"
}
```

