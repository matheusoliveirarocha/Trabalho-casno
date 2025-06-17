package com.cassino.online.controller;

import com.cassino.online.service.SlotMachineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SlotMachineController.class)
public class SlotMachineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlotMachineService slotMachineService;

    @Test
    public void deveRetornar200QuandoJogarSlotMachine() throws Exception {
        when(slotMachineService.jogar()).thenReturn("Slot machine girada. Resultado: 🍒🍒🍋");

        mockMvc.perform(post("/slotmachine/jogar"))
                .andExpect(status().isOk())
                .andExpect(content().string("Slot machine girada. Resultado: 🍒🍒🍋"));
    }
}
