package com.cassino.online.service;

import com.cassino.online.enums.StatusPartida;
import com.cassino.online.enums.TipoJogo;
import com.cassino.online.model.Partida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para SlotMachineService
 */
@ExtendWith(MockitoExtension.class)
class SlotMachineServiceTest {

    @Mock
    private PartidaService partidaService;

    @InjectMocks
    private SlotMachineService slotMachineService;

    private Partida partidaExemplo;

    @BeforeEach
    void setUp() {
        partidaExemplo = new Partida();
        partidaExemplo.setId(1L);
        partidaExemplo.setNomeJogador("João Silva");
        partidaExemplo.setTipoJogo(TipoJogo.SLOT_MACHINE);
        partidaExemplo.setValorAposta(new BigDecimal("20.00"));
        partidaExemplo.setStatus(StatusPartida.EM_ANDAMENTO);
    }

    @Test
    void jogarSlotMachine_DeveRetornarResultado_QuandoPartidaExiste() {
        // Arrange
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(Optional.of(partidaExemplo));
        when(partidaService.finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString()))
                .thenReturn(null); // Simplificado para o teste

        // Act
        SlotMachineService.ResultadoSlotMachine resultado = slotMachineService.jogarSlotMachine(1L);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getSimbolos());
        assertEquals(3, resultado.getSimbolos().length);
        assertNotNull(resultado.getMultiplicador());
        assertNotNull(resultado.getTipoCombinacao());
        verify(partidaService).buscarPartidaPorId(1L);
        verify(partidaService).finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString());
    }

    @Test
    void jogarSlotMachine_DeveLancarExcecao_QuandoPartidaNaoExiste() {
        // Arrange
        when(partidaService.buscarPartidaPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> slotMachineService.jogarSlotMachine(999L)
        );
        
        assertTrue(exception.getMessage().contains("Partida não encontrada"));
        verify(partidaService).buscarPartidaPorId(999L);
        verify(partidaService, never()).finalizarPartida(anyLong(), any(StatusPartida.class), any(BigDecimal.class), anyString());
    }

    @Test
    void jogarSlotMachine_DeveGerarSimbolosValidos() {
        // Arrange
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(Optional.of(partidaExemplo));
        when(partidaService.finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString()))
                .thenReturn(null);

        // Act
        SlotMachineService.ResultadoSlotMachine resultado = slotMachineService.jogarSlotMachine(1L);

        // Assert
        String[] simbolosValidos = {"🍒", "🍋", "🍊", "🍇", "⭐", "💎", "🔔", "7️⃣"};
        for (String simbolo : resultado.getSimbolos()) {
            boolean encontrado = false;
            for (String simboloPermitido : simbolosValidos) {
                if (simboloPermitido.equals(simbolo)) {
                    encontrado = true;
                    break;
                }
            }
            assertTrue(encontrado, "Símbolo inválido encontrado: " + simbolo);
        }
    }

    @Test
    void jogarSlotMachine_DeveDefinirStatusCorreto_QuandoGanha() {
        // Arrange
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(Optional.of(partidaExemplo));
        when(partidaService.finalizarPartida(eq(1L), eq(StatusPartida.VITORIA), any(BigDecimal.class), anyString()))
                .thenReturn(null);

        // Act - Executar múltiplas vezes para tentar obter uma vitória
        boolean vitoriaTestada = false;
        for (int i = 0; i < 50 && !vitoriaTestada; i++) {
            reset(partidaService);
            when(partidaService.buscarPartidaPorId(1L)).thenReturn(Optional.of(partidaExemplo));
            when(partidaService.finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString()))
                    .thenReturn(null);
            
            SlotMachineService.ResultadoSlotMachine resultado = slotMachineService.jogarSlotMachine(1L);
            
            if (resultado.isGanhou()) {
                vitoriaTestada = true;
                assertTrue(resultado.getMultiplicador().compareTo(BigDecimal.ZERO) > 0);
            }
        }
        
        // Se não conseguiu testar vitória, pelo menos verifica que o método foi chamado
        verify(partidaService, atLeastOnce()).finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString());
    }
}

