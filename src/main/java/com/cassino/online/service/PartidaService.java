package com.cassino.online.service;

import com.cassino.online.dto.IniciarPartidaDTO;
import com.cassino.online.dto.ResultadoPartidaDTO;
import com.cassino.online.enums.StatusPartida;
import com.cassino.online.enums.TipoJogo;
import com.cassino.online.model.Partida;
import com.cassino.online.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço para gerenciamento de partidas
 */
@Service
@Transactional
public class PartidaService {

    private final PartidaRepository partidaRepository;

    @Autowired
    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    /**
     * Inicia uma nova partida
     */
    public Partida iniciarPartida(IniciarPartidaDTO iniciarPartidaDTO) {
        // Verifica se o jogador já tem partidas em andamento
        List<Partida> partidasEmAndamento = partidaRepository.findByNomeJogadorAndStatus(
                iniciarPartidaDTO.getNomeJogador(), StatusPartida.EM_ANDAMENTO);
        
        if (!partidasEmAndamento.isEmpty()) {
            throw new IllegalStateException("Jogador já possui uma partida em andamento. Finalize a partida atual antes de iniciar uma nova.");
        }

        Partida novaPartida = new Partida(
                iniciarPartidaDTO.getNomeJogador(),
                iniciarPartidaDTO.getTipoJogo(),
                iniciarPartidaDTO.getValorAposta()
        );

        return partidaRepository.save(novaPartida);
    }

    /**
     * Lista todas as partidas
     */
    @Transactional(readOnly = true)
    public List<Partida> listarTodasPartidas() {
        return partidaRepository.findAll();
    }

    /**
     * Busca uma partida por ID
     */
    @Transactional(readOnly = true)
    public Optional<Partida> buscarPartidaPorId(Long id) {
        return partidaRepository.findById(id);
    }

    /**
     * Busca partidas por jogador
     */
    @Transactional(readOnly = true)
    public List<Partida> buscarPartidasPorJogador(String nomeJogador) {
        return partidaRepository.findByNomeJogadorContainingIgnoreCase(nomeJogador);
    }

    /**
     * Busca partidas por tipo de jogo
     */
    @Transactional(readOnly = true)
    public List<Partida> buscarPartidasPorTipo(TipoJogo tipoJogo) {
        return partidaRepository.findByTipoJogo(tipoJogo);
    }

    /**
     * Busca partidas em andamento
     */
    @Transactional(readOnly = true)
    public List<Partida> buscarPartidasEmAndamento() {
        return partidaRepository.findByStatus(StatusPartida.EM_ANDAMENTO);
    }

    /**
     * Finaliza uma partida
     */
    public ResultadoPartidaDTO finalizarPartida(Long partidaId, StatusPartida novoStatus, BigDecimal valorGanho, String resultado) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada com ID: " + partidaId));

        if (partida.getStatus() != StatusPartida.EM_ANDAMENTO) {
            throw new IllegalStateException("Partida já foi finalizada");
        }

        partida.finalizarPartida(novoStatus, valorGanho, resultado);
        Partida partidaSalva = partidaRepository.save(partida);

        return criarResultadoDTO(partidaSalva);
    }

    /**
     * Cancela uma partida
     */
    public void cancelarPartida(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada com ID: " + partidaId));

        if (partida.getStatus() != StatusPartida.EM_ANDAMENTO) {
            throw new IllegalStateException("Apenas partidas em andamento podem ser canceladas");
        }

        partida.finalizarPartida(StatusPartida.CANCELADA, BigDecimal.ZERO, "Partida cancelada pelo jogador");
        partidaRepository.save(partida);
    }

    /**
     * Busca histórico de partidas finalizadas de um jogador
     */
    @Transactional(readOnly = true)
    public List<Partida> buscarHistoricoJogador(String nomeJogador) {
        return partidaRepository.findPartidasFinalizada(nomeJogador, StatusPartida.EM_ANDAMENTO);
    }

    /**
     * Busca partidas por período
     */
    @Transactional(readOnly = true)
    public List<Partida> buscarPartidasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return partidaRepository.findByPeriodo(dataInicio, dataFim);
    }

    /**
     * Calcula estatísticas de um jogador
     */
    @Transactional(readOnly = true)
    public EstatisticasJogadorDTO calcularEstatisticasJogador(String nomeJogador) {
        List<Partida> todasPartidas = partidaRepository.findByNomeJogadorContainingIgnoreCase(nomeJogador);
        
        long totalPartidas = todasPartidas.size();
        long vitorias = partidaRepository.contarVitoriasPorJogador(nomeJogador, StatusPartida.VITORIA);
        
        BigDecimal totalApostado = todasPartidas.stream()
                .map(Partida::getValorAposta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalGanho = todasPartidas.stream()
                .map(Partida::getValorGanho)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new EstatisticasJogadorDTO(nomeJogador, totalPartidas, vitorias, totalApostado, totalGanho);
    }

    /**
     * Cria um DTO de resultado da partida
     */
    private ResultadoPartidaDTO criarResultadoDTO(Partida partida) {
        String mensagem = switch (partida.getStatus()) {
            case VITORIA -> "Parabéns! Você ganhou!";
            case DERROTA -> "Que pena! Você perdeu.";
            case EMPATE -> "Empate! Sua aposta foi devolvida.";
            case CANCELADA -> "Partida cancelada.";
            default -> "Partida em andamento.";
        };

        return new ResultadoPartidaDTO(
                partida.getId(),
                partida.getStatus().getDescricao(),
                partida.getResultadoJogo(),
                partida.getValorGanho().toString(),
                partida.calcularLucroLiquido().toString(),
                mensagem
        );
    }

    /**
     * Classe interna para estatísticas do jogador
     */
    public static class EstatisticasJogadorDTO {
        private String nomeJogador;
        private long totalPartidas;
        private long vitorias;
        private BigDecimal totalApostado;
        private BigDecimal totalGanho;

        public EstatisticasJogadorDTO(String nomeJogador, long totalPartidas, long vitorias, BigDecimal totalApostado, BigDecimal totalGanho) {
            this.nomeJogador = nomeJogador;
            this.totalPartidas = totalPartidas;
            this.vitorias = vitorias;
            this.totalApostado = totalApostado;
            this.totalGanho = totalGanho;
        }

        // Getters
        public String getNomeJogador() { return nomeJogador; }
        public long getTotalPartidas() { return totalPartidas; }
        public long getVitorias() { return vitorias; }
        public BigDecimal getTotalApostado() { return totalApostado; }
        public BigDecimal getTotalGanho() { return totalGanho; }
        public double getPercentualVitorias() { 
            return totalPartidas > 0 ? (double) vitorias / totalPartidas * 100 : 0; 
        }
        public BigDecimal getLucroLiquido() { 
            return totalGanho.subtract(totalApostado); 
        }
    }
}

