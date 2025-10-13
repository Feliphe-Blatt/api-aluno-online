# Como Usar Token JWT no Insomnia - Guia Completo

## 🚀 PASSO A PASSO NO INSOMNIA

### **ETAPA 1: Registrar/Fazer Login**

1. **Crie uma nova requisição** no Insomnia
   - Clique no **"+"** para criar uma nova requisição
   - Dê um nome: "Login Aluno"

2. **Configure a requisição de Login:**
   - **Método**: `POST`
   - **URL**: `http://localhost:8080/auth/login`
   - **Body**: Selecione `JSON`
   
3. **Cole este JSON no Body:**
   ```json
   {
     "email": "joao@email.com",
     "senha": "senha123",
     "tipoUsuario": "ALUNO"
   }
   ```

4. **Clique em "Send"**

5. **Você receberá uma resposta assim:**
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwibm9tZSI6IkpvXG8gU2lsdmEiLCJ0aXBvVXN1YXJpbyI6IkFMVU5PIiwic3ViIjoiam9hb0BlbWFpbC5jb20iLCJpYXQiOjE3MDk5MDEyMDAsImV4cCI6MTcwOTk4NzYwMH0.abc123xyz...",
     "tipo": "Bearer",
     "id": 1,
     "nome": "João Silva",
     "email": "joao@email.com",
     "tipoUsuario": "ALUNO"
   }
   ```

6. **📋 COPIE O TOKEN!** (todo o valor depois de "token": "...")

---

### **ETAPA 2: Usar o Token - MÉTODO 1 (Recomendado! 👍)**

#### **Usando a Aba "Auth"**

1. **Crie uma nova requisição** (ex: "Buscar Aluno")
   - **Método**: `GET`
   - **URL**: `http://localhost:8080/alunos/1`

2. **Clique na aba "Auth"** (ao lado de Body, Query, etc)

3. **No dropdown "AUTH"**, selecione: `Bearer Token`

4. **No campo "TOKEN"**, cole o token que você copiou:
   ```
   eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwibm9tZSI6IkpvXG8gU2lsdmEiLCJ0aXBvVXN1YXJpbyI6IkFMVU5PIiwic3ViIjoiam9hb0BlbWFpbC5jb20iLCJpYXQiOjE3MDk5MDEyMDAsImV4cCI6MTcwOTk4NzYwMH0.abc123xyz...
   ```
   **⚠️ IMPORTANTE:** Cole APENAS o token, SEM a palavra "Bearer"! O Insomnia adiciona automaticamente.

5. **Clique em "Send"** ✅

**Pronto!** O Insomnia enviará automaticamente o header: `Authorization: Bearer SEU_TOKEN`

---

### **ETAPA 2: Usar o Token - MÉTODO 2 (Manual)**

#### **Usando a Aba "Header"**

1. **Crie uma nova requisição**
   - **Método**: `GET`
   - **URL**: `http://localhost:8080/alunos/1`

2. **Clique na aba "Header"**

3. **Adicione um novo header:**
   - Clique em "Add"
   - **Name**: `Authorization`
   - **Value**: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwibm9tZSI6IkpvXG8...` (cole "Bearer " + espaço + seu token)

4. **Clique em "Send"** ✅

---

## 🎯 DICA PRO: Usar Variáveis de Ambiente

### **Passo 1: Criar Variável de Ambiente**

1. **Clique no ícone de engrenagem** ⚙️ (canto superior esquerdo)

2. Selecione **"Manage Environments"**

3. Clique em **"+"** para criar um novo ambiente ou edite "Base Environment"

4. **Adicione uma variável:**
   ```json
   {
     "base_url": "http://localhost:8080",
     "token": ""
   }
   ```

5. **Clique em "Done"**

### **Passo 2: Capturar o Token Automaticamente**

1. **Na requisição de Login**, vá para a aba "Tests" (lado direito, após enviar)

2. **NÃO** existe aba "Tests" no Insomnia? Use este método manual:
   - Após fazer login e receber o token
   - Copie o token da resposta
   - Vá em ⚙️ → Manage Environments
   - Cole o token no campo `"token": "COLE_AQUI"`

### **Passo 3: Usar a Variável nas Requisições**

1. **Configure a URL usando a variável:**
   ```
   {{ _.base_url }}/alunos/1
   ```

2. **Configure o Auth:**
   - Aba "Auth"
   - Type: "Bearer Token"
   - Token: `{{ _.token }}`

**Agora todas as requisições usam as variáveis!** 🎉

---

## 📝 EXEMPLO COMPLETO NO INSOMNIA

### **Requisição 1: Registrar Aluno**

```
POST http://localhost:8080/auth/registro/aluno
Content-Type: application/json

Body (JSON):
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@teste.com",
  "senha": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "id": 1,
  "nome": "Maria Santos",
  "email": "maria@teste.com",
  "tipoUsuario": "ALUNO"
}
```

📋 **COPIE O TOKEN DA RESPOSTA!**

---

### **Requisição 2: Buscar Dados do Aluno (COM TOKEN)**

```
GET http://localhost:8080/alunos/1

Auth:
├─ Type: Bearer Token
└─ Token: eyJhbGciOiJIUzI1NiJ9... (cole aqui)
```

**OU manualmente no Header:**
```
Header:
├─ Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### **Requisição 3: Ver Histórico do Aluno (COM TOKEN)**

```
GET http://localhost:8080/matriculas-aluno/historico/1

Auth:
├─ Type: Bearer Token
└─ Token: eyJhbGciOiJIUzI1NiJ9... (mesmo token)
```

---

## ✅ CHECKLIST - Está Funcionando?

Marque cada item:

- [ ] Fiz login e recebi o token na resposta
- [ ] Copiei o token COMPLETO (é bem longo!)
- [ ] Selecionei "Bearer Token" na aba Auth
- [ ] Colei APENAS o token (sem "Bearer") no campo Token
- [ ] OU colei "Bearer " + token no Header Authorization
- [ ] Cliquei em "Send"
- [ ] Recebi resposta 200 (sucesso) ao invés de 401/403

---

## ❌ ERROS COMUNS NO INSOMNIA

### **1. Erro 401 Unauthorized**

**Causas:**
- Token não foi enviado
- Token expirado (após 24 horas)
- Token inválido ou incompleto

**Solução:**
- Verifique se selecionou "Bearer Token" na aba Auth
- Faça login novamente para obter um token novo
- Certifique-se de copiar o token COMPLETO

### **2. Erro 403 Forbidden**

**Causa:** Você não tem permissão para acessar esse endpoint

**Solução:**
- Verifique se você é ALUNO ou PROFESSOR
- Alguns endpoints são exclusivos para professores
- Exemplo: Aluno não pode acessar `/professores/**`

### **3. Token não está sendo enviado**

**Verificar:**
- Vá na aba "Preview" → "Timeline" → Veja os headers enviados
- Deve ter: `Authorization: Bearer eyJhbG...`
- Se não tiver, o token não foi configurado corretamente

### **4. Token copiado errado**

**Problema:** Copiou apenas parte do token

**Solução:**
- O token JWT é MUITO LONGO (exemplo: 200+ caracteres)
- Certifique-se de copiar TUDO entre as aspas
- Formato: `eyJ` + mais caracteres + `.` + mais caracteres + `.` + mais caracteres

---

## 🔄 ORGANIZANDO SUAS REQUISIÇÕES

### **Crie uma Pasta "Autenticação"**

1. Clique com botão direito → New Folder → "Autenticação"
2. Adicione as requisições:
   - `POST /auth/registro/aluno`
   - `POST /auth/registro/professor`
   - `POST /auth/login`

### **Crie uma Pasta "Alunos (Autenticado)"**

1. New Folder → "Alunos"
2. Adicione as requisições com token:
   - `GET /alunos/{id}` (com Auth)
   - `PUT /alunos/{id}` (com Auth)
   - `GET /matriculas-aluno/historico/{id}` (com Auth)

### **Crie uma Pasta "Professores (Autenticado)"**

1. New Folder → "Professores"
2. Adicione as requisições com token:
   - `POST /disciplinas` (com Auth)
   - `POST /matriculas-aluno` (com Auth)
   - `PATCH /matriculas-aluno/atualizar-notas/{id}` (com Auth)

---

## 🎨 DICA: Cores no Insomnia

Você pode colorir suas requisições:
- **Verde**: Requisições públicas (sem autenticação)
- **Azul**: Requisições de Aluno (com token ALUNO)
- **Roxo**: Requisições de Professor (com token PROFESSOR)

---

## 🧪 TESTE RÁPIDO - 3 PASSOS

### **1. Registre um Aluno:**

```
POST http://localhost:8080/auth/registro/aluno

{
  "nome": "Teste Insomnia",
  "cpf": "11111111111",
  "email": "teste.insomnia@email.com",
  "senha": "senha123"
}
```

### **2. Copie o Token da Resposta**

Procure por `"token":` na resposta e copie o valor.

### **3. Use em Qualquer Requisição:**

```
GET http://localhost:8080/alunos/1

Aba Auth → Bearer Token → Cole o token
```

**Clique em Send → Deve funcionar! ✅**

---

## 📱 EXPORTAR/IMPORTAR REQUISIÇÕES

### **Exportar suas requisições:**
1. Clique na pasta/workspace
2. Export Data → JSON
3. Salve o arquivo

### **Importar requisições:**
1. Import/Export
2. Import Data → From File
3. Selecione o arquivo JSON

---

## 🆘 AINDA NÃO FUNCIONA?

### **Verificações:**

1. **Aplicação está rodando?**
   - Teste: http://localhost:8080/swagger-ui/index.html
   - Deve abrir a documentação

2. **Banco de dados conectado?**
   - Verifique os logs da aplicação
   - Procure por erros de conexão

3. **Token completo?**
   - Token JWT tem ~200+ caracteres
   - Começa com: `eyJ`
   - Tem 2 pontos (`.`) separando as partes

4. **Token não expirou?**
   - Tokens expiram em 24 horas
   - Faça login novamente

5. **Endpoint correto?**
   - Use `/alunos/1` (com o ID)
   - Não `/aluno/1` (sem S)

---

## 📚 ENDPOINTS QUE FUNCIONAM COM TOKEN DE ALUNO

```
✅ GET    /alunos/{id}              (ver próprio perfil)
✅ PUT    /alunos/{id}              (editar próprio perfil)
✅ GET    /matriculas-aluno/historico/{id}  (ver histórico)
```

## 📚 ENDPOINTS QUE FUNCIONAM COM TOKEN DE PROFESSOR

```
✅ GET    /professores              (listar todos)
✅ POST   /professores              (criar professor)
✅ GET    /disciplinas              (listar disciplinas)
✅ POST   /disciplinas              (criar disciplina)
✅ POST   /matriculas-aluno         (criar matrícula)
✅ PATCH  /matriculas-aluno/atualizar-notas/{id}
✅ PATCH  /matriculas-aluno/trancar/{id}
```

---

## 🎯 RESUMO FINAL

1. **Faça login** → receba o token
2. **Copie o token** (tudo após "token": "...")
3. **Aba Auth** → Bearer Token → Cole o token
4. **Send** → Pronto! ✅

**Ou:**

1. **Aba Header** → Add
2. **Name**: `Authorization`
3. **Value**: `Bearer SEU_TOKEN_AQUI`
4. **Send** → Pronto! ✅

---

## 🚀 PRONTO PARA USAR!

Agora você sabe usar tokens JWT no Insomnia como um profissional! 🎉

**Happy Testing! 💜**

