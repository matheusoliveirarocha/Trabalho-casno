package com.cassino.online.controller;

import com.cassino.online.dto.JogadaBlackjackDTO;
import com.cassino.online.dto.IniciarPartidaDTO;
import com.cassino.online.service.BlackjackService;
import com.cassino.online.service.PartidaService;
import com.cassino.online.service.RoletaService;
import com.cassino.online.service.SlotMachineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({BlackjackController.class, PartidaController.class, RoletaController.class, SlotMachineController.class})
public class BlackjackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlackjackService blackjackService;

    @MockBean
    private PartidaService partidaService;

    @MockBean
    private RoletaService roletaService;

    @MockBean
    private SlotMachineService slotMachineService;

    @Test
    public void deveRetornar200QuandoJogarBlackjack() throws Exception {
        String jsonRequest = "{\"valor\":18}";
        when(blackjackService.jogar(any(JogadaBlackjackDTO.class))).thenReturn("Sua jogada foi 18. Resultado: perdeu");

        mockMvc.perform(post("/blackjack/jogar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Sua jogada foi 18. Resultado: perdeu"));
    }

    @Test
    public void deveRetornar200QuandoIniciarPartida() throws Exception {
        String json = "{\"jogo\":\"Roleta\"}";
        when(partidaService.iniciarPartida(any(IniciarPartidaDTO.class))).thenReturn("Partida de Roleta iniciada");

        mockMvc.perform(post("/partida/iniciar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Partida de Roleta iniciada"));
    }

    @Test
    public void deveRetornar200QuandoJogarRoleta() throws Exception {
        when(roletaService.jogar()).thenReturn("Roleta girada. Número sorteado: 27");

        mockMvc.perform(post("/roleta/jogar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Roleta girada. Número sorteado: 27"));
    }

    @Test
    public void deveRetornar200QuandoJogarSlotMachine() throws Exception {
        when(slotMachineService.jogar()).thenReturn("Slot machine girada. Resultado: 🍒🍒🍋");

        mockMvc.perform(post("/slotmachine/jogar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Slot machine girada. Resultado: 🍒🍒🍋"));
    }
}
