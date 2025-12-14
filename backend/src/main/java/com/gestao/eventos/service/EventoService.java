package com.gestao.eventos.service;

import com.gestao.eventos.dto.CreateEventoDto;
import com.gestao.eventos.dto.EventoDto;
import com.gestao.eventos.dto.UpdateEventoDto;
import com.gestao.eventos.entity.Evento;
import com.gestao.eventos.repository.EventoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public Page<EventoDto> findAll(Pageable pageable) {
        return eventoRepository.findAllNotDeleted(pageable)
                .map(this::convertToDto);
    }

    public EventoDto findById(Long id) {
        Evento evento = eventoRepository.findNotDeletedById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
        return convertToDto(evento);
    }

    public EventoDto create(CreateEventoDto dto) {
        Evento evento = new Evento();
        evento.setTitulo(dto.getTitulo());
        evento.setDescricao(dto.getDescricao());
        evento.setDataEvento(dto.getDataEvento());
        evento.setLocal(dto.getLocal());
        evento = eventoRepository.save(evento);
        return convertToDto(evento);
    }

    public EventoDto update(Long id, UpdateEventoDto dto) {
        Evento evento = eventoRepository.findNotDeletedById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
        evento.setTitulo(dto.getTitulo());
        evento.setDescricao(dto.getDescricao());
        evento.setDataEvento(dto.getDataEvento());
        evento.setLocal(dto.getLocal());
        evento = eventoRepository.save(evento);
        return convertToDto(evento);
    }

    @Transactional
    public void delete(Long id) {
        if (eventoRepository.findNotDeletedById(id).isEmpty()) {
            throw new RuntimeException("Evento não encontrado com ID: " + id);
        }
        eventoRepository.markAsDeleted(id);
    }

    private EventoDto convertToDto(Evento evento) {
        EventoDto dto = new EventoDto();
        dto.setId(evento.getId());
        dto.setTitulo(evento.getTitulo());
        dto.setDescricao(evento.getDescricao());
        dto.setDataEvento(evento.getDataEvento());
        dto.setLocal(evento.getLocal());
        dto.setCreatedAt(evento.getCreatedAt());
        dto.setUpdatedAt(evento.getUpdatedAt());
        return dto;
    }
}
