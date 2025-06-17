package com.cassino.online.service;

import com.cassino.online.dto.JogadaRoletaDTO;
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
 * Testes unitários para RoletaService
 */
@ExtendWith(MockitoExtension.class)
class RoletaServiceTest {

    @Mock
    private PartidaService partidaService;

    @InjectMocks
    private RoletaService roletaService;

    private Partida partidaExemplo;
    private JogadaRoletaDTO jogadaRoleta;

    @BeforeEach
    void setUp() {
        partidaExemplo = new Partida();
        partidaExemplo.setId(1L);
        partidaExemplo.setNomeJogador("João Silva");
        partidaExemplo.setTipoJogo(TipoJogo.ROLETA);
        partidaExemplo.setValorAposta(new BigDecimal("50.00"));
        partidaExemplo.setStatus(StatusPartida.EM_ANDAMENTO);

        jogadaRoleta = new JogadaRoletaDTO();
        jogadaRoleta.setNumeroApostado(7);
        jogadaRoleta.setTipoAposta("NUMERO");
    }

    @Test
    void jogarRoleta_DeveRetornarResultado_QuandoPartidaExiste() {
        // Arrange
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(Optional.of(partidaExemplo));
        when(partidaService.finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString()))
                .thenReturn(null); // Simplificado para o teste

        // Act
        RoletaService.ResultadoRoleta resultado = roletaService.jogarRoleta(1L, jogadaRoleta);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getNumeroSorteado() >= 0 && resultado.getNumeroSorteado() <= 36);
        assertNotNull(resultado.getCor());
        assertNotNull(resultado.getParImpar());
        verify(partidaService).buscarPartidaPorId(1L);
        verify(partidaService).finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString());
    }

    @Test
    void jogarRoleta_DeveLancarExcecao_QuandoPartidaNaoExiste() {
        // Arrange
        when(partidaService.buscarPartidaPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roletaService.jogarRoleta(999L, jogadaRoleta)
        );
        
        assertTrue(exception.getMessage().contains("Partida não encontrada"));
        verify(partidaService).buscarPartidaPorId(999L);
        verify(partidaService, never()).finalizarPartida(anyLong(), any(StatusPartida.class), any(BigDecimal.class), anyString());
    }

    @Test
    void jogarRoleta_DeveProcessarApostaCor_QuandoTipoApostaEhCor() {
        // Arrange
        jogadaRoleta.setTipoAposta("COR");
        jogadaRoleta.setCor("VERMELHO");
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(Optional.of(partidaExemplo));
        when(partidaService.finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString()))
                .thenReturn(null);

        // Act
        RoletaService.ResultadoRoleta resultado = roletaService.jogarRoleta(1L, jogadaRoleta);

        // Assert
        assertNotNull(resultado);
        verify(partidaService).buscarPartidaPorId(1L);
        verify(partidaService).finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString());
    }

    @Test
    void jogarRoleta_DeveProcessarApostaParImpar_QuandoTipoApostaEhParImpar() {
        // Arrange
        jogadaRoleta.setTipoAposta("PAR_IMPAR");
        jogadaRoleta.setParImpar("IMPAR");
        when(partidaService.buscarPartidaPorId(1L)).thenReturn(Optional.of(partidaExemplo));
        when(partidaService.finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString()))
                .thenReturn(null);

        // Act
        RoletaService.ResultadoRoleta resultado = roletaService.jogarRoleta(1L, jogadaRoleta);

        // Assert
        assertNotNull(resultado);
        verify(partidaService).buscarPartidaPorId(1L);
        verify(partidaService).finalizarPartida(eq(1L), any(StatusPartida.class), any(BigDecimal.class), anyString());
    }
}

