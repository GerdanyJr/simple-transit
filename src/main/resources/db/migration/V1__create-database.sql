CREATE TABLE tipo_ocorrencia(
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE usuario(
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    nome VARCHAR(50) NOT NULL,
    login VARCHAR(20) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE ocorrencia(
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    resumo VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    endereco VARCHAR(100) NOT NULL,
    latitude NUMERIC,
    longitude NUMERIC,
    tipo_ocorrencia_id INTEGER NOT NULL,
    usuario_id INTEGER NOT NULL,
    FOREIGN KEY (tipo_ocorrencia_id) REFERENCES tipo_ocorrencia(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE comentario(
    id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    comentario VARCHAR(140) NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    ocorrencia_id INTEGER NOT NULL,
    usuario_id INTEGER NOT NULL,
    FOREIGN KEY (ocorrencia_id) REFERENCES ocorrencia(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);