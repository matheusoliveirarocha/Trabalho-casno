package com.cassino.online.enums;

/**
 * Enum para os tipos de jogos disponíveis no cassino
 */
public enum TipoJogo {
    BLACKJACK("Blackjack", "Jogo de cartas onde o objetivo é chegar o mais próximo possível de 21"),
    ROLETA("Roleta", "Jogo de azar com uma roda numerada de 0 a 36"),
    SLOT_MACHINE("Slot Machine", "Máquina caça-níqueis com símbolos e combinações premiadas");

    private final String nome;
    private final String descricao;

    TipoJogo(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}

