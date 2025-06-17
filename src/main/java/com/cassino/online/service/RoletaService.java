package com.cassino.online.service;

import com.cassino.online.dto.JogadaRoletaDTO;
import com.cassino.online.dto.ResultadoPartidaDTO;
import com.cassino.online.enums.StatusPartida;
import com.cassino.online.model.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Serviço para o jogo Roleta
 */
@Service
public class RoletaService {

    private final PartidaService partidaService;
    private final Random random = new Random();

    // Números vermelhos na roleta europeia
    private static final List<Integer> NUMEROS_VERMELHOS = Arrays.asList(
            1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    );

    @Autowired
    public RoletaService(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    /**
     * Joga na roleta
     */
    public ResultadoRoleta jogarRoleta(Long partidaId, JogadaRoletaDTO jogada) {
        Partida partida = partidaService.buscarPartidaPorId(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        // Gira a roleta
        int numeroSorteado = random.nextInt(37); // 0 a 36

        // Determina cor do número sorteado
        String corSorteada = determinarCor(numeroSorteado);
        String parImparSorteado = numeroSorteado == 0 ? "ZERO" : (numeroSorteado % 2 == 0 ? "PAR" : "IMPAR");

        // Verifica se ganhou
        boolean ganhou = false;
        BigDecimal multiplicador = BigDecimal.ONE;

        if ("NUMERO".equals(jogada.getTipoAposta())) {
            ganhou = numeroSorteado == jogada.getNumeroApostado();
            multiplicador = BigDecimal.valueOf(35); // Paga 35:1
        } else if ("COR".equals(jogada.getTipoAposta())) {
            ganhou = corSorteada.equals(jogada.getCor());
            multiplicador = BigDecimal.valueOf(1); // Paga 1:1
        } else if ("PAR_IMPAR".equals(jogada.getTipoAposta())) {
            ganhou = parImparSorteado.equals(jogada.getParImpar());
            multiplicador = BigDecimal.valueOf(1); // Paga 1:1
        }

        // Calcula resultado
        StatusPartida status;
        BigDecimal valorGanho = BigDecimal.ZERO;
        String resultado;

        if (ganhou) {
            status = StatusPartida.VITORIA;
            valorGanho = partida.getValorAposta().multiply(multiplicador.add(BigDecimal.ONE)); // Inclui a aposta original
            resultado = String.format("Número sorteado: %d (%s, %s). Você ganhou!", 
                    numeroSorteado, corSorteada, parImparSorteado);
        } else {
            status = StatusPartida.DERROTA;
            resultado = String.format("Número sorteado: %d (%s, %s). Você perdeu.", 
                    numeroSorteado, corSorteada, parImparSorteado);
        }

        // Finaliza a partida
        ResultadoPartidaDTO resultadoPartida = partidaService.finalizarPartida(partidaId, status, valorGanho, resultado);

        return new ResultadoRoleta(
                numeroSorteado,
                corSorteada,
                parImparSorteado,
                ganhou,
                resultadoPartida
        );
    }

    /**
     * Determina a cor de um número na roleta
     */
    private String determinarCor(int numero) {
        if (numero == 0) {
            return "VERDE";
        }
        return NUMEROS_VERMELHOS.contains(numero) ? "VERMELHO" : "PRETO";
    }

    /**
     * Classe para resultado da roleta
     */
    public static class ResultadoRoleta {
        private int numeroSorteado;
        private String cor;
        private String parImpar;
        private boolean ganhou;
        private ResultadoPartidaDTO resultadoPartida;

        public ResultadoRoleta(int numeroSorteado, String cor, String parImpar, boolean ganhou, ResultadoPartidaDTO resultadoPartida) {
            this.numeroSorteado = numeroSorteado;
            this.cor = cor;
            this.parImpar = parImpar;
            this.ganhou = ganhou;
            this.resultadoPartida = resultadoPartida;
        }

        // Getters
        public int getNumeroSorteado() { return numeroSorteado; }
        public String getCor() { return cor; }
        public String getParImpar() { return parImpar; }
        public boolean isGanhou() { return ganhou; }
        public ResultadoPartidaDTO getResultadoPartida() { return resultadoPartida; }
    }
}

