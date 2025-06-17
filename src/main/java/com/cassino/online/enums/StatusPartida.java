package com.cassino.online.enums;

/**
 * Enum para o status de uma partida
 */
public enum StatusPartida {
    EM_ANDAMENTO("Em Andamento"),
    VITORIA("Vitória"),
    DERROTA("Derrota"),
    EMPATE("Empate"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusPartida(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

