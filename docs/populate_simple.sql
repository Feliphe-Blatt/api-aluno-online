-- Script Simplificado para Popular Banco MySQL
-- Este script deve ser executado APÓS a aplicação criar as tabelas

USE aluno_online;

-- Inserir Professores (senha já criptografada: senha123)
INSERT INTO professor (nome, cpf, email, senha) VALUES
('Dr. João Silva', '12345678901', 'joao.silva@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('Dra. Maria Santos', '23456789012', 'maria.santos@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('Prof. Carlos Oliveira', '34567890123', 'carlos.oliveira@escola.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Inserir Alunos (senha já criptografada: senha123)
INSERT INTO aluno (nome, cpf, email, senha) VALUES
('Lucas Ferreira', '11122233344', 'lucas.ferreira@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('Juliana Souza', '22233344455', 'juliana.souza@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('Rafael Lima', '33344455566', 'rafael.lima@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Inserir Disciplinas
INSERT INTO disciplina (nome, carga_horaria, professor_id) VALUES
('Matemática I', 80, 1),
('Programação I', 120, 3),
('Banco de Dados', 100, 3);

-- Inserir Matrículas (diversos cenários)
INSERT INTO matricula_aluno (aluno_id, disciplina_id, nota1, nota2, status) VALUES
(1, 1, 8.5, 9.0, 'APROVADO'),
(1, 2, 7.5, 8.0, 'APROVADO'),
(2, 1, 5.0, 6.0, 'REPROVADO'),
(3, 2, NULL, NULL, 'MATRICULADO');

SELECT 'Dados inseridos com sucesso!' as mensagem;

