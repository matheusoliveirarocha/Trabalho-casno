package com.cassino.online.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para jogada na Roleta
 */
public class JogadaRoletaDTO {

    @NotNull(message = "O número apostado é obrigatório")
    @Min(value = 0, message = "O número deve ser entre 0 e 36")
    @Max(value = 36, message = "O número deve ser entre 0 e 36")
    private Integer numeroApostado;

    private String tipoAposta; // "NUMERO", "COR", "PAR_IMPAR"
    private String cor; // "VERMELHO", "PRETO" (para apostas de cor)
    private String parImpar; // "PAR", "IMPAR" (para apostas par/ímpar)

    // Construtores
    public JogadaRoletaDTO() {
    }

    public JogadaRoletaDTO(Integer numeroApostado) {
        this.numeroApostado = numeroApostado;
        this.tipoAposta = "NUMERO";
    }

    // Getters e Setters
    public Integer getNumeroApostado() {
        return numeroApostado;
    }

    public void setNumeroApostado(Integer numeroApostado) {
        this.numeroApostado = numeroApostado;
    }

    public String getTipoAposta() {
        return tipoAposta;
    }

    public void setTipoAposta(String tipoAposta) {
        this.tipoAposta = tipoAposta;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getParImpar() {
        return parImpar;
    }

    public void setParImpar(String parImpar) {
        this.parImpar = parImpar;
    }
}

