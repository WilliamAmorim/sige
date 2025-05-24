-- Extensão para hash de senha
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Tipos ENUM
CREATE TYPE tipo_usuario AS ENUM ('ADMIN', 'PROFESSOR');
CREATE TYPE status_aluno AS ENUM ('ATIVO', 'INATIVO');

-- Tabela de usuários
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha TEXT NOT NULL,
    tipo tipo_usuario NOT NULL
);

-- Usuário admin inicial (senha criptografada)
INSERT INTO "user" (nome, login, senha, tipo)
VALUES ('Administrador', 'admin', crypt('admin123', gen_salt('bf')), 'ADMIN');

-- Tabela de disciplinas
CREATE TABLE disciplina (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL
);

-- Tabela de turmas
CREATE TABLE turma (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    turno VARCHAR(20),
    ano INT
);

-- Relacionamento N:N turma ↔ disciplina
CREATE TABLE turma_disciplina (
    turma_id INT,
    disciplina_id INT,
    PRIMARY KEY (turma_id, disciplina_id),
    FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
    FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
);

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
