# 🔐 Explicação Detalhada: JWT, Roles e Migração de Banco

## Para Apresentação ao Professor

---

## PARTE 1: SISTEMA JWT (JSON Web Token)

### 📌 O que é JWT e por que usamos?

JWT é um padrão de autenticação **stateless** (sem estado no servidor). Ao invés de guardar sessões no servidor, o token contém todas as informações necessárias.

**Vantagens:**
- ✅ Escalável (não precisa guardar sessão no servidor)
- ✅ Seguro (assinado digitalmente)
- ✅ Auto-contido (contém dados do usuário)
- ✅ Expira automaticamente (24 horas)

---

### 🔧 CÓDIGO JWT - Explicação Linha por Linha

#### 1️⃣ JwtService.java - Gerenciador de Tokens

**Localização**: `src/main/java/br/com/alunoonline/api/security/JwtService.java`

```java
@Service
public class JwtService {
  
  // Chave secreta para assinar tokens (configurada no application.properties)
  @Value("${jwt.secret}")
  private String secret;
  
  // Tempo de expiração: 86400000 ms = 24 horas
  @Value("${jwt.expiration:86400000}")
  private Long expiration;
```

**📝 Explicação:**
- `@Value` injeta valores do `application.properties`
- `secret` é a chave usada para assinar e validar tokens
- `expiration` define quanto tempo o token é válido

---

#### Método: generateToken()
```java
public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    
    if (userDetails instanceof UserDetailsImpl) {
      UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
      // Adiciona informações extras no token
      claims.put("id", userDetailsImpl.getId());
      claims.put("nome", userDetailsImpl.getNome());
      claims.put("tipoUsuario", userDetailsImpl.getTipoUsuario().name());
    }
    
    return createToken(claims, userDetails.getUsername());
}
```

**📝 Explicação:**
- `claims` são as informações armazenadas no token
- Guardamos: **id**, **nome**, **tipoUsuario** (ALUNO ou PROFESSOR)
- `username` é o email do usuário

**Exemplo de Token Gerado:**
```
eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidGlwb1VzdWFyaW8iOiJBTFVOTyIsIm5vbWUiOiJBbmEgUGF1bGEiLCJzdWIiOiJhbmEuY29zdGFAYWx1bm8uY29tIiwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwODY0MDB9.signature
```

**Decodificado:**
```json
{
  "id": 1,
  "tipoUsuario": "ALUNO",
  "nome": "Ana Paula",
  "sub": "ana.costa@aluno.com",
  "iat": 1700000000,  // Data de criação
  "exp": 1700086400   // Data de expiração
}
```

---

#### Método: validateToken()
```java
public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
}
```

**📝 Explicação:**
- Extrai o username (email) do token
- Verifica se corresponde ao usuário logado
- Verifica se o token não expirou

---

### 2️⃣ JwtAuthenticationFilter.java - Filtro de Segurança

**Localização**: `src/main/java/br/com/alunoonline/api/security/JwtAuthenticationFilter.java`

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) {
    
    // 1. Pega o header Authorization
    final String authorizationHeader = request.getHeader("Authorization");
    
    String username = null;
    String jwt = null;
    
    // 2. Verifica se começa com "Bearer "
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      // 3. Remove "Bearer " e pega só o token
      jwt = authorizationHeader.substring(7);
      
      // 4. Extrai o email do token
      username = jwtService.extractUsername(jwt);
    }
    
    // 5. Se tem username e não está autenticado ainda
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      
      // 6. Carrega dados do usuário do banco
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      
      // 7. Valida o token
      if (jwtService.validateToken(jwt, userDetails)) {
        // 8. Autentica o usuário no Spring Security
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities() // Inclui as ROLES
            );
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    
    // 9. Continua o processamento da requisição
    filterChain.doFilter(request, response);
  }
}
```

**📝 Fluxo de cada requisição:**
```
Requisição HTTP
    ↓
1. Extrai header "Authorization: Bearer {token}"
    ↓
2. Valida token JWT
    ↓
3. Se válido, extrai email
    ↓
4. Busca usuário no banco
    ↓
5. Carrega roles (ALUNO ou PROFESSOR)
    ↓
6. Autentica no Spring Security
    ↓
7. Processa a requisição
```

---

## PARTE 2: SISTEMA DE ROLES (Controle de Acesso)

### 📌 O que são Roles?

Roles definem **O QUE cada tipo de usuário pode fazer** no sistema.

---

### 🔧 CÓDIGO ROLES - Explicação

#### 1️⃣ TipoUsuario.java - Enum de Tipos

**Localização**: `src/main/java/br/com/alunoonline/api/enums/TipoUsuario.java`

```java
public enum TipoUsuario {
  ALUNO,
  PROFESSOR
}
```

**📝 Explicação:**
- Define os 2 tipos de usuários no sistema
- Usado para diferenciar permissões
- Armazenado no banco e no token JWT

---

#### 2️⃣ UserDetailsImpl.java - Adaptador de Usuário

**Localização**: `src/main/java/br/com/alunoonline/api/security/UserDetailsImpl.java`

```java
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
  
  private Long id;
  private String nome;
  private String email;
  private String senha;
  private TipoUsuario tipoUsuario; // ALUNO ou PROFESSOR
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Converte TipoUsuario em Role do Spring Security
    return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
  }
  
  @Override
  public String getUsername() {
    return email; // Email é usado como username
  }
  
  @Override
  public String getPassword() {
    return senha;
  }
}
```

**📝 Explicação:**
- `UserDetails` é a interface padrão do Spring Security
- `getAuthorities()` retorna as roles do usuário
- Se `tipoUsuario = ALUNO` → retorna `ROLE_ALUNO`
- Se `tipoUsuario = PROFESSOR` → retorna `ROLE_PROFESSOR`

**Exemplo:**
```java
// Aluno
getAuthorities() → ["ROLE_ALUNO"]

// Professor
getAuthorities() → ["ROLE_PROFESSOR"]
```

---

#### 3️⃣ UserDetailsServiceImpl.java - Carregador de Usuário

**Localização**: `src/main/java/br/com/alunoonline/api/security/UserDetailsServiceImpl.java`

```java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  
  @Autowired
  private AlunoRepository alunoRepository;
  
  @Autowired
  private ProfessorRepository professorRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) {
    // 1. Tenta encontrar como ALUNO
    Aluno aluno = alunoRepository.findByEmail(username).orElse(null);
    if (aluno != null) {
      return new UserDetailsImpl(
          aluno.getId(),
          aluno.getNome(),
          aluno.getEmail(),
          aluno.getSenha(),
          TipoUsuario.ALUNO  // Define role ALUNO
      );
    }
    
    // 2. Tenta encontrar como PROFESSOR
    Professor professor = professorRepository.findByEmail(username).orElse(null);
    if (professor != null) {
      return new UserDetailsImpl(
          professor.getId(),
          professor.getNome(),
          professor.getEmail(),
          professor.getSenha(),
          TipoUsuario.PROFESSOR  // Define role PROFESSOR
      );
    }
    
    throw new UsernameNotFoundException("Usuário não encontrado");
  }
}
```

**📝 Explicação:**
- Busca usuário nas tabelas `aluno` e `professor`
- Identifica automaticamente se é ALUNO ou PROFESSOR
- Retorna `UserDetailsImpl` com a role correta

---

#### 4️⃣ SecurityConfig.java - Configuração de Permissões

**Localização**: `src/main/java/br/com/alunoonline/api/config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        
        // 🌐 PÚBLICO (sem autenticação)
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
        
        // 🎓 ALUNO e PROFESSOR podem ver dados de alunos
        .requestMatchers(HttpMethod.GET, "/alunos/{id}")
          .hasAnyRole("ALUNO", "PROFESSOR")
        
        // 🎓 Apenas ALUNO pode atualizar seus próprios dados
        .requestMatchers(HttpMethod.PUT, "/alunos/{id}")
          .hasRole("ALUNO")
        
        // 👨‍🏫 Apenas PROFESSOR pode gerenciar
        .requestMatchers("/professores/**").hasRole("PROFESSOR")
        .requestMatchers("/disciplinas/**").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.POST, "/matriculas").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.PATCH, "/matriculas/trancar/**").hasRole("PROFESSOR")
        .requestMatchers(HttpMethod.PATCH, "/matriculas/atualizar-notas/**").hasRole("PROFESSOR")
        
        // 🎓 ALUNO pode ver seu próprio histórico
        .requestMatchers(HttpMethod.GET, "/matriculas/historico-aluno/**")
          .hasRole("ALUNO")
        
        // 🔒 Tudo mais requer autenticação
        .anyRequest().authenticated()
      )
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}
```

**📝 Tabela de Permissões:**

| Endpoint | ALUNO | PROFESSOR | PÚBLICO |
|----------|-------|-----------|---------|
| `POST /auth/login` | ✅ | ✅ | ✅ |
| `POST /auth/registro/aluno` | ✅ | ✅ | ✅ |
| `GET /alunos/{id}` | ✅ (próprio) | ✅ (todos) | ❌ |
| `PUT /alunos/{id}` | ✅ (próprio) | ❌ | ❌ |
| `GET /professores/**` | ❌ | ✅ | ❌ |
| `POST /disciplinas` | ❌ | ✅ | ❌ |
| `POST /matriculas` | ❌ | ✅ | ❌ |
| `PATCH /matriculas/atualizar-notas` | ❌ | ✅ | ❌ |
| `GET /matriculas/historico-aluno` | ✅ (próprio) | ❌ | ❌ |

---

### 5️⃣ AuthService.java - Lógica de Autenticação

**Localização**: `src/main/java/br/com/alunoonline/api/service/AuthService.java`

```java
@Service
public class AuthService {
  
  public LoginResponseDTO login(LoginRequestDTO loginRequest) {
    // 1. Busca usuário por email e tipo
    UserDetails userDetails = userDetailsService.loadUserByUsernameAndType(
        loginRequest.getEmail(),
        loginRequest.getTipoUsuario()
    );
    
    // 2. Autentica (verifica senha)
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(),
            loginRequest.getSenha()
        )
    );
    
    // 3. Gera token JWT
    String token = jwtService.generateToken(userDetails);
    
    // 4. Retorna token + dados do usuário
    return new LoginResponseDTO(
        token,
        userDetailsImpl.getId(),
        userDetailsImpl.getNome(),
        userDetailsImpl.getEmail(),
        userDetailsImpl.getTipoUsuario()
    );
  }
}
```

**📝 Fluxo de Login:**
```
1. Cliente envia: { email, senha, tipoUsuario }
    ↓
2. Sistema busca usuário no banco (tabela aluno ou professor)
    ↓
3. Verifica senha com BCrypt
    ↓
4. Se válido, gera token JWT com dados do usuário
    ↓
5. Retorna: { token, id, nome, email, tipoUsuario }
    ↓
6. Cliente guarda token e envia em todas as requisições
```

---

### 6️⃣ AuthController.java - Endpoints de Autenticação

**Localização**: `src/main/java/br/com/alunoonline/api/controller/AuthController.java`

```java
@RestController
@RequestMapping("/auth")
public class AuthController {
  
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
    LoginResponseDTO response = authService.login(loginRequest);
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/registro/aluno")
  public ResponseEntity<?> registrarAluno(@Valid @RequestBody Aluno aluno) {
    // Criptografa senha e salva no banco
    LoginResponseDTO response = authService.registrarAluno(aluno);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
```

**📝 Endpoints disponíveis:**
- `POST /auth/login` → Login (retorna token)
- `POST /auth/registro/aluno` → Criar conta de aluno
- `POST /auth/registro/professor` → Criar conta de professor

---

## PARTE 2: MODELOS COM ROLES

### 📌 Como as Roles estão integradas nos Models?

As entidades `Aluno` e `Professor` **NÃO têm campo `role` diretamente**, mas são diferenciadas pelo **tipo de tabela**.

#### Aluno.java
```java
@Entity
@Table(name = "aluno")
public class Aluno {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotBlank(message = "Nome é obrigatório")
  private String nome;
  
  @NotBlank(message = "CPF é obrigatório")
  @Size(min = 11, max = 11)
  @Column(unique = true)
  private String cpf;
  
  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  @Column(unique = true)
  private String email;
  
  @NotBlank(message = "Senha é obrigatória")
  @Size(min = 6)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Senha nunca é retornada
  private String senha;
}
```

**📝 Explicação das Anotações:**

- `@Entity` → Marca como entidade JPA (tabela no banco)
- `@Table(name = "aluno")` → Nome da tabela: `aluno`
- `@Id @GeneratedValue` → ID auto-incrementado
- `@Column(unique = true)` → CPF e email únicos (não podem repetir)
- `@NotBlank`, `@Email`, `@Size` → Validações automáticas
- `@JsonProperty(access = WRITE_ONLY)` → Senha só pode ser enviada, nunca retornada

**Professor.java** tem a mesma estrutura!

---

### 📌 Como a Role é determinada?

```java
// UserDetailsServiceImpl.java

// Se encontrar na tabela ALUNO → Role = ALUNO
Aluno aluno = alunoRepository.findByEmail(email);
return new UserDetailsImpl(..., TipoUsuario.ALUNO);

// Se encontrar na tabela PROFESSOR → Role = PROFESSOR
Professor professor = professorRepository.findByEmail(email);
return new UserDetailsImpl(..., TipoUsuario.PROFESSOR);
```

**Conclusão:**
- Alunos na tabela `aluno` → automaticamente `ROLE_ALUNO`
- Professores na tabela `professor` → automaticamente `ROLE_PROFESSOR`

---

## PARTE 3: MIGRAÇÃO POSTGRESQL → MYSQL

### 📌 Por que migrar?

**Motivos técnicos:**
- ✅ MySQL é mais leve e rápido para projetos pequenos/médios
- ✅ Mais fácil de instalar e configurar no Windows
- ✅ Amplamente usado em ambientes acadêmicos
- ✅ Melhor integração com ferramentas gratuitas (phpMyAdmin, Workbench)

---

### 🔧 MUDANÇAS REALIZADAS

#### 1️⃣ pom.xml - Dependências

**ANTES (só PostgreSQL):**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

**DEPOIS (PostgreSQL + MySQL):**
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

**📝 Explicação:**
- Mantemos PostgreSQL para compatibilidade (caso alguém queira usar)
- Adicionamos MySQL connector para conexão com MySQL
- `scope=runtime` → Só necessário na execução, não na compilação

---

#### 2️⃣ application.properties - Configuração de Conexão

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

**📝 Explicação das mudanças:**

| Configuração | PostgreSQL | MySQL |
|--------------|------------|-------|
| **Driver** | `org.postgresql.Driver` | `com.mysql.cj.jdbc.Driver` |
| **URL** | `jdbc:postgresql://...` | `jdbc:mysql://...` |
| **Porta** | 5432 | 3306 |
| **Dialeto** | `PostgreSQLDialect` | `MySQLDialect` |

---

#### 3️⃣ Diferenças SQL

**AUTO_INCREMENT:**
```sql
-- PostgreSQL
id SERIAL PRIMARY KEY

-- MySQL
id BIGINT AUTO_INCREMENT PRIMARY KEY
```

**Tipos de String:**
```sql
-- PostgreSQL
nome TEXT

-- MySQL
nome VARCHAR(255)
```

**Encoding:**
```sql
-- MySQL (adiciona suporte a caracteres especiais)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
```

---

## PARTE 4: FLYWAY - MIGRAÇÕES DE BANCO

### 📌 Como funciona o Flyway?

Flyway executa scripts SQL em **ordem de versão** e registra em uma tabela especial.

---

### 🗂️ Scripts de Migração

**Convenção de nomenclatura:**
```
V{versão}__{descrição}.sql
V1__create_table_aluno.sql
V2__create_table_professor.sql
```

- `V` = Versioned migration (migração versionada)
- Número = Ordem de execução
- `__` = Dois underscores (obrigatório)
- Descrição = Nome legível

---

### 📄 V1__create_table_aluno.sql

```sql
CREATE TABLE aluno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_aluno_cpf ON aluno(cpf);
CREATE INDEX idx_aluno_email ON aluno(email);
```

**📝 O que faz:**
- Cria tabela `aluno`
- Define CPF e email como únicos
- Cria índices para buscas rápidas por CPF e email

---

### 📄 V3__create_table_disciplina.sql

```sql
CREATE TABLE disciplina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    carga_horaria INT,
    professor_id BIGINT,
    CONSTRAINT fk_disciplina_professor FOREIGN KEY (professor_id) 
        REFERENCES professor(id) 
        ON DELETE SET NULL
);
```

**📝 Chave Estrangeira:**
- `professor_id` referencia `professor(id)`
- `ON DELETE SET NULL` → Se professor for deletado, disciplina não é deletada, apenas fica sem professor

---

### 📄 V4__create_table_matricula_aluno.sql

```sql
CREATE TABLE matricula_aluno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    disciplina_id BIGINT NOT NULL,
    nota1 DOUBLE,
    nota2 DOUBLE,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) 
        REFERENCES aluno(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_matricula_disciplina FOREIGN KEY (disciplina_id) 
        REFERENCES disciplina(id) 
        ON DELETE CASCADE
);
```

**📝 Chaves Estrangeiras:**
- `aluno_id` → `aluno(id)` com `CASCADE`
- `disciplina_id` → `disciplina(id)` com `CASCADE`
- `ON DELETE CASCADE` → Se aluno ou disciplina for deletado, matrícula também é deletada

---

### 📄 V5__insert_initial_data.sql

```sql
-- Popula dados de teste automaticamente
INSERT INTO professor (nome, cpf, email, senha) VALUES
('Dr. João Silva', '12345678901', 'joao.silva@professor.com', '{hash_bcrypt}');

INSERT INTO aluno (nome, cpf, email, senha) VALUES
('Ana Paula Costa', '45678901234', 'ana.costa@aluno.com', '{hash_bcrypt}');

INSERT INTO disciplina (nome, carga_horaria, professor_id) VALUES
('Programação Orientada a Objetos', 80, 1);

INSERT INTO matricula_aluno (aluno_id, disciplina_id, nota1, nota2, status) VALUES
(1, 1, 8.5, 9.0, 'APROVADO');
```

**📝 O que faz:**
- Insere 3 professores
- Insere 5 alunos
- Insere 6 disciplinas
- Insere 12 matrículas com notas variadas

---

### 🔄 Fluxo de Execução do Flyway

```
1. Aplicação Spring Boot inicia
    ↓
2. Flyway verifica banco de dados
    ↓
3. Cria tabela flyway_schema_history (se não existir)
    ↓
4. Verifica quais migrações já foram executadas
    ↓
5. Executa apenas migrações novas em ordem:
   V1 → V2 → V3 → V4 → V5
    ↓
6. Registra cada migração executada com timestamp
    ↓
7. Hibernate valida se estrutura está correta
    ↓
8. Aplicação pronta para uso!
```

---

### 📊 Tabela flyway_schema_history

Após execução, Flyway cria esta tabela:

| installed_rank | version | description | type | script | checksum | installed_on | execution_time | success |
|----------------|---------|-------------|------|--------|----------|--------------|----------------|---------|
| 1 | 1 | create table aluno | SQL | V1__create_table_aluno.sql | 123456 | 2025-11-25 10:00:00 | 45 | true |
| 2 | 2 | create table professor | SQL | V2__create_table_professor.sql | 234567 | 2025-11-25 10:00:00 | 32 | true |
| 3 | 3 | create table disciplina | SQL | V3__create_table_disciplina.sql | 345678 | 2025-11-25 10:00:01 | 28 | true |
| 4 | 4 | create table matricula aluno | SQL | V4__create_table_matricula_aluno.sql | 456789 | 2025-11-25 10:00:01 | 35 | true |
| 5 | 5 | insert initial data | SQL | V5__insert_initial_data.sql | 567890 | 2025-11-25 10:00:01 | 52 | true |

**📝 Explicação:**
- Cada linha = uma migração executada
- `checksum` = hash do arquivo (detecta modificações indevidas)
- `success` = se executou com sucesso
- **Flyway NUNCA executa a mesma versão duas vezes!**

---

## PARTE 5: INTEGRAÇÃO COMPLETA

### 🔄 Como tudo funciona junto?

```
┌─────────────────────────────────────────────────────────────┐
│  1. USUÁRIO FAZ LOGIN                                       │
│     POST /auth/login                                        │
│     { email, senha, tipoUsuario: "ALUNO" }                  │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  2. AuthService busca na tabela ALUNO (criada pelo Flyway) │
│     SELECT * FROM aluno WHERE email = 'ana.costa@aluno.com' │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  3. Verifica senha com BCrypt                               │
│     passwordEncoder.matches(senhaDigitada, senhaHashBanco)  │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  4. Cria UserDetailsImpl com ROLE_ALUNO                     │
│     new UserDetailsImpl(..., TipoUsuario.ALUNO)             │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  5. JwtService gera token com dados + role                  │
│     { id: 1, nome: "Ana", tipoUsuario: "ALUNO" }            │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  6. Retorna token para o cliente                            │
│     { token: "eyJhbGc...", id: 1, nome: "Ana", ... }        │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  7. Cliente usa token em requisições futuras                │
│     Authorization: Bearer eyJhbGc...                         │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  8. JwtAuthenticationFilter valida token                    │
│     - Extrai email e role do token                          │
│     - Autentica no Spring Security                          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  9. SecurityConfig verifica permissões                      │
│     .hasRole("ALUNO") → Libera acesso se for ALUNO          │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 RESUMO PARA APRESENTAÇÃO

### 1. JWT - Autenticação Segura
- ✅ Token gerado após login
- ✅ Contém: id, nome, email, tipoUsuario
- ✅ Expira em 24 horas
- ✅ Validado em cada requisição

### 2. Roles - Controle de Acesso
- ✅ 2 tipos: ALUNO e PROFESSOR
- ✅ Determinado pela tabela de origem
- ✅ Permissões configuradas no SecurityConfig
- ✅ Aluno: acessa próprios dados
- ✅ Professor: gerencia sistema completo

### 3. Migração PostgreSQL → MySQL
- ✅ Mudança de driver e dialeto
- ✅ Ajustes na URL de conexão
- ✅ Adaptação de tipos SQL
- ✅ Configuração de encoding UTF-8

### 4. Flyway - Gerenciamento de Banco
- ✅ 5 scripts de migração
- ✅ Cria tabelas automaticamente
- ✅ Popula dados de teste
- ✅ Versionamento do esquema
- ✅ Histórico rastreável

---

## 📌 CREDENCIAIS DE TESTE

**Professores:**
- joao.silva@professor.com / senha123
- maria.santos@professor.com / senha123
- carlos.oliveira@professor.com / senha123

**Alunos:**
- ana.costa@aluno.com / senha123
- pedro.lima@aluno.com / senha123
- juliana.ferreira@aluno.com / senha123
- lucas.rodrigues@aluno.com / senha123
- beatriz.almeida@aluno.com / senha123

---

**✨ Configuração Completa e Pronta para Apresentação!**

