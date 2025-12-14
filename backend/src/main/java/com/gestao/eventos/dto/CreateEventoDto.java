package com.gestao.eventos.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventoDto {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
    private String titulo;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String descricao;

    @FutureOrPresent(message = "A data do evento não pode ser no passado")
    private LocalDateTime dataEvento;

    @Size(max = 200, message = "O local deve ter no máximo 200 caracteres")
    private String local;
}