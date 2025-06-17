package com.cassino.online.repository;

import com.cassino.online.enums.StatusPartida;
import com.cassino.online.enums.TipoJogo;
import com.cassino.online.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório JPA para a entidade Partida
 */
@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

    /**
     * Busca partidas por nome do jogador
     */
    List<Partida> findByNomeJogadorContainingIgnoreCase(String nomeJogador);

    /**
     * Busca partidas por tipo de jogo
     */
    List<Partida> findByTipoJogo(TipoJogo tipoJogo);

    /**
     * Busca partidas por status
     */
    List<Partida> findByStatus(StatusPartida status);

    /**
     * Busca partidas em andamento de um jogador
     */
    List<Partida> findByNomeJogadorAndStatus(String nomeJogador, StatusPartida status);

    /**
     * Busca partidas finalizadas de um jogador
     */
    @Query("SELECT p FROM Partida p WHERE p.nomeJogador = :nomeJogador AND p.status != :status ORDER BY p.dataFim DESC")
    List<Partida> findPartidasFinalizada(@Param("nomeJogador") String nomeJogador, @Param("status") StatusPartida status);

    /**
     * Busca partidas por período
     */
    @Query("SELECT p FROM Partida p WHERE p.dataInicio BETWEEN :dataInicio AND :dataFim ORDER BY p.dataInicio DESC")
    List<Partida> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    /**
     * Calcula total de apostas por tipo de jogo
     */
    @Query("SELECT SUM(p.valorAposta) FROM Partida p WHERE p.tipoJogo = :tipoJogo")
    BigDecimal calcularTotalApostasPorTipo(@Param("tipoJogo") TipoJogo tipoJogo);

    /**
     * Calcula total de ganhos por tipo de jogo
     */
    @Query("SELECT SUM(p.valorGanho) FROM Partida p WHERE p.tipoJogo = :tipoJogo")
    BigDecimal calcularTotalGanhosPorTipo(@Param("tipoJogo") TipoJogo tipoJogo);

    /**
     * Conta vitórias por jogador
     */
    @Query("SELECT COUNT(p) FROM Partida p WHERE p.nomeJogador = :nomeJogador AND p.status = :status")
    Long contarVitoriasPorJogador(@Param("nomeJogador") String nomeJogador, @Param("status") StatusPartida status);

    /**
     * Busca top 10 maiores ganhos
     */
    @Query("SELECT p FROM Partida p WHERE p.valorGanho > 0 ORDER BY p.valorGanho DESC")
    List<Partida> findTop10MaioresGanhos();
}

