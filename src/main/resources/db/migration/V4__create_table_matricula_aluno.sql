-- Criação da tabela matricula_aluno
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Índices para melhorar performance
CREATE INDEX idx_matricula_aluno ON matricula_aluno(aluno_id);
CREATE INDEX idx_matricula_disciplina ON matricula_aluno(disciplina_id);
CREATE INDEX idx_matricula_status ON matricula_aluno(status);

