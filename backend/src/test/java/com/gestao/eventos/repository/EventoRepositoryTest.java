package com.gestao.eventos.repository;

import com.gestao.eventos.entity.Evento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class EventoRepositoryTest {

    @Autowired
    private EventoRepository eventoRepository;

    @AfterEach
    void tearDown() {
        eventoRepository.deleteAll();
    }

    @Test
    void deveIgnorarEventosDeletadosNaBuscaPorId() {
        // dado: evento criado e depois "deletado"
        Evento evento = new Evento();
        evento.setTitulo("Teste");
        evento.setDataEvento(LocalDateTime.now().plusDays(1));
        evento.setLocal("Local");
        evento = eventoRepository.save(evento);

        eventoRepository.markAsDeleted(evento.getId());
        eventoRepository.flush();

        // quando
        Optional<Evento> resultado = eventoRepository.findNotDeletedById(evento.getId());

        // então
        assertThat(resultado).isEmpty();
    }
}