package com.cassino.online.controller;

import com.cassino.online.dto.IniciarPartidaDTO;
import com.cassino.online.service.PartidaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartidaController.class)
public class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidaService partidaService;

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
}
