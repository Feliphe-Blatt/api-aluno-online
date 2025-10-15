# 🚀 Setup Rápido - MySQL

## Opção 1: Automática (Recomendado)

Execute o script batch:
```cmd
setup-mysql.bat
```

## Opção 2: Manual

### 1. Compile o projeto
```cmd
mvnw clean package -DskipTests
```

### 2. Inicie a aplicação (para criar as tabelas)
```cmd
mvnw spring-boot:run
```
Aguarde iniciar completamente e depois pare (Ctrl+C)

### 3. Execute o script SQL
```cmd
mysql -u root -p aluno_online < docs\populate_database.sql
```

### 4. Reinicie a aplicação
```cmd
mvnw spring-boot:run
```

## 📊 Dados de Teste

### Alunos (8 cadastrados)
- **Email**: lucas.ferreira@aluno.com | **Senha**: senha123
- **Email**: juliana.souza@aluno.com | **Senha**: senha123
- **Email**: rafael.lima@aluno.com | **Senha**: senha123

### Professores (5 cadastrados)
- **Email**: joao.silva@escola.com | **Senha**: senha123
- **Email**: maria.santos@escola.com | **Senha**: senha123
- **Email**: carlos.oliveira@escola.com | **Senha**: senha123

### Dados incluídos:
- ✅ 8 Alunos
- ✅ 5 Professores
- ✅ 10 Disciplinas
- ✅ 27 Matrículas (com diferentes status: APROVADO, REPROVADO, MATRICULADO, TRANCADO)

## 🔐 Testando Login

**POST** `http://localhost:8080/auth/login`
```json
{
  "email": "lucas.ferreira@aluno.com",
  "senha": "senha123",
  "tipoUsuario": "ALUNO"
}
```

## 📝 Configuração Atual

- **Banco**: MySQL 
- **Host**: localhost:3306
- **Database**: aluno_online
- **User**: root
- **Password**: root

Para alterar, edite: `src/main/resources/application.properties`

## 📚 Documentação Completa

Ver arquivo: `docs/MIGRACAO-MYSQL.md`

