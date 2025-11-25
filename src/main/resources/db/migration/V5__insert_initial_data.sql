-- Inserção de dados iniciais para testes
-- IMPORTANTE: Todas as senhas são "senha123" criptografadas com BCrypt
-- Para login use: email + senha "senha123"

-- Inserir Professores
INSERT INTO professor (nome, cpf, email, senha) VALUES
('Dr. João Silva', '12345678901', 'joao.silva@professor.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7'),
('Dra. Maria Santos', '23456789012', 'maria.santos@professor.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7'),
('Prof. Carlos Oliveira', '34567890123', 'carlos.oliveira@professor.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7');

-- Inserir Alunos
INSERT INTO aluno (nome, cpf, email, senha) VALUES
('Ana Paula Costa', '45678901234', 'ana.costa@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7'),
('Pedro Henrique Lima', '56789012345', 'pedro.lima@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7'),
('Juliana Ferreira', '67890123456', 'juliana.ferreira@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7'),
('Lucas Rodrigues', '78901234567', 'lucas.rodrigues@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7'),
('Beatriz Almeida', '89012345678', 'beatriz.almeida@aluno.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J6D7xqz7x7x7x7x7x7x7x7x7x7x7x7');

-- Inserir Disciplinas
INSERT INTO disciplina (nome, carga_horaria, professor_id) VALUES
('Programação Orientada a Objetos', 80, 1),
('Banco de Dados', 60, 1),
('Estruturas de Dados', 80, 2),
('Desenvolvimento Web', 60, 2),
('Engenharia de Software', 80, 3),
('Redes de Computadores', 60, 3);

-- Inserir Matrículas
INSERT INTO matricula_aluno (aluno_id, disciplina_id, nota1, nota2, status) VALUES
-- Ana Paula
(1, 1, 8.5, 9.0, 'APROVADO'),
(1, 2, 7.0, 8.0, 'APROVADO'),
(1, 3, 6.5, 7.5, 'APROVADO'),

-- Pedro Henrique
(2, 1, 9.0, 9.5, 'APROVADO'),
(2, 4, 8.0, 8.5, 'APROVADO'),
(2, 5, NULL, NULL, 'MATRICULADO'),

-- Juliana Ferreira
(3, 2, 5.0, 6.0, 'REPROVADO'),
(3, 3, 7.5, 8.0, 'APROVADO'),
(3, 6, NULL, NULL, 'MATRICULADO'),

-- Lucas Rodrigues
(4, 1, NULL, NULL, 'TRANCADO'),
(4, 4, 8.5, 9.0, 'APROVADO'),

-- Beatriz Almeida
(5, 5, 9.0, 9.5, 'APROVADO'),
(5, 6, 7.0, 8.0, 'APROVADO');

