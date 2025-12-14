package com.gestao.eventos.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDto {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataEvento;
    private String local;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}