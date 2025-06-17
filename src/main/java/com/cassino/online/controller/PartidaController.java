package com.cassino.online.controller;

import com.cassino.online.dto.IniciarPartidaDTO;
import com.cassino.online.enums.TipoJogo;
import com.cassino.online.model.Partida;
import com.cassino.online.service.PartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gerenciamento de partidas do cassino
 */
@RestController
@RequestMapping("/api/partidas")
@Tag(name = "Partidas", description = "API para gerenciamento de partidas do cassino online")
@CrossOrigin(origins = "*")
public class PartidaController {

    private final PartidaService partidaService;

    @Autowired
    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    /**
     * Iniciar uma nova partida
     */
    @PostMapping
    @Operation(summary = "Iniciar nova partida", description = "Inicia uma nova partida no cassino")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Partida criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "Jogador já possui partida em andamento")
    })
    public ResponseEntity<?> iniciarPartida(@Valid @RequestBody IniciarPartidaDTO iniciarPartidaDTO) {
        try {
            Partida novaPartida = partidaService.iniciarPartida(iniciarPartidaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPartida);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Erro ao iniciar partida", e.getMessage()));
        }
    }

    /**
     * Listar todas as partidas
     */
    @GetMapping
    @Operation(summary = "Listar todas as partidas", description = "Retorna uma lista com todas as partidas do cassino")
    @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso")
    public ResponseEntity<List<Partida>> listarTodasPartidas() {
        List<Partida> partidas = partidaService.listarTodasPartidas();
        return ResponseEntity.ok(partidas);
    }

    /**
     * Buscar partida por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar partida por ID", description = "Retorna uma partida específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partida encontrada"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<?> buscarPartidaPorId(
            @Parameter(description = "ID da partida") @PathVariable Long id) {
        Optional<Partida> partida = partidaService.buscarPartidaPorId(id);
        if (partida.isPresent()) {
            return ResponseEntity.ok(partida.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Partida não encontrada", "Nenhuma partida encontrada com ID: " + id));
        }
    }

    /**
     * Buscar partidas por jogador
     */
    @GetMapping("/jogador/{nomeJogador}")
    @Operation(summary = "Buscar partidas por jogador", description = "Retorna todas as partidas de um jogador específico")
    @ApiResponse(responseCode = "200", description = "Lista de partidas do jogador retornada com sucesso")
    public ResponseEntity<List<Partida>> buscarPartidasPorJogador(
            @Parameter(description = "Nome do jogador") @PathVariable String nomeJogador) {
        List<Partida> partidas = partidaService.buscarPartidasPorJogador(nomeJogador);
        return ResponseEntity.ok(partidas);
    }

    /**
     * Buscar partidas por tipo de jogo
     */
    @GetMapping("/tipo/{tipoJogo}")
    @Operation(summary = "Buscar partidas por tipo de jogo", description = "Retorna todas as partidas de um tipo específico de jogo")
    @ApiResponse(responseCode = "200", description = "Lista de partidas do tipo de jogo retornada com sucesso")
    public ResponseEntity<List<Partida>> buscarPartidasPorTipo(
            @Parameter(description = "Tipo de jogo") @PathVariable TipoJogo tipoJogo) {
        List<Partida> partidas = partidaService.buscarPartidasPorTipo(tipoJogo);
        return ResponseEntity.ok(partidas);
    }

    /**
     * Buscar partidas em andamento
     */
    @GetMapping("/em-andamento")
    @Operation(summary = "Buscar partidas em andamento", description = "Retorna todas as partidas que estão em andamento")
    @ApiResponse(responseCode = "200", description = "Lista de partidas em andamento retornada com sucesso")
    public ResponseEntity<List<Partida>> buscarPartidasEmAndamento() {
        List<Partida> partidas = partidaService.buscarPartidasEmAndamento();
        return ResponseEntity.ok(partidas);
    }

    /**
     * Cancelar uma partida
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar partida", description = "Cancela uma partida em andamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partida cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada"),
            @ApiResponse(responseCode = "400", description = "Partida não pode ser cancelada")
    })
    public ResponseEntity<?> cancelarPartida(
            @Parameter(description = "ID da partida") @PathVariable Long id) {
        try {
            partidaService.cancelarPartida(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Partida não encontrada", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Erro ao cancelar partida", e.getMessage()));
        }
    }

    /**
     * Buscar histórico de um jogador
     */
    @GetMapping("/historico/{nomeJogador}")
    @Operation(summary = "Buscar histórico do jogador", description = "Retorna o histórico de partidas finalizadas de um jogador")
    @ApiResponse(responseCode = "200", description = "Histórico do jogador retornado com sucesso")
    public ResponseEntity<List<Partida>> buscarHistoricoJogador(
            @Parameter(description = "Nome do jogador") @PathVariable String nomeJogador) {
        List<Partida> historico = partidaService.buscarHistoricoJogador(nomeJogador);
        return ResponseEntity.ok(historico);
    }

    /**
     * Buscar partidas por período
     */
    @GetMapping("/periodo")
    @Operation(summary = "Buscar partidas por período", description = "Retorna partidas dentro de um período específico")
    @ApiResponse(responseCode = "200", description = "Lista de partidas do período retornada com sucesso")
    public ResponseEntity<List<Partida>> buscarPartidasPorPeriodo(
            @Parameter(description = "Data de início") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @Parameter(description = "Data de fim") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<Partida> partidas = partidaService.buscarPartidasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(partidas);
    }

    /**
     * Buscar estatísticas de um jogador
     */
    @GetMapping("/estatisticas/{nomeJogador}")
    @Operation(summary = "Buscar estatísticas do jogador", description = "Retorna estatísticas detalhadas de um jogador")
    @ApiResponse(responseCode = "200", description = "Estatísticas do jogador retornadas com sucesso")
    public ResponseEntity<PartidaService.EstatisticasJogadorDTO> buscarEstatisticasJogador(
            @Parameter(description = "Nome do jogador") @PathVariable String nomeJogador) {
        PartidaService.EstatisticasJogadorDTO estatisticas = partidaService.calcularEstatisticasJogador(nomeJogador);
        return ResponseEntity.ok(estatisticas);
    }

    /**
     * Classe interna para respostas de erro
     */
    public static class ErrorResponse {
        private String erro;
        private String mensagem;

        public ErrorResponse(String erro, String mensagem) {
            this.erro = erro;
            this.mensagem = mensagem;
        }

        public String getErro() {
            return erro;
        }

        public void setErro(String erro) {
            this.erro = erro;
        }

        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }
    }
}

