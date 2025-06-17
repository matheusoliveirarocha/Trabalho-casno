package com.cassino.online.dto;

import com.cassino.online.enums.TipoJogo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para iniciar uma nova partida
 */
public class IniciarPartidaDTO {

    @NotBlank(message = "O nome do jogador é obrigatório")
    @Size(min = 2, max = 50, message = "O nome do jogador deve ter entre 2 e 50 caracteres")
    private String nomeJogador;

    @NotNull(message = "O tipo de jogo é obrigatório")
    private TipoJogo tipoJogo;

    @NotNull(message = "O valor da aposta é obrigatório")
    @Positive(message = "O valor da aposta deve ser positivo")
    private BigDecimal valorAposta;

    // Construtores
    public IniciarPartidaDTO() {
    }

    public IniciarPartidaDTO(String nomeJogador, TipoJogo tipoJogo, BigDecimal valorAposta) {
        this.nomeJogador = nomeJogador;
        this.tipoJogo = tipoJogo;
        this.valorAposta = valorAposta;
    }

    // Getters e Setters
    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public TipoJogo getTipoJogo() {
        return tipoJogo;
    }

    public void setTipoJogo(TipoJogo tipoJogo) {
        this.tipoJogo = tipoJogo;
    }

    public BigDecimal getValorAposta() {
        return valorAposta;
    }

    public void setValorAposta(BigDecimal valorAposta) {
        this.valorAposta = valorAposta;
    }
}

