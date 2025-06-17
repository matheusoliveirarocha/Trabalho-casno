package com.cassino.online.service;

import com.cassino.online.dto.JogadaBlackjackDTO;
import com.cassino.online.dto.ResultadoPartidaDTO;
import com.cassino.online.enums.StatusPartida;
import com.cassino.online.model.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Serviço para o jogo Blackjack
 */
@Service
public class BlackjackService {

    private final PartidaService partidaService;
    private final Random random = new Random();

    @Autowired
    public BlackjackService(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    /**
     * Inicia uma partida de Blackjack
     */
    public EstadoBlackjack iniciarBlackjack(Long partidaId) {
        Partida partida = partidaService.buscarPartidaPorId(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        // Distribui cartas iniciais
        List<Integer> cartasJogador = new ArrayList<>();
        List<Integer> cartasDealer = new ArrayList<>();

        cartasJogador.add(sortearCarta());
        cartasJogador.add(sortearCarta());
        cartasDealer.add(sortearCarta());
        cartasDealer.add(sortearCarta()); // Segunda carta do dealer fica oculta

        int pontuacaoJogador = calcularPontuacao(cartasJogador);
        int pontuacaoDealer = cartasDealer.get(0); // Apenas primeira carta visível

        EstadoBlackjack estado = new EstadoBlackjack(
                partidaId,
                cartasJogador,
                cartasDealer,
                pontuacaoJogador,
                pontuacaoDealer,
                false
        );

        // Verifica blackjack natural
        if (pontuacaoJogador == 21) {
            return finalizarBlackjack(partidaId, "STAND");
        }

        return estado;
    }

    /**
     * Processa uma jogada no Blackjack
     */
    public EstadoBlackjack jogarBlackjack(Long partidaId, JogadaBlackjackDTO jogada) {
        Partida partida = partidaService.buscarPartidaPorId(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        // Simula estado atual (em uma implementação real, isso seria persistido)
        EstadoBlackjack estadoAtual = recuperarEstadoBlackjack(partidaId);

        switch (jogada.getAcao().toUpperCase()) {
            case "HIT":
                return hit(estadoAtual);
            case "STAND":
                return finalizarBlackjack(partidaId, "STAND");
            case "DOUBLE":
                return doubleDown(estadoAtual);
            default:
                throw new IllegalArgumentException("Ação inválida: " + jogada.getAcao());
        }
    }

    /**
     * Pede mais uma carta (HIT)
     */
    private EstadoBlackjack hit(EstadoBlackjack estado) {
        estado.getCartasJogador().add(sortearCarta());
        int novaPontuacao = calcularPontuacao(estado.getCartasJogador());
        estado.setPontuacaoJogador(novaPontuacao);

        // Verifica se estourou
        if (novaPontuacao > 21) {
            return finalizarBlackjack(estado.getPartidaId(), "BUST");
        }

        return estado;
    }

    /**
     * Dobra a aposta e pede uma carta
     */
    private EstadoBlackjack doubleDown(EstadoBlackjack estado) {
        estado.getCartasJogador().add(sortearCarta());
        int novaPontuacao = calcularPontuacao(estado.getCartasJogador());
        estado.setPontuacaoJogador(novaPontuacao);

        return finalizarBlackjack(estado.getPartidaId(), "DOUBLE");
    }

    /**
     * Finaliza a partida de Blackjack
     */
    private EstadoBlackjack finalizarBlackjack(Long partidaId, String acao) {
        Partida partida = partidaService.buscarPartidaPorId(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        EstadoBlackjack estado = recuperarEstadoBlackjack(partidaId);
        
        // Revela segunda carta do dealer e joga até 17
        int pontuacaoDealer = calcularPontuacao(estado.getCartasDealer());
        while (pontuacaoDealer < 17) {
            estado.getCartasDealer().add(sortearCarta());
            pontuacaoDealer = calcularPontuacao(estado.getCartasDealer());
        }
        estado.setPontuacaoDealer(pontuacaoDealer);
        estado.setFinalizada(true);

        // Determina resultado
        int pontuacaoJogador = estado.getPontuacaoJogador();
        StatusPartida status;
        BigDecimal valorGanho = BigDecimal.ZERO;
        String resultado;

        if (acao.equals("BUST") || pontuacaoJogador > 21) {
            status = StatusPartida.DERROTA;
            resultado = String.format("Jogador estourou com %d pontos. Dealer: %d", pontuacaoJogador, pontuacaoDealer);
        } else if (pontuacaoDealer > 21) {
            status = StatusPartida.VITORIA;
            valorGanho = acao.equals("DOUBLE") ? partida.getValorAposta().multiply(BigDecimal.valueOf(4)) : partida.getValorAposta().multiply(BigDecimal.valueOf(2));
            resultado = String.format("Dealer estourou com %d pontos. Jogador: %d", pontuacaoDealer, pontuacaoJogador);
        } else if (pontuacaoJogador > pontuacaoDealer) {
            status = StatusPartida.VITORIA;
            valorGanho = acao.equals("DOUBLE") ? partida.getValorAposta().multiply(BigDecimal.valueOf(4)) : partida.getValorAposta().multiply(BigDecimal.valueOf(2));
            resultado = String.format("Jogador venceu: %d vs %d", pontuacaoJogador, pontuacaoDealer);
        } else if (pontuacaoJogador < pontuacaoDealer) {
            status = StatusPartida.DERROTA;
            resultado = String.format("Dealer venceu: %d vs %d", pontuacaoDealer, pontuacaoJogador);
        } else {
            status = StatusPartida.EMPATE;
            valorGanho = partida.getValorAposta(); // Devolve a aposta
            resultado = String.format("Empate: %d vs %d", pontuacaoJogador, pontuacaoDealer);
        }

        partidaService.finalizarPartida(partidaId, status, valorGanho, resultado);
        return estado;
    }

    /**
     * Sorteia uma carta (1-11, onde 1 é Ás e pode valer 11)
     */
    private int sortearCarta() {
        int carta = random.nextInt(13) + 1;
        return Math.min(carta, 10); // J, Q, K valem 10
    }

    /**
     * Calcula a pontuação das cartas
     */
    private int calcularPontuacao(List<Integer> cartas) {
        int pontuacao = 0;
        int ases = 0;

        for (int carta : cartas) {
            if (carta == 1) {
                ases++;
                pontuacao += 11;
            } else {
                pontuacao += carta;
            }
        }

        // Ajusta Ases de 11 para 1 se necessário
        while (pontuacao > 21 && ases > 0) {
            pontuacao -= 10;
            ases--;
        }

        return pontuacao;
    }

    /**
     * Recupera o estado atual do Blackjack (simulado)
     */
    private EstadoBlackjack recuperarEstadoBlackjack(Long partidaId) {
        // Em uma implementação real, isso seria recuperado do banco de dados ou cache
        // Por simplicidade, vamos simular um estado
        List<Integer> cartasJogador = new ArrayList<>();
        List<Integer> cartasDealer = new ArrayList<>();
        
        cartasJogador.add(sortearCarta());
        cartasJogador.add(sortearCarta());
        cartasDealer.add(sortearCarta());
        cartasDealer.add(sortearCarta());

        return new EstadoBlackjack(
                partidaId,
                cartasJogador,
                cartasDealer,
                calcularPontuacao(cartasJogador),
                cartasDealer.get(0),
                false
        );
    }

    /**
     * Classe para representar o estado do Blackjack
     */
    public static class EstadoBlackjack {
        private Long partidaId;
        private List<Integer> cartasJogador;
        private List<Integer> cartasDealer;
        private int pontuacaoJogador;
        private int pontuacaoDealer;
        private boolean finalizada;

        public EstadoBlackjack(Long partidaId, List<Integer> cartasJogador, List<Integer> cartasDealer, 
                              int pontuacaoJogador, int pontuacaoDealer, boolean finalizada) {
            this.partidaId = partidaId;
            this.cartasJogador = cartasJogador;
            this.cartasDealer = cartasDealer;
            this.pontuacaoJogador = pontuacaoJogador;
            this.pontuacaoDealer = pontuacaoDealer;
            this.finalizada = finalizada;
        }

        // Getters e Setters
        public Long getPartidaId() { return partidaId; }
        public List<Integer> getCartasJogador() { return cartasJogador; }
        public List<Integer> getCartasDealer() { return cartasDealer; }
        public int getPontuacaoJogador() { return pontuacaoJogador; }
        public void setPontuacaoJogador(int pontuacaoJogador) { this.pontuacaoJogador = pontuacaoJogador; }
        public int getPontuacaoDealer() { return pontuacaoDealer; }
        public void setPontuacaoDealer(int pontuacaoDealer) { this.pontuacaoDealer = pontuacaoDealer; }
        public boolean isFinalizada() { return finalizada; }
        public void setFinalizada(boolean finalizada) { this.finalizada = finalizada; }
    }
}

