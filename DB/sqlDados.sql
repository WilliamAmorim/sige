-- Extensão para hash de senha
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Tipos ENUM
CREATE TYPE tipo_usuario AS ENUM ('ADMIN', 'PROFESSOR');
CREATE TYPE status_aluno AS ENUM ('ATIVO', 'INATIVO');

-- Tabela de usuários
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(50) UNIQUE NOT NULL,
    senha TEXT NOT NULL,
    tipo tipo_usuario NOT NULL
);

-- Usuário admin inicial
INSERT INTO "user" (nome, cpf, senha, tipo)
VALUES 
('Administrador', '1234', crypt('admin123', gen_salt('bf')), 'ADMIN'),
('Carlos Silva', '11111111111', crypt('prof123', gen_salt('bf')), 'PROFESSOR'),
('Maria Souza', '22222222222', crypt('prof456', gen_salt('bf')), 'PROFESSOR');

-- Tabela de disciplinas
CREATE TABLE disciplina (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL
);

INSERT INTO disciplina (nome, carga_horaria)
VALUES 
('Matemática', 80),
('Português', 60),
('História', 40),
('Biologia', 50);

-- Tabela de turmas
CREATE TABLE turma (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    turno VARCHAR(20),
    ano INT
);

INSERT INTO turma (codigo, nome, turno, ano)
VALUES 
('1A2025', '1º Ano A', 'Manhã', 2025),
('2B2025', '2º Ano B', 'Tarde', 2025);

-- Relacionamento N:N turma ↔ disciplina
CREATE TABLE turma_disciplina (
    turma_id INT,
    disciplina_id INT,
    PRIMARY KEY (turma_id, disciplina_id),
    FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
);

INSERT INTO turma_disciplina (turma_id, disciplina_id)
VALUES 
(1, 1), (1, 2),
(2, 3), (2, 4);

-- Tabela de alunos
CREATE TABLE aluno (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(20) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    status status_aluno DEFAULT 'ATIVO',
    turma_id INT,
    FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE SET NULL
);

INSERT INTO aluno (nome, matricula, data_nascimento, status, turma_id)
VALUES 
('Ana Lima', 'MAT001', '2010-05-12', 'ATIVO', 1),
('João Pedro', 'MAT002', '2010-07-23', 'ATIVO', 1),
('Larissa Costa', 'MAT003', '2009-02-18', 'ATIVO', 2),
('Bruno Rocha', 'MAT004', '2009-09-30', 'INATIVO', 2);

-- Tabela de notas
CREATE TABLE nota (
    id SERIAL PRIMARY KEY,
    aluno_id INT,
    disciplina_id INT,
    bimestre INT,
    valor DECIMAL(5,2),
    FOREIGN KEY (aluno_id) REFERENCES aluno(id),
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id)
);

INSERT INTO nota (aluno_id, disciplina_id, bimestre, valor)
VALUES 
(1, 1, 1, 8.5), (1, 2, 1, 9.0),
(2, 1, 1, 7.0), (2, 2, 1, 7.5),
(3, 3, 1, 8.0), (3, 4, 1, 8.5),
(4, 3, 1, 6.0), (4, 4, 1, 6.5);

-- Tabela de frequência
CREATE TABLE frequencia (
    id SERIAL PRIMARY KEY,
    aluno_id INT,
    disciplina_id INT,
    bimestre INT,
    faltas INT,
    FOREIGN KEY (aluno_id) REFERENCES aluno(id),
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id)
);

INSERT INTO frequencia (aluno_id, disciplina_id, bimestre, faltas)
VALUES 
(1, 1, 1, 2), (1, 2, 1, 0),
(2, 1, 1, 1), (2, 2, 1, 3),
(3, 3, 1, 0), (3, 4, 1, 1),
(4, 3, 1, 5), (4, 4, 1, 4);


-- Carlos Silva leciona Matemática para o 1º Ano A
INSERT INTO professor_turma_disciplina (professor_id, turma_id, disciplina_id)
VALUES (2, 1, 1);

-- Maria Souza leciona Português para o 1º Ano A
INSERT INTO professor_turma_disciplina (professor_id, turma_id, disciplina_id)
VALUES (3, 1, 2);

-- Maria Souza leciona História para o 2º Ano B
INSERT INTO professor_turma_disciplina (professor_id, turma_id, disciplina_id)
VALUES (3, 2, 3);
