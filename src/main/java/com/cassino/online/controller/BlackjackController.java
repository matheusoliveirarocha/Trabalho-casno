package com.cassino.online.controller;

import com.cassino.online.dto.JogadaBlackjackDTO;
import com.cassino.online.service.BlackjackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para o jogo Blackjack
 */
@RestController
@RequestMapping("/api/jogos/blackjack")
@Tag(name = "Blackjack", description = "API para o jogo Blackjack")
@CrossOrigin(origins = "*")
public class BlackjackController {

    private final BlackjackService blackjackService;

    @Autowired
    public BlackjackController(BlackjackService blackjackService) {
        this.blackjackService = blackjackService;
    }

    /**
     * Iniciar uma partida de Blackjack
     */
    @PostMapping("/iniciar/{partidaId}")
    @Operation(summary = "Iniciar Blackjack", description = "Inicia uma partida de Blackjack distribuindo as cartas iniciais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blackjack iniciado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<?> iniciarBlackjack(
            @Parameter(description = "ID da partida") @PathVariable Long partidaId) {
        try {
            BlackjackService.EstadoBlackjack estado = blackjackService.iniciarBlackjack(partidaId);
            return ResponseEntity.ok(estado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Fazer uma jogada no Blackjack
     */
    @PostMapping("/jogar/{partidaId}")
    @Operation(summary = "Jogar Blackjack", description = "Executa uma jogada no Blackjack (HIT, STAND, DOUBLE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogada executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Jogada inválida"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<?> jogarBlackjack(
            @Parameter(description = "ID da partida") @PathVariable Long partidaId,
            @RequestBody JogadaBlackjackDTO jogada) {
        try {
            BlackjackService.EstadoBlackjack estado = blackjackService.jogarBlackjack(partidaId, jogada);
            return ResponseEntity.ok(estado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Erro na jogada", e.getMessage()));
        }
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

