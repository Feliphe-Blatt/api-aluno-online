-- Criação da tabela aluno
CREATE TABLE aluno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhorar performance
CREATE INDEX idx_aluno_cpf ON aluno(cpf);
CREATE INDEX idx_aluno_email ON aluno(email);

