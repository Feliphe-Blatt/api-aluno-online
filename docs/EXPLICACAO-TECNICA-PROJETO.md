# Explicação Técnica do Projeto - API Aluno Online

## Preparação para Apresentação ao Professor

---

## 1. 🔐 SISTEMA DE AUTENTICAÇÃO JWT (JSON Web Token)

### O que é JWT?
JWT é um padrão de token seguro usado para autenticar usuários sem precisar armazenar sessões no servidor. Quando o usuário faz login, ele recebe um token que deve ser enviado em todas as requisições subsequentes.

### Como funciona no projeto:

#### 📁 **JwtService.java** - Serviço Principal de JWT
- **Localização**: `src/main/java/br/com/alunoonline/api/security/JwtService.java`
- **Responsabilidades**:
  - `generateToken()`: Gera um token JWT após login bem-sucedido
  - `validateToken()`: Verifica se o token é válido e não expirou
  - `extractUsername()`: Extrai o email do usuário do token
  - `extractClaim()`: Extrai informações adicionais do token (id, nome, tipoUsuario)

#### Configurações JWT (application.properties):
```properties
jwt.secret=minha-chave-secreta-super-segura-para-jwt-authentication-aluno-online-2024-minimum-256-bits
jwt.expiration=86400000  # 24 horas em milissegundos
```

#### Dependências utilizadas (pom.xml):
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
```

#### Fluxo de Autenticação:
1. Usuário envia email e senha para `/auth/login`
2. Sistema valida credenciais
3. Se válido, gera token JWT com informações do usuário
4. Token é retornado ao cliente
5. Cliente envia token no header `Authorization: Bearer {token}` em todas as requisições
6. Filtro `JwtAuthenticationFilter` valida o token antes de processar a requisição

---

## 2. 👥 SISTEMA DE ROLES (Controle de Acesso)

### O que são Roles?
Roles (papéis) definem permissões de acesso. No projeto, temos dois tipos de usuários com permissões diferentes.

### 📁 **TipoUsuario.java** - Enum de Roles
```java
public enum TipoUsuario {
    ALUNO,
    PROFESSOR
}
```

### 📁 **UserDetailsImpl.java** - Implementação de Usuário com Roles
- **Responsabilidade**: Adaptar entidades (Aluno/Professor) para o Spring Security
- **Método importante**: `getAuthorities()` retorna a role como `ROLE_ALUNO` ou `ROLE_PROFESSOR`

### 📁 **SecurityConfig.java** - Configuração de Permissões

#### Permissões por Role:

**🎓 ROLE_ALUNO pode:**
- ✅ Ver próprios dados: `GET /alunos/{id}` (somente seu próprio ID)
- ✅ Atualizar próprios dados: `PUT /alunos/{id}` (somente seu próprio ID)
- ✅ Ver histórico de matrículas: `GET /matriculas/historico-aluno/**`

**👨‍🏫 ROLE_PROFESSOR pode:**
- ✅ TUDO que ALUNO pode fazer
- ✅ Gerenciar professores: `/professores/**` (CRUD completo)
- ✅ Gerenciar disciplinas: `/disciplinas/**` (CRUD completo)
- ✅ Criar matrículas: `POST /matriculas`
- ✅ Trancar matrículas: `PATCH /matriculas/trancar/**`
- ✅ Atualizar notas: `PATCH /matriculas/atualizar-notas/**`

**🌐 PÚBLICO (sem autenticação):**
- ✅ Login: `/auth/**`
- ✅ Documentação Swagger: `/swagger-ui/**`, `/v3/api-docs/**`

#### Código relevante:
```java
.requestMatchers(HttpMethod.GET, "/alunos/{id}").hasAnyRole("ALUNO", "PROFESSOR")
.requestMatchers(HttpMethod.PUT, "/alunos/{id}").hasRole("ALUNO")
.requestMatchers("/professores/**").hasRole("PROFESSOR")
.requestMatchers("/disciplinas/**").hasRole("PROFESSOR")
```

---

## 3. 🗄️ MIGRAÇÃO DE PostgreSQL PARA MySQL

### Por que migrar?
- MySQL é mais leve e amplamente usado em projetos acadêmicos
- Melhor compatibilidade com ferramentas gratuitas
- Mais fácil de instalar e configurar localmente

### Mudanças realizadas:

#### 📁 **pom.xml** - Dependências de Banco
```xml
<!-- PostgreSQL (mantido para compatibilidade opcional) -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- MySQL (driver atual) -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

#### 📁 **application.properties** - Configurações do Banco

**ANTES (PostgreSQL):**
```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/alunoonline
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**DEPOIS (MySQL):**
```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/p4_back_2025
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Diferenças importantes PostgreSQL vs MySQL:
- **Porta padrão**: PostgreSQL usa 5432, MySQL usa 3306
- **Dialeto Hibernate**: Cada banco tem seu próprio dialeto de SQL
- **AUTO_INCREMENT**: MySQL usa `AUTO_INCREMENT`, PostgreSQL usa `SERIAL`
- **Tipos de dados**: Pequenas diferenças em tipos como TEXT, VARCHAR, etc.

---

## 4. 🚀 FLYWAY - GERENCIAMENTO DE MIGRAÇÕES

### O que é Flyway?
Flyway é uma ferramenta de versionamento de banco de dados que permite:
- ✅ Criar tabelas de forma controlada e versionada
- ✅ Rastrear mudanças no esquema do banco
- ✅ Aplicar migrações automaticamente ao iniciar a aplicação
- ✅ Evitar inconsistências entre ambientes (dev, prod)

### Configuração implementada:

#### 📁 **pom.xml** - Dependências Flyway
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

#### 📁 **application.properties** - Configurações Flyway
```properties
# Antes: Spring criava tabelas automaticamente
spring.jpa.hibernate.ddl-auto=update

# Agora: Spring apenas valida, Flyway gerencia
spring.jpa.hibernate.ddl-auto=validate

# Configurações Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-version=0
```

### Scripts de Migração criados:

#### 📄 **V1__create_table_aluno.sql**
- Cria tabela `aluno` com campos: id, nome, cpf, email, senha
- CPF e email são UNIQUE (não podem repetir)
- Adiciona índices para melhorar buscas por CPF e email

#### 📄 **V2__create_table_professor.sql**
- Cria tabela `professor` com estrutura similar ao aluno
- CPF e email são UNIQUE
- Adiciona índices otimizados

#### 📄 **V3__create_table_disciplina.sql**
- Cria tabela `disciplina` com campos: id, nome, carga_horaria, professor_id
- **CHAVE ESTRANGEIRA**: professor_id referencia professor(id)
- `ON DELETE SET NULL`: Se professor for deletado, disciplina fica sem professor

#### 📄 **V4__create_table_matricula_aluno.sql**
- Cria tabela `matricula_aluno` com: id, aluno_id, disciplina_id, nota1, nota2, status
- **DUAS CHAVES ESTRANGEIRAS**:
  - aluno_id → aluno(id)
  - disciplina_id → disciplina(id)
- `ON DELETE CASCADE`: Se aluno ou disciplina for deletado, matrícula também é deletada
- Status usa ENUM: MATRICULADO, APROVADO, REPROVADO, TRANCADO

### Convenção de nomenclatura Flyway:
```
V{versão}__{descrição}.sql
V1__create_table_aluno.sql
V2__create_table_professor.sql
```
- **V**: Indica migração versionada
- **Número**: Ordem de execução (1, 2, 3, 4...)
- **Dois underscores**: Separador obrigatório
- **Descrição**: Nome descritivo da migração

---

## 5. 🏗️ ARQUITETURA DO SISTEMA

### Camadas da aplicação:

```
┌─────────────────────────────────────┐
│        CONTROLLERS                  │  ← Recebe requisições HTTP
│  (AlunoController, AuthController)  │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     SECURITY FILTERS                │  ← Valida JWT Token
│   (JwtAuthenticationFilter)         │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│         SERVICES                    │  ← Lógica de negócio
│  (AlunoService, AuthService)        │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│       REPOSITORIES                  │  ← Acesso ao banco
│  (AlunoRepository - JPA)            │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      BANCO DE DADOS MySQL           │  ← Tabelas gerenciadas
│    (Criado via Flyway)              │     pelo Flyway
└─────────────────────────────────────┘
```

---

## 6. 📊 MODELOS E RELACIONAMENTOS

### Entidades JPA:

#### **Aluno** (model/Aluno.java)
- `@Entity` - Marca como entidade JPA
- `@Table(name = "aluno")` - Nome da tabela no banco
- `@Id @GeneratedValue` - ID auto-incrementado
- `@Column(unique = true)` - CPF e email únicos
- `@JsonProperty(access = WRITE_ONLY)` - Senha nunca é retornada nas respostas

#### **Professor** (model/Professor.java)
- Estrutura idêntica ao Aluno
- Diferenciados pelo TipoUsuario no sistema de autenticação

#### **Disciplina** (model/Disciplina.java)
- `@ManyToOne` - Muitas disciplinas para um professor
- `@JoinColumn(name = "professor_id")` - Chave estrangeira

#### **MatriculaAluno** (model/MatriculaAluno.java)
- `@ManyToOne` - Muitas matrículas para um aluno
- `@ManyToOne` - Muitas matrículas para uma disciplina
- `@Enumerated(EnumType.STRING)` - Status armazenado como texto

### Relacionamentos:
```
Professor (1) ──────< (N) Disciplina
    │
    │
Aluno (1) ──────< (N) MatriculaAluno >──────< (N) Disciplina
```

---

## 7. 🔧 COMO TESTAR

### 1. Limpar e recompilar o projeto:
```bash
mvn clean install
```

### 2. Executar a aplicação:
```bash
mvn spring-boot:run
```

### 3. Verificar Flyway:
- Ao iniciar, Flyway criará uma tabela `flyway_schema_history`
- Executará os scripts V1, V2, V3, V4 em ordem
- Registrará cada migração executada

### 4. Testar autenticação:
- **Criar aluno**: `POST /alunos` (público)
- **Login**: `POST /auth/login` → Retorna token JWT
- **Usar token**: Adicionar header `Authorization: Bearer {token}`

---

## 8. 📝 PONTOS IMPORTANTES PARA A APRESENTAÇÃO

### Vantagens da Implementação:

✅ **Segurança**:
- Senhas criptografadas com BCrypt
- Tokens JWT expiram em 24 horas
- Controle de acesso baseado em roles

✅ **Escalabilidade**:
- Stateless (sem sessões no servidor)
- Pool de conexões otimizado (Hikari)

✅ **Manutenibilidade**:
- Migrações versionadas com Flyway
- Código limpo com Lombok
- Validações automáticas (Bean Validation)

✅ **Documentação**:
- Swagger UI acessível em `/swagger-ui.html`
- Endpoints documentados automaticamente

### Tecnologias utilizadas:
- **Spring Boot 3.3.5**
- **Spring Security** (autenticação e autorização)
- **JWT (JJWT 0.12.3)** (tokens seguros)
- **Flyway** (migrações de banco)
- **MySQL 8** (banco de dados)
- **Hibernate/JPA** (ORM)
- **Lombok** (redução de boilerplate)
- **Bean Validation** (validações)
- **Swagger/OpenAPI** (documentação)

---

## 9. 🎯 RESUMO EXECUTIVO

Este projeto implementa uma **API REST completa** para gerenciamento de alunos online com:

1. **Autenticação JWT**: Sistema seguro sem sessões no servidor
2. **Controle de Acesso**: Alunos e professores têm permissões diferentes
3. **Banco MySQL**: Migrado de PostgreSQL para facilitar uso
4. **Flyway**: Tabelas criadas automaticamente via migrações versionadas
5. **Segurança**: Senhas criptografadas, tokens com expiração, validações

### Diferencial:
Não é mais necessário criar tabelas manualmente no banco. O Flyway gerencia tudo automaticamente ao iniciar a aplicação, garantindo que todos os ambientes tenham exatamente a mesma estrutura de banco de dados.

---

## 10. 📌 COMANDOS ÚTEIS

### Verificar migrações do Flyway:
```bash
mvn flyway:info
```

### Executar migrações manualmente:
```bash
mvn flyway:migrate
```

### Limpar banco (cuidado! apaga tudo):
```bash
mvn flyway:clean
```

### Rodar testes:
```bash
mvn test
```

### Gerar arquivo JAR:
```bash
mvn package
```

---

**Preparado por**: Sistema API Aluno Online  
**Data**: Novembro 2025  
**Tecnologia**: Spring Boot 3 + JWT + MySQL + Flyway

