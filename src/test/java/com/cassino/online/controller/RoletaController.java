package com.cassino.online.controller;

import com.cassino.online.service.RoletaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoletaController.class)
public class RoletaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoletaService roletaService;

    @Test
    public void deveRetornar200QuandoJogarRoleta() throws Exception {
        when(roletaService.jogar()).thenReturn("Roleta girada. Número sorteado: 27");

        mockMvc.perform(post("/roleta/jogar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Roleta girada. Número sorteado: 27"));
    }
}
