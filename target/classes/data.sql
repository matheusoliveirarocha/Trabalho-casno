-- Dados de exemplo para desenvolvimento (H2) - Cassino Online
INSERT INTO partidas (nome_jogador, tipo_jogo, valor_aposta, valor_ganho, status, resultado_jogo, data_inicio, data_fim) VALUES
('João Silva', 'BLACKJACK', 50.00, 100.00, 'VITORIA', 'Jogador venceu: 20 vs 19', DATEADD('HOUR', -2, CURRENT_TIMESTAMP), DATEADD('MINUTE', 2, DATEADD('HOUR', -2, CURRENT_TIMESTAMP))),
('Maria Santos', 'ROLETA', 25.00, 0.00, 'DERROTA', 'Número sorteado: 15 (VERMELHO, IMPAR). Você perdeu.', DATEADD('HOUR', -1, CURRENT_TIMESTAMP), DATEADD('SECOND', 30, DATEADD('HOUR', -1, CURRENT_TIMESTAMP))),
('Pedro Costa', 'SLOT_MACHINE', 10.00, 30.00, 'VITORIA', 'Combinação vencedora: 🍒 🍒 🍒 - Multiplicador: 3x', DATEADD('MINUTE', -45, CURRENT_TIMESTAMP), DATEADD('SECOND', 5, DATEADD('MINUTE', -45, CURRENT_TIMESTAMP))),
('Ana Oliveira', 'BLACKJACK', 75.00, 0.00, 'DERROTA', 'Jogador estourou com 23 pontos. Dealer: 19', DATEADD('MINUTE', -30, CURRENT_TIMESTAMP), DATEADD('MINUTE', 2, DATEADD('MINUTE', -30, CURRENT_TIMESTAMP))),
('Carlos Ferreira', 'ROLETA', 100.00, 200.00, 'VITORIA', 'Número sorteado: 7 (VERMELHO, IMPAR). Você ganhou!', DATEADD('MINUTE', -15, CURRENT_TIMESTAMP), DATEADD('SECOND', 15, DATEADD('MINUTE', -15, CURRENT_TIMESTAMP))),
('Lucia Mendes', 'SLOT_MACHINE', 20.00, 0.00, 'DERROTA', 'Sem combinação vencedora: 🍋 🍊 🍇', DATEADD('MINUTE', -10, CURRENT_TIMESTAMP), DATEADD('SECOND', 3, DATEADD('MINUTE', -10, CURRENT_TIMESTAMP))),
('Roberto Lima', 'BLACKJACK', 30.00, 30.00, 'EMPATE', 'Empate: 20 vs 20', DATEADD('MINUTE', -5, CURRENT_TIMESTAMP), DATEADD('MINUTE', 2, DATEADD('MINUTE', -5, CURRENT_TIMESTAMP))),
('Fernanda Rocha', 'SLOT_MACHINE', 15.00, 150.00, 'VITORIA', 'Combinação vencedora: ⭐ ⭐ ⭐ - Multiplicador: 10x', DATEADD('MINUTE', -3, CURRENT_TIMESTAMP), DATEADD('SECOND', 4, DATEADD('MINUTE', -3, CURRENT_TIMESTAMP))),
('Marcos Alves', 'ROLETA', 50.00, 0.00, 'DERROTA', 'Número sorteado: 0 (VERDE). Você perdeu.', DATEADD('MINUTE', -2, CURRENT_TIMESTAMP), DATEADD('SECOND', 10, DATEADD('MINUTE', -2, CURRENT_TIMESTAMP))),
('Carla Dias', 'BLACKJACK', 40.00, 80.00, 'VITORIA', 'Blackjack natural! Jogador: 21', DATEADD('MINUTE', -1, CURRENT_TIMESTAMP), DATEADD('SECOND', 30, DATEADD('MINUTE', -1, CURRENT_TIMESTAMP)));

