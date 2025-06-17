package com.cassino.online.service;

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
 * Serviço para o jogo Slot Machine (Caça-níqueis)
 */
@Service
public class SlotMachineService {

    private final PartidaService partidaService;
    private final Random random = new Random();

    // Símbolos da slot machine
    private static final List<String> SIMBOLOS = Arrays.asList(
            "🍒", "🍋", "🍊", "🍇", "⭐", "💎", "🔔", "7️⃣"
    );

    // Multiplicadores por símbolo (quanto mais raro, maior o multiplicador)
    private static final BigDecimal[] MULTIPLICADORES = {
            BigDecimal.valueOf(2),   // 🍒
            BigDecimal.valueOf(3),   // 🍋
            BigDecimal.valueOf(4),   // 🍊
            BigDecimal.valueOf(5),   // 🍇
            BigDecimal.valueOf(10),  // ⭐
            BigDecimal.valueOf(20),  // 💎
            BigDecimal.valueOf(50),  // 🔔
            BigDecimal.valueOf(100)  // 7️⃣
    };

    @Autowired
    public SlotMachineService(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    /**
     * Joga na slot machine
     */
    public ResultadoSlotMachine jogarSlotMachine(Long partidaId) {
        Partida partida = partidaService.buscarPartidaPorId(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada"));

        // Gira os 3 rolos
        String[] resultado = new String[3];
        for (int i = 0; i < 3; i++) {
            resultado[i] = sortearSimbolo();
        }

        // Verifica combinações vencedoras
        CombinacaoVencedora combinacao = verificarCombinacao(resultado);
        
        StatusPartida status;
        BigDecimal valorGanho = BigDecimal.ZERO;
        String descricaoResultado;

        if (combinacao.isVencedora()) {
            status = StatusPartida.VITORIA;
            valorGanho = partida.getValorAposta().multiply(combinacao.getMultiplicador());
            descricaoResultado = String.format("Combinação vencedora: %s %s %s - Multiplicador: %sx", 
                    resultado[0], resultado[1], resultado[2], combinacao.getMultiplicador());
        } else {
            status = StatusPartida.DERROTA;
            descricaoResultado = String.format("Sem combinação vencedora: %s %s %s", 
                    resultado[0], resultado[1], resultado[2]);
        }

        // Finaliza a partida
        ResultadoPartidaDTO resultadoPartida = partidaService.finalizarPartida(partidaId, status, valorGanho, descricaoResultado);

        return new ResultadoSlotMachine(
                resultado,
                combinacao.isVencedora(),
                combinacao.getMultiplicador(),
                combinacao.getTipoCombinacao(),
                resultadoPartida
        );
    }

    /**
     * Sorteia um símbolo baseado em probabilidades
     */
    private String sortearSimbolo() {
        // Probabilidades diferentes para cada símbolo (símbolos mais valiosos são mais raros)
        int[] probabilidades = {30, 25, 20, 15, 5, 3, 1, 1}; // Total: 100
        int sorteio = random.nextInt(100);
        
        int acumulado = 0;
        for (int i = 0; i < SIMBOLOS.size(); i++) {
            acumulado += probabilidades[i];
            if (sorteio < acumulado) {
                return SIMBOLOS.get(i);
            }
        }
        
        return SIMBOLOS.get(0); // Fallback
    }

    /**
     * Verifica se há combinação vencedora
     */
    private CombinacaoVencedora verificarCombinacao(String[] resultado) {
        // Três símbolos iguais
        if (resultado[0].equals(resultado[1]) && resultado[1].equals(resultado[2])) {
            int indice = SIMBOLOS.indexOf(resultado[0]);
            return new CombinacaoVencedora(true, MULTIPLICADORES[indice], "TRÊS IGUAIS");
        }
        
        // Dois símbolos iguais
        if (resultado[0].equals(resultado[1]) || resultado[1].equals(resultado[2]) || resultado[0].equals(resultado[2])) {
            // Pega o símbolo que se repete
            String simboloRepetido = resultado[0].equals(resultado[1]) ? resultado[0] : 
                                   resultado[1].equals(resultado[2]) ? resultado[1] : resultado[0];
            int indice = SIMBOLOS.indexOf(simboloRepetido);
            BigDecimal multiplicadorReduzido = MULTIPLICADORES[indice].divide(BigDecimal.valueOf(3), 2, java.math.RoundingMode.HALF_UP);
            return new CombinacaoVencedora(true, multiplicadorReduzido, "DOIS IGUAIS");
        }
        
        // Combinação especial: qualquer combinação com 7️⃣
        for (String simbolo : resultado) {
            if ("7️⃣".equals(simbolo)) {
                return new CombinacaoVencedora(true, BigDecimal.valueOf(2), "SORTE COM 7");
            }
        }

        return new CombinacaoVencedora(false, BigDecimal.ZERO, "SEM COMBINAÇÃO");
    }

    /**
     * Classe para representar uma combinação vencedora
     */
    private static class CombinacaoVencedora {
        private boolean vencedora;
        private BigDecimal multiplicador;
        private String tipoCombinacao;

        public CombinacaoVencedora(boolean vencedora, BigDecimal multiplicador, String tipoCombinacao) {
            this.vencedora = vencedora;
            this.multiplicador = multiplicador;
            this.tipoCombinacao = tipoCombinacao;
        }

        public boolean isVencedora() { return vencedora; }
        public BigDecimal getMultiplicador() { return multiplicador; }
        public String getTipoCombinacao() { return tipoCombinacao; }
    }

    /**
     * Classe para resultado da slot machine
     */
    public static class ResultadoSlotMachine {
        private String[] simbolos;
        private boolean ganhou;
        private BigDecimal multiplicador;
        private String tipoCombinacao;
        private ResultadoPartidaDTO resultadoPartida;

        public ResultadoSlotMachine(String[] simbolos, boolean ganhou, BigDecimal multiplicador, 
                                   String tipoCombinacao, ResultadoPartidaDTO resultadoPartida) {
            this.simbolos = simbolos;
            this.ganhou = ganhou;
            this.multiplicador = multiplicador;
            this.tipoCombinacao = tipoCombinacao;
            this.resultadoPartida = resultadoPartida;
        }

        // Getters
        public String[] getSimbolos() { return simbolos; }
        public boolean isGanhou() { return ganhou; }
        public BigDecimal getMultiplicador() { return multiplicador; }
        public String getTipoCombinacao() { return tipoCombinacao; }
        public ResultadoPartidaDTO getResultadoPartida() { return resultadoPartida; }
    }
}

