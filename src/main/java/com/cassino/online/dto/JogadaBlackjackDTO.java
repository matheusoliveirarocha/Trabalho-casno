package com.cassino.online.dto;

/**
 * DTO para jogada no Blackjack
 */
public class JogadaBlackjackDTO {

    private String acao; // "HIT", "STAND", "DOUBLE"

    // Construtores
    public JogadaBlackjackDTO() {
    }

    public JogadaBlackjackDTO(String acao) {
        this.acao = acao;
    }

    // Getters e Setters
    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
}

