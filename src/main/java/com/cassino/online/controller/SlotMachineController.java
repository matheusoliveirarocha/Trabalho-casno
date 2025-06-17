package com.cassino.online.controller;

import com.cassino.online.service.SlotMachineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para o jogo Slot Machine
 */
@RestController
@RequestMapping("/api/jogos/slot-machine")
@Tag(name = "Slot Machine", description = "API para o jogo Slot Machine (Caça-níqueis)")
@CrossOrigin(origins = "*")
public class SlotMachineController {

    private final SlotMachineService slotMachineService;

    @Autowired
    public SlotMachineController(SlotMachineService slotMachineService) {
        this.slotMachineService = slotMachineService;
    }

    /**
     * Jogar na Slot Machine
     */
    @PostMapping("/jogar/{partidaId}")
    @Operation(summary = "Jogar Slot Machine", description = "Executa uma jogada na Slot Machine girando os rolos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jogada executada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    })
    public ResponseEntity<?> jogarSlotMachine(
            @Parameter(description = "ID da partida") @PathVariable Long partidaId) {
        try {
            SlotMachineService.ResultadoSlotMachine resultado = slotMachineService.jogarSlotMachine(partidaId);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
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

