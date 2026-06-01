CREATE DATABASE IF NOT EXISTS gerenciamento;
USE gerenciamento;

CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categorias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome_categoria VARCHAR(50) NOT NULL,
    usuario_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE tarefas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    status ENUM('PENDENTE', 'CONCLUIDO', 'EM_ATRASO') DEFAULT 'PENDENTE',
    prioridade VARCHAR(20),
    prazo DATETIME,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT NOT NULL,
    categoria_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE lembretes (
    iid INT PRIMARY KEY AUTO_INCREMENT,
    tarefa_id INT NOT NULL,
    data_hora_lembrete DATETIME NOT NULL,
    enviado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (tarefa_id) REFERENCES tarefas(id)
);