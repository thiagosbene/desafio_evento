package com.gestao.eventos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestão de Eventos")
                        .description("API REST para gerenciar eventos: cadastrar, listar, editar e excluir com soft delete.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thiago da Silve Benevides")
                                .email("thiago.sbene@gmail.com")
                                .url("https://github.com/seu-usuario"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}