package com.cassino.online.controller;

import com.cassino.online.dto.JogadaRoletaDTO;
import com.cassino.online.service.RoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para o jogo Roleta
 */
@RestController
@RequestMapping("/api/jogos/roleta")
@Tag(name = "Roleta", description = "API para o jogo Roleta")
@CrossOrigin(origins = "*")
public class RoletaController {

    private final RoletaService roletaService;

    @Autowired
    public RoletaController(RoletaService roletaService) {
        this.roletaService = roletaService;
    }

    /**
     * Jogar na Roleta
     */
    @PostMapping("/jogar/{partidaId}")
    @Operation(summary = "Jogar Roleta", description = "Executa uma jogada na Roleta com número, cor ou par/ímpar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogada executada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da jogada inválidos"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<?> jogarRoleta(
            @Parameter(description = "ID da partida") @PathVariable Long partidaId,
            @Valid @RequestBody JogadaRoletaDTO jogada) {
        try {
            RoletaService.ResultadoRoleta resultado = roletaService.jogarRoleta(partidaId, jogada);
            return ResponseEntity.ok(resultado);
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

