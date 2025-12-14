package com.gestao.eventos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestao.eventos.dto.CreateEventoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarEListarEventoViaEndpoint() throws Exception {
        // dado
        CreateEventoDto dto = new CreateEventoDto();
        dto.setTitulo("Evento de Integração");
        dto.setDescricao("Teste completo");
        dto.setDataEvento(LocalDateTime.now().plusDays(1));
        dto.setLocal("Online");

        String json = objectMapper.writeValueAsString(dto);

        // quando: cria evento
        mockMvc.perform(post("/api/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Evento de Integração"));

        // quando: lista eventos
        mockMvc.perform(get("/api/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Evento de Integração"));
    }
}