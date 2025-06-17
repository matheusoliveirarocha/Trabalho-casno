package com.cassino.online.config;

import com.cassino.online.enums.TipoJogo;
import com.cassino.online.model.Partida;
import com.cassino.online.repository.PartidaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class PartidaDataLoader {

    @Bean
    CommandLineRunner carregarPartidas(PartidaRepository repository) {
        return args -> {
            repository.save(new Partida("JogadorA", TipoJogo.ROLETA, new BigDecimal("100.00")));
            repository.save(new Partida("JogadorB", TipoJogo.BLACKJACK, new BigDecimal("250.00")));
            repository.save(new Partida("JogadorC", TipoJogo.SLOT_MACHINE, new BigDecimal("50.00")));
        };
    }
}

