-- Script para criar e popular o banco de dados MySQL com dados de teste
-- Execute este script em um banco vazio

USE p4_back_2025;

-- Criar tabela de Professores
CREATE TABLE IF NOT EXISTS professor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

-- Criar tabela de Alunos
CREATE TABLE IF NOT EXISTS aluno (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

-- Criar tabela de Disciplinas
CREATE TABLE IF NOT EXISTS disciplina (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL,
    professor_id INT NOT NULL,
    FOREIGN KEY (professor_id) REFERENCES professor(id)
);

-- Criar tabela de Matrículas de Aluno
CREATE TABLE IF NOT EXISTS matricula_aluno (
    id INT AUTO_INCREMENT PRIMARY KEY,
    aluno_id INT NOT NULL,
    disciplina_id INT NOT NULL,
    nota1 DOUBLE,
    nota2 DOUBLE,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (aluno_id) REFERENCES aluno(id),
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id)
);

-- Limpar dados existentes (se houver)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE matricula_aluno;
TRUNCATE TABLE disciplina;
TRUNCATE TABLE professor;
TRUNCATE TABLE aluno;
SET FOREIGN_KEY_CHECKS = 1;

-- Inserir Professores
-- Senha padrão: senha123 (será criptografada pela aplicação no registro)
INSERT INTO professor (id, nome, cpf, email, senha) VALUES
(1, 'Dr. João Silva', '12345678901', 'joao.silva@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(2, 'Dra. Maria Santos', '23456789012', 'maria.santos@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(3, 'Prof. Carlos Oliveira', '34567890123', 'carlos.oliveira@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(4, 'Profa. Ana Costa', '45678901234', 'ana.costa@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(5, 'Prof. Pedro Almeida', '56789012345', 'pedro.almeida@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Inserir Alunos
-- Senha padrão: senha123 (será criptografada pela aplicação no registro)
INSERT INTO aluno (id, nome, cpf, email, senha) VALUES
(1, 'Lucas Ferreira', '11122233344', 'lucas.ferreira@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(2, 'Juliana Souza', '22233344455', 'juliana.souza@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(3, 'Rafael Lima', '33344455566', 'rafael.lima@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(4, 'Camila Rodrigues', '44455566677', 'camila.rodrigues@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(5, 'Felipe Martins', '55566677788', 'felipe.martins@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(6, 'Beatriz Alves', '66677788899', 'beatriz.alves@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(7, 'Gustavo Pereira', '77788899900', 'gustavo.pereira@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(8, 'Larissa Mendes', '88899900011', 'larissa.mendes@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Inserir Disciplinas
INSERT INTO disciplina (id, nome, carga_horaria, professor_id) VALUES
(1, 'Matemática I', 80, 1),
(2, 'Física I', 80, 2),
(3, 'Programação I', 120, 3),
(4, 'Banco de Dados', 100, 3),
(5, 'Inglês Técnico', 60, 4),
(6, 'Estrutura de Dados', 100, 3),
(7, 'Cálculo I', 80, 1),
(8, 'Química Geral', 80, 5),
(9, 'Algoritmos', 100, 3),
(10, 'Redes de Computadores', 80, 2);

-- Inserir Matrículas com diferentes cenários
-- Cenário 1: Alunos aprovados
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(1, 1, 1, 8.5, 9.0, 'APROVADO'),
(2, 1, 2, 7.5, 8.0, 'APROVADO'),
(3, 1, 3, 9.0, 9.5, 'APROVADO');

-- Cenário 2: Alunos reprovados
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(4, 2, 1, 5.0, 6.0, 'REPROVADO'),
(5, 2, 2, 6.5, 6.0, 'REPROVADO'),
(6, 2, 4, 7.0, 8.0, 'APROVADO');

-- Cenário 3: Alunos matriculados (sem notas ainda)
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(7, 3, 3, NULL, NULL, 'MATRICULADO'),
(8, 3, 5, NULL, NULL, 'MATRICULADO'),
(9, 3, 6, NULL, NULL, 'MATRICULADO');

-- Cenário 4: Alunos com nota parcial (apenas nota1)
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(10, 4, 1, 8.0, NULL, 'MATRICULADO'),
(11, 4, 3, 7.5, NULL, 'MATRICULADO'),
(12, 4, 7, 9.0, NULL, 'MATRICULADO');

-- Cenário 5: Matrículas trancadas
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(13, 5, 2, NULL, NULL, 'TRANCADO'),
(14, 5, 4, 8.0, 8.5, 'APROVADO'),
(15, 5, 8, NULL, NULL, 'TRANCADO');

-- Cenário 6: Mix de situações
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(16, 6, 3, 10.0, 9.5, 'APROVADO'),
(17, 6, 4, 9.0, 8.5, 'APROVADO'),
(18, 6, 6, 8.5, 9.0, 'APROVADO'),
(19, 6, 9, 7.0, 7.5, 'APROVADO');

-- Cenário 7: Aluno com múltiplas disciplinas
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(20, 7, 1, 6.0, 5.5, 'REPROVADO'),
(21, 7, 3, 8.0, 7.5, 'APROVADO'),
(22, 7, 5, NULL, NULL, 'MATRICULADO'),
(23, 7, 7, 7.0, NULL, 'MATRICULADO');

-- Cenário 8: Aluno novo (só matriculado)
INSERT INTO matricula_aluno (id, aluno_id, disciplina_id, nota1, nota2, status) VALUES
(24, 8, 1, NULL, NULL, 'MATRICULADO'),
(25, 8, 2, NULL, NULL, 'MATRICULADO'),
(26, 8, 3, NULL, NULL, 'MATRICULADO'),
(27, 8, 9, NULL, NULL, 'MATRICULADO');

-- Resetar auto_increment para os próximos IDs
ALTER TABLE professor AUTO_INCREMENT = 6;
ALTER TABLE aluno AUTO_INCREMENT = 9;
ALTER TABLE disciplina AUTO_INCREMENT = 11;
ALTER TABLE matricula_aluno AUTO_INCREMENT = 28;

-- Verificar dados inseridos
SELECT 'Professores cadastrados:' as '';
SELECT COUNT(*) as total FROM professor;

SELECT 'Alunos cadastrados:' as '';
SELECT COUNT(*) as total FROM aluno;

SELECT 'Disciplinas cadastradas:' as '';
SELECT COUNT(*) as total FROM disciplina;

SELECT 'Matrículas cadastradas:' as '';
SELECT COUNT(*) as total FROM matricula_aluno;

SELECT 'Distribuição de status das matrículas:' as '';
SELECT status, COUNT(*) as quantidade FROM matricula_aluno GROUP BY status;

