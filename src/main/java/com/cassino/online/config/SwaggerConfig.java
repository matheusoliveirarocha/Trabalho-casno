package com.cassino.online.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI cassinoOnlineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cassino Online Microservice API")
                        .description("API RESTful para cassino online com jogos de Blackjack, Roleta e Slot Machine desenvolvida com Spring Boot")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("dev@cassinoonline.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

