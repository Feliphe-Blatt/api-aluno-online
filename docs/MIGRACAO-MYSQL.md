# Guia de Migração e População do Banco de Dados MySQL

## Pré-requisitos

1. MySQL Server instalado e rodando
2. Usuário root com senha 'root' (ou ajuste as credenciais no application.properties)
3. Maven instalado

## Passo 1: Verificar MySQL

Certifique-se de que o MySQL está rodando:

```cmd
mysql --version
```

## Passo 2: Iniciar a Aplicação (Primeira Vez)

A aplicação irá criar automaticamente o banco de dados e as tabelas:

```cmd
mvnw spring-boot:run
```

Aguarde até ver a mensagem "Started AlunoOnlineApplication" e depois pare a aplicação (Ctrl+C).

## Passo 3: Popular o Banco de Dados

Execute o script SQL para inserir dados de teste:

```cmd
mysql -u root -p aluno_online < docs\populate_database.sql
```

Digite a senha quando solicitado (padrão: root).

**Alternativa**: Use uma ferramenta gráfica como MySQL Workbench, DBeaver ou phpMyAdmin para executar o arquivo `docs/populate_database.sql`.

## Passo 4: Verificar os Dados

Conecte-se ao MySQL e verifique:

```cmd
mysql -u root -p
```

Depois execute:

```sql
USE aluno_online;
SHOW TABLES;
SELECT COUNT(*) FROM aluno;
SELECT COUNT(*) FROM professor;
SELECT COUNT(*) FROM disciplina;
SELECT COUNT(*) FROM matricula_aluno;
```

## Passo 5: Reiniciar a Aplicação

```cmd
mvnw spring-boot:run
```

## Dados de Teste Inseridos

### Professores (5)
- Dr. João Silva - joao.silva@escola.com
- Dra. Maria Santos - maria.santos@escola.com
- Prof. Carlos Oliveira - carlos.oliveira@escola.com
- Profa. Ana Costa - ana.costa@escola.com
- Prof. Pedro Almeida - pedro.almeida@escola.com

### Alunos (8)
- Lucas Ferreira - lucas.ferreira@aluno.com
- Juliana Souza - juliana.souza@aluno.com
- Rafael Lima - rafael.lima@aluno.com
- Camila Rodrigues - camila.rodrigues@aluno.com
- Felipe Martins - felipe.martins@aluno.com
- Beatriz Alves - beatriz.alves@aluno.com
- Gustavo Pereira - gustavo.pereira@aluno.com
- Larissa Mendes - larissa.mendes@aluno.com

### Senha padrão para todos os usuários
**senha123** (já criptografada no banco)

### Disciplinas (10)
- Matemática I, Física I, Programação I, Banco de Dados, Inglês Técnico, etc.

### Cenários de Teste Incluídos

1. **Alunos Aprovados**: Com notas acima de 7.0
2. **Alunos Reprovados**: Com média abaixo de 7.0
3. **Alunos Matriculados**: Sem notas ainda
4. **Notas Parciais**: Apenas nota1 preenchida
5. **Matrículas Trancadas**: Status TRANCADO
6. **Múltiplas Situações**: Mix de todos os cenários

## Testando a API

Após popular o banco, você pode testar os endpoints:

### Login (use com Insomnia/Postman)

**POST** http://localhost:8080/auth/login

```json
{
  "email": "lucas.ferreira@aluno.com",
  "senha": "senha123",
  "tipoUsuario": "ALUNO"
}
```

ou

```json
{
  "email": "joao.silva@escola.com",
  "senha": "senha123",
  "tipoUsuario": "PROFESSOR"
}
```

### Histórico de Aluno

**GET** http://localhost:8080/matriculas/historico-aluno/1

(Use o token JWT no header Authorization)

## Solução de Problemas

### Erro de conexão com MySQL
- Verifique se o MySQL está rodando
- Confirme usuário e senha no application.properties
- Verifique a porta (padrão: 3306)

### Tabelas não criadas
- Execute a aplicação pelo menos uma vez antes de popular
- Verifique se há erros no console

### Erro de chave estrangeira
- Execute o script completo do início
- Certifique-se de que FOREIGN_KEY_CHECKS está configurado corretamente

## Configuração Atual

- **Banco de Dados**: aluno_online
- **Host**: localhost:3306
- **Usuário**: root
- **Senha**: root
- **Dialect**: MySQLDialect
- **DDL**: update (cria/atualiza tabelas automaticamente)

## Mudanças Realizadas

✅ Application.properties configurado para MySQL
✅ Driver JDBC MySQL já estava no pom.xml
✅ Script SQL de população criado com dados de teste
✅ Senhas criptografadas com BCrypt
✅ Múltiplos cenários de teste incluídos

