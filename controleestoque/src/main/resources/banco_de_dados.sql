CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    nivel_acesso VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(255) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    modelo VARCHAR(255) NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    preco DOUBLE PRECISION NOT NULL
);

CREATE TABLE movimentacoes (
    id SERIAL PRIMARY KEY,
    produto_id INT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    quantidade INT NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT, 
    FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) 
);

CREATE TABLE notificacoes (
    id SERIAL PRIMARY KEY,
    mensagem VARCHAR(500) NOT NULL,
    lida BOOLEAN DEFAULT FALSE
);

