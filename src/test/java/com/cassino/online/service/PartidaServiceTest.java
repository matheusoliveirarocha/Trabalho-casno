package com.cassino.online.service;

import com.cassino.online.dto.IniciarPartidaDTO;
import com.cassino.online.enums.StatusPartida;
import com.cassino.online.enums.TipoJogo;
import com.cassino.online.model.Partida;
import com.cassino.online.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para PartidaService
 */
@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @InjectMocks
    private PartidaService partidaService;

    private Partida partidaExemplo;
    private IniciarPartidaDTO iniciarPartidaDTO;

    @BeforeEach
    void setUp() {
        partidaExemplo = new Partida();
        partidaExemplo.setId(1L);
        partidaExemplo.setNomeJogador("João Silva");
        partidaExemplo.setTipoJogo(TipoJogo.BLACKJACK);
        partidaExemplo.setValorAposta(new BigDecimal("50.00"));
        partidaExemplo.setStatus(StatusPartida.EM_ANDAMENTO);

        iniciarPartidaDTO = new IniciarPartidaDTO();
        iniciarPartidaDTO.setNomeJogador("Maria Santos");
        iniciarPartidaDTO.setTipoJogo(TipoJogo.ROLETA);
        iniciarPartidaDTO.setValorAposta(new BigDecimal("25.00"));
    }

    @Test
    void iniciarPartida_DeveRetornarPartidaSalva_QuandoDadosValidos() {
        // Arrange
        when(partidaRepository.findByNomeJogadorAndStatus(iniciarPartidaDTO.getNomeJogador(), StatusPartida.EM_ANDAMENTO))
                .thenReturn(Arrays.asList());
        when(partidaRepository.save(any(Partida.class))).thenReturn(partidaExemplo);

        // Act
        Partida resultado = partidaService.iniciarPartida(iniciarPartidaDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(partidaExemplo.getId(), resultado.getId());
        verify(partidaRepository).findByNomeJogadorAndStatus(iniciarPartidaDTO.getNomeJogador(), StatusPartida.EM_ANDAMENTO);
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void iniciarPartida_DeveLancarExcecao_QuandoJogadorTemPartidaEmAndamento() {
        // Arrange
        when(partidaRepository.findByNomeJogadorAndStatus(iniciarPartidaDTO.getNomeJogador(), StatusPartida.EM_ANDAMENTO))
                .thenReturn(Arrays.asList(partidaExemplo));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> partidaService.iniciarPartida(iniciarPartidaDTO)
        );
        
        assertTrue(exception.getMessage().contains("já possui uma partida em andamento"));
        verify(partidaRepository).findByNomeJogadorAndStatus(iniciarPartidaDTO.getNomeJogador(), StatusPartida.EM_ANDAMENTO);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void listarTodasPartidas_DeveRetornarListaDePartidas() {
        // Arrange
        List<Partida> partidasEsperadas = Arrays.asList(partidaExemplo);
        when(partidaRepository.findAll()).thenReturn(partidasEsperadas);

        // Act
        List<Partida> resultado = partidaService.listarTodasPartidas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(partidaExemplo, resultado.get(0));
        verify(partidaRepository).findAll();
    }

    @Test
    void buscarPartidaPorId_DeveRetornarPartida_QuandoIdExiste() {
        // Arrange
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partidaExemplo));

        // Act
        Optional<Partida> resultado = partidaService.buscarPartidaPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(partidaExemplo, resultado.get());
        verify(partidaRepository).findById(1L);
    }

    @Test
    void buscarPartidaPorId_DeveRetornarVazio_QuandoIdNaoExiste() {
        // Arrange
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Partida> resultado = partidaService.buscarPartidaPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(partidaRepository).findById(999L);
    }

    @Test
    void buscarPartidasPorJogador_DeveRetornarListaDePartidas() {
        // Arrange
        List<Partida> partidasEsperadas = Arrays.asList(partidaExemplo);
        when(partidaRepository.findByNomeJogadorContainingIgnoreCase("João")).thenReturn(partidasEsperadas);

        // Act
        List<Partida> resultado = partidaService.buscarPartidasPorJogador("João");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(partidaExemplo, resultado.get(0));
        verify(partidaRepository).findByNomeJogadorContainingIgnoreCase("João");
    }

    @Test
    void buscarPartidasPorTipo_DeveRetornarListaDePartidas() {
        // Arrange
        List<Partida> partidasEsperadas = Arrays.asList(partidaExemplo);
        when(partidaRepository.findByTipoJogo(TipoJogo.BLACKJACK)).thenReturn(partidasEsperadas);

        // Act
        List<Partida> resultado = partidaService.buscarPartidasPorTipo(TipoJogo.BLACKJACK);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(partidaExemplo, resultado.get(0));
        verify(partidaRepository).findByTipoJogo(TipoJogo.BLACKJACK);
    }

    @Test
    void buscarPartidasEmAndamento_DeveRetornarApenasPartidasEmAndamento() {
        // Arrange
        List<Partida> partidasEmAndamento = Arrays.asList(partidaExemplo);
        when(partidaRepository.findByStatus(StatusPartida.EM_ANDAMENTO)).thenReturn(partidasEmAndamento);

        // Act
        List<Partida> resultado = partidaService.buscarPartidasEmAndamento();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(StatusPartida.EM_ANDAMENTO, resultado.get(0).getStatus());
        verify(partidaRepository).findByStatus(StatusPartida.EM_ANDAMENTO);
    }

    @Test
    void cancelarPartida_DeveCancelarPartida_QuandoPartidaEmAndamento() {
        // Arrange
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partidaExemplo));
        when(partidaRepository.save(any(Partida.class))).thenReturn(partidaExemplo);

        // Act
        assertDoesNotThrow(() -> partidaService.cancelarPartida(1L));

        // Assert
        verify(partidaRepository).findById(1L);
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void cancelarPartida_DeveLancarExcecao_QuandoPartidaNaoExiste() {
        // Arrange
        when(partidaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> partidaService.cancelarPartida(999L)
        );
        
        assertTrue(exception.getMessage().contains("Partida não encontrada"));
        verify(partidaRepository).findById(999L);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void cancelarPartida_DeveLancarExcecao_QuandoPartidaJaFinalizada() {
        // Arrange
        partidaExemplo.setStatus(StatusPartida.VITORIA);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partidaExemplo));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> partidaService.cancelarPartida(1L)
        );
        
        assertTrue(exception.getMessage().contains("Apenas partidas em andamento podem ser canceladas"));
        verify(partidaRepository).findById(1L);
        verify(partidaRepository, never()).save(any(Partida.class));
    }

    @Test
    void calcularEstatisticasJogador_DeveRetornarEstatisticasCorretas() {
        // Arrange
        List<Partida> todasPartidas = Arrays.asList(partidaExemplo);
        when(partidaRepository.findByNomeJogadorContainingIgnoreCase("João")).thenReturn(todasPartidas);
        when(partidaRepository.contarVitoriasPorJogador("João", StatusPartida.VITORIA)).thenReturn(1L);

        // Act
        PartidaService.EstatisticasJogadorDTO resultado = partidaService.calcularEstatisticasJogador("João");

        // Assert
        assertNotNull(resultado);
        assertEquals("João", resultado.getNomeJogador());
        assertEquals(1, resultado.getTotalPartidas());
        assertEquals(1, resultado.getVitorias());
        verify(partidaRepository).findByNomeJogadorContainingIgnoreCase("João");
        verify(partidaRepository).contarVitoriasPorJogador("João", StatusPartida.VITORIA);
    }
}

