# ✅ CHECKLIST - Configuração Flyway para Apresentação

## 📋 Verificação das Configurações

### 1. ✅ Dependências no pom.xml
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```
**Status**: ✅ CONFIGURADO

---

### 2. ✅ Configurações no application.properties

```properties
# Hibernate NÃO cria mais as tabelas automaticamente
spring.jpa.hibernate.ddl-auto=validate

# Flyway habilitado
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-version=0
```

**Status**: ✅ CONFIGURADO

**O que mudou?**
- **ANTES**: `spring.jpa.hibernate.ddl-auto=update` → Hibernate criava as tabelas
- **AGORA**: `spring.jpa.hibernate.ddl-auto=validate` → Flyway cria, Hibernate apenas valida

---

### 3. ✅ Scripts de Migração Criados

Localização: `src/main/resources/db/migration/`

| Arquivo | Descrição | Status |
|---------|-----------|--------|
| `V1__create_table_aluno.sql` | Cria tabela aluno com campos: id, nome, cpf, email, senha | ✅ |
| `V2__create_table_professor.sql` | Cria tabela professor com mesma estrutura | ✅ |
| `V3__create_table_disciplina.sql` | Cria tabela disciplina com FK para professor | ✅ |
| `V4__create_table_matricula_aluno.sql` | Cria tabela matricula_aluno com FKs para aluno e disciplina | ✅ |
| `V5__insert_initial_data.sql` | Popula tabelas com dados iniciais de teste | ✅ |

---

### 4. 📊 Estrutura das Tabelas

#### Tabela: `aluno`
```sql
- id (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- nome (VARCHAR 255, NOT NULL)
- cpf (VARCHAR 11, NOT NULL, UNIQUE)
- email (VARCHAR 255, NOT NULL, UNIQUE)
- senha (VARCHAR 255, NOT NULL) -- Criptografada com BCrypt
```

#### Tabela: `professor`
```sql
- id (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- nome (VARCHAR 255, NOT NULL)
- cpf (VARCHAR 11, NOT NULL, UNIQUE)
- email (VARCHAR 255, NOT NULL, UNIQUE)
- senha (VARCHAR 255, NOT NULL) -- Criptografada com BCrypt
```

#### Tabela: `disciplina`
```sql
- id (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- nome (VARCHAR 255, NOT NULL)
- carga_horaria (INT)
- professor_id (BIGINT, FK → professor.id)
```

#### Tabela: `matricula_aluno`
```sql
- id (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
- aluno_id (BIGINT, NOT NULL, FK → aluno.id)
- disciplina_id (BIGINT, NOT NULL, FK → disciplina.id)
- nota1 (DOUBLE)
- nota2 (DOUBLE)
- status (VARCHAR 50, NOT NULL) -- ENUM: MATRICULADO, APROVADO, REPROVADO, TRANCADO
```

---

### 5. 🔄 Como o Flyway Funciona

#### Primeira execução:
1. Flyway cria uma tabela especial: `flyway_schema_history`
2. Executa os scripts em ordem: V1 → V2 → V3 → V4 → V5
3. Registra cada migração executada com timestamp

#### Execuções subsequentes:
- Flyway verifica `flyway_schema_history`
- Executa APENAS scripts novos que ainda não foram aplicados
- Garante que o banco nunca seja recriado acidentalmente

---

### 6. 📦 Dados Iniciais Populados Automaticamente

**3 Professores:**
- Dr. João Silva (joao.silva@professor.com)
- Dra. Maria Santos (maria.santos@professor.com)
- Prof. Carlos Oliveira (carlos.oliveira@professor.com)

**5 Alunos:**
- Ana Paula Costa (ana.costa@aluno.com)
- Pedro Henrique Lima (pedro.lima@aluno.com)
- Juliana Ferreira (juliana.ferreira@aluno.com)
- Lucas Rodrigues (lucas.rodrigues@aluno.com)
- Beatriz Almeida (beatriz.almeida@aluno.com)

**6 Disciplinas:**
- Programação Orientada a Objetos (80h - Prof. João)
- Banco de Dados (60h - Prof. João)
- Estruturas de Dados (80h - Profa. Maria)
- Desenvolvimento Web (60h - Profa. Maria)
- Engenharia de Software (80h - Prof. Carlos)
- Redes de Computadores (60h - Prof. Carlos)

**12 Matrículas:**
- Com notas variadas (aprovados, reprovados, matriculados, trancados)

**Senha para TODOS os usuários de teste**: `senha123`

---

### 7. 🚀 Como Executar e Testar

#### Passo 1: Limpar banco de dados (se necessário)
```sql
DROP DATABASE IF EXISTS p4_back_2025;
CREATE DATABASE p4_back_2025;
```

#### Passo 2: Executar a aplicação Spring Boot
- Execute via IDE (IntelliJ/Eclipse)
- Ou via comando: `mvnw spring-boot:run` (requer JDK 17)

#### Passo 3: Verificar logs do Flyway
Procure no console:
```
Flyway Community Edition...
Successfully validated 5 migrations
Current version of schema `p4_back_2025`: 0
Migrating schema `p4_back_2025` to version 1 - create table aluno
Migrating schema `p4_back_2025` to version 2 - create table professor
Migrating schema `p4_back_2025` to version 3 - create table disciplina
Migrating schema `p4_back_2025` to version 4 - create table matricula aluno
Migrating schema `p4_back_2025` to version 5 - insert initial data
Successfully applied 5 migrations
```

#### Passo 4: Verificar no banco de dados
```sql
-- Ver histórico de migrações
SELECT * FROM flyway_schema_history;

-- Ver dados populados
SELECT * FROM aluno;
SELECT * FROM professor;
SELECT * FROM disciplina;
SELECT * FROM matricula_aluno;
```

---

### 8. ⚠️ IMPORTANTE - Requisitos

#### Para executar você precisa:
1. ✅ MySQL rodando (porta 3306)
2. ✅ Banco `p4_back_2025` criado
3. ⚠️ **JDK 17 instalado** (não JRE)
4. ✅ Dependências Maven baixadas

#### Verificar JDK:
```bash
java -version
# Deve mostrar: java version "17.x.x"
```

Se mostrar Java 8, você precisa instalar JDK 17:
- Download: https://www.oracle.com/java/technologies/downloads/#java17

---

### 9. 🎯 O QUE EXPLICAR AO PROFESSOR

#### Vantagens da Migração com Flyway:

✅ **Versionamento**: Cada mudança no banco é versionada (V1, V2, V3...)  
✅ **Rastreabilidade**: Histórico completo em `flyway_schema_history`  
✅ **Automação**: Tabelas criadas automaticamente ao iniciar a aplicação  
✅ **Consistência**: Mesmo esquema em todos os ambientes (dev, prod)  
✅ **Segurança**: Não precisa mais executar scripts SQL manualmente  
✅ **População Automática**: Dados de teste inseridos automaticamente  

#### Diferença de Abordagens:

**❌ ANTES (Hibernate ddl-auto=update):**
- Spring cria tabelas baseado nas anotações @Entity
- Sem controle de versão
- Sem histórico de mudanças
- Arriscado em produção

**✅ AGORA (Flyway):**
- Scripts SQL versionados e rastreáveis
- Controle total sobre o esquema
- Seguro para produção
- Facilita trabalho em equipe

---

### 10. 🧪 Teste Rápido

#### Testar Login após população automática:

**Requisição:**
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "ana.costa@aluno.com",
  "senha": "senha123",
  "tipoUsuario": "ALUNO"
}
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "id": 1,
  "nome": "Ana Paula Costa",
  "email": "ana.costa@aluno.com",
  "tipoUsuario": "ALUNO"
}
```

---

### 11. 📝 Resumo Final

**SIM**, você configurou tudo corretamente para que o Spring (via Flyway):
1. ✅ Crie as tabelas automaticamente
2. ✅ Popule com dados iniciais
3. ✅ Versione as mudanças no banco
4. ✅ Mantenha histórico de migrações

**Para funcionar, você só precisa:**
- Ter MySQL rodando ✅
- Ter JDK 17 instalado ⚠️ (atualmente você tem Java 8)
- Executar a aplicação Spring Boot

**Ordem de execução do Flyway:**
```
V1 → Cria tabela aluno
V2 → Cria tabela professor  
V3 → Cria tabela disciplina (com FK para professor)
V4 → Cria tabela matricula_aluno (com FKs para aluno e disciplina)
V5 → Insere dados de teste (3 professores, 5 alunos, 6 disciplinas, 12 matrículas)
```

---

**✨ Pronto para apresentar ao professor!**

