package com.gestao.eventos.service;

import com.gestao.eventos.dto.CreateEventoDto;
import com.gestao.eventos.dto.EventoDto;
import com.gestao.eventos.dto.UpdateEventoDto; // ← IMPORTANTE: adicione este import
import com.gestao.eventos.entity.Evento;
import com.gestao.eventos.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    private CreateEventoDto createEventoDto;
    private UpdateEventoDto updateEventoDto; // ← novo campo
    private Evento eventoSalvo;

    @BeforeEach
    void setUp() {
        createEventoDto = new CreateEventoDto();
        createEventoDto.setTitulo("Evento Teste");
        createEventoDto.setDescricao("Descrição de teste");
        createEventoDto.setDataEvento(LocalDateTime.now().plusDays(1));
        createEventoDto.setLocal("Local Teste");

        // ✅ Crie um UpdateEventoDto separado
        updateEventoDto = new UpdateEventoDto();
        updateEventoDto.setTitulo("Evento Atualizado");
        updateEventoDto.setDescricao("Nova descrição");
        updateEventoDto.setDataEvento(LocalDateTime.now().plusDays(2));
        updateEventoDto.setLocal("Novo Local");

        eventoSalvo = new Evento();
        eventoSalvo.setId(1L);
        eventoSalvo.setTitulo("Evento Teste");
        eventoSalvo.setDescricao("Descrição de teste");
        eventoSalvo.setDataEvento(LocalDateTime.now().plusDays(1));
        eventoSalvo.setLocal("Local Teste");
        eventoSalvo.setDeleted(false);
    }

    @Test
    void deveCriarEventoComSucesso() {
        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoSalvo);

        EventoDto resultado = eventoService.create(createEventoDto);

        assertNotNull(resultado);
        assertEquals("Evento Teste", resultado.getTitulo());
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarEventoInexistente() {
        // ✅ Agora usamos updateEventoDto (não createEventoDto)
        when(eventoRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventoService.update(999L, updateEventoDto); // ← aqui estava o erro
        });

        assertEquals("Evento não encontrado com ID: 999", exception.getMessage());
    }

    @Test
    void deveDeletarEventoComSucesso() {
        when(eventoRepository.findNotDeletedById(1L)).thenReturn(Optional.of(eventoSalvo));

        eventoService.delete(1L);

        verify(eventoRepository, times(1)).markAsDeleted(1L);
    }
}