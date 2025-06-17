-- Script SQL para criação da base de dados em produção (MySQL) - Cassino Online
-- Este script é específico para MySQL e não deve ser executado no H2

-- CREATE DATABASE IF NOT EXISTS cassino_online 
-- CHARACTER SET utf8mb4 
-- COLLATE utf8mb4_unicode_ci;

-- USE cassino_online;

-- Criação da tabela de partidas (compatível com H2 e MySQL)
CREATE TABLE IF NOT EXISTS partidas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_jogador VARCHAR(50) NOT NULL,
    tipo_jogo VARCHAR(20) NOT NULL,
    valor_aposta DECIMAL(10,2) NOT NULL,
    valor_ganho DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'EM_ANDAMENTO',
    resultado_jogo TEXT,
    data_inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_fim TIMESTAMP
);

