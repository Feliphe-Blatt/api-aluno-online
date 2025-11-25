-- Criação da tabela disciplina
CREATE TABLE disciplina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    carga_horaria INT,
    professor_id BIGINT,
    CONSTRAINT fk_disciplina_professor FOREIGN KEY (professor_id)
        REFERENCES professor(id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhorar performance
CREATE INDEX idx_disciplina_professor ON disciplina(professor_id);
CREATE INDEX idx_disciplina_nome ON disciplina(nome);

