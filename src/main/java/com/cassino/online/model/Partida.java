package com.cassino.online.model;

import com.cassino.online.enums.TipoJogo;
import com.cassino.online.enums.StatusPartida;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA que representa uma Partida no cassino
 */
@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O nome do jogador é obrigatório")
    @Size(min = 2, max = 50, message = "O nome do jogador deve ter entre 2 e 50 caracteres")
    @Column(name = "nome_jogador", nullable = false, length = 50)
    private String nomeJogador;

    @NotNull(message = "O tipo de jogo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_jogo", nullable = false)
    private TipoJogo tipoJogo;

    @NotNull(message = "O valor da aposta é obrigatório")
    @Positive(message = "O valor da aposta deve ser positivo")
    @Column(name = "valor_aposta", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorAposta;

    @Column(name = "valor_ganho", precision = 10, scale = 2)
    private BigDecimal valorGanho = BigDecimal.ZERO;

    @NotNull(message = "O status da partida é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPartida status = StatusPartida.EM_ANDAMENTO;

    @Column(name = "resultado_jogo", columnDefinition = "TEXT")
    private String resultadoJogo;

    @Column(name = "data_inicio", nullable = false, updatable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    // Construtores
    public Partida() {
    }

    public Partida(String nomeJogador, TipoJogo tipoJogo, BigDecimal valorAposta) {
        this.nomeJogador = nomeJogador;
        this.tipoJogo = tipoJogo;
        this.valorAposta = valorAposta;
    }

    // Métodos de callback JPA
    @PrePersist
    protected void onCreate() {
        dataInicio = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (status != StatusPartida.EM_ANDAMENTO && dataFim == null) {
            dataFim = LocalDateTime.now();
        }
    }

    // Métodos de negócio
    public void finalizarPartida(StatusPartida novoStatus, BigDecimal valorGanho, String resultado) {
        this.status = novoStatus;
        this.valorGanho = valorGanho != null ? valorGanho : BigDecimal.ZERO;
        this.resultadoJogo = resultado;
        this.dataFim = LocalDateTime.now();
    }

    public BigDecimal calcularLucroLiquido() {
        return valorGanho.subtract(valorAposta);
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BigDecimal getValorGanho() {
        return valorGanho;
    }

    public void setValorGanho(BigDecimal valorGanho) {
        this.valorGanho = valorGanho;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public void setStatus(StatusPartida status) {
        this.status = status;
    }

    public String getResultadoJogo() {
        return resultadoJogo;
    }

    public void setResultadoJogo(String resultadoJogo) {
        this.resultadoJogo = resultadoJogo;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partida partida = (Partida) o;
        return Objects.equals(id, partida.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", nomeJogador='" + nomeJogador + '\'' +
                ", tipoJogo=" + tipoJogo +
                ", valorAposta=" + valorAposta +
                ", valorGanho=" + valorGanho +
                ", status=" + status +
                '}';
    }
}

