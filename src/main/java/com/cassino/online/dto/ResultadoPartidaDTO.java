package com.cassino.online.dto;

/**
 * DTO para resultado de uma partida
 */
public class ResultadoPartidaDTO {

    private Long partidaId;
    private String status;
    private String resultado;
    private String valorGanho;
    private String lucroLiquido;
    private String mensagem;

    // Construtores
    public ResultadoPartidaDTO() {
    }

    public ResultadoPartidaDTO(Long partidaId, String status, String resultado, String valorGanho, String lucroLiquido, String mensagem) {
        this.partidaId = partidaId;
        this.status = status;
        this.resultado = resultado;
        this.valorGanho = valorGanho;
        this.lucroLiquido = lucroLiquido;
        this.mensagem = mensagem;
    }

    // Getters e Setters
    public Long getPartidaId() {
        return partidaId;
    }

    public void setPartidaId(Long partidaId) {
        this.partidaId = partidaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getValorGanho() {
        return valorGanho;
    }

    public void setValorGanho(String valorGanho) {
        this.valorGanho = valorGanho;
    }

    public String getLucroLiquido() {
        return lucroLiquido;
    }

    public void setLucroLiquido(String lucroLiquido) {
        this.lucroLiquido = lucroLiquido;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

