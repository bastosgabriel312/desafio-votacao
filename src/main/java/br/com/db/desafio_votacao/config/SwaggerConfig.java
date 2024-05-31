package br.com.db.desafio_votacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        record Logo(String url) {
        }
        return new OpenAPI()
                .info(new Info()
                        .title("Desafio Votação")
                        .description("Uma aplicação em Java com Spring Boot para gerenciar votações em cooperativas, permitindo cadastrar pautas, abrir sessões de votação, receber e contabilizar votos, com integração externa para verificação de CPFs")
                        .version("1.0")
                        .extensions(Collections.singletonMap("x-logo", new Logo("http://localhost:8080/logo.png")))
                );
    }
}
