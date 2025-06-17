package com.cassino.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Spring Boot
 * Microserviço para cassino online com jogos de Blackjack, Roleta e Slot Machine
 */
@SpringBootApplication
public class CassinoOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(CassinoOnlineApplication.class, args);

    }
}

