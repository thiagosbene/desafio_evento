package com.gestao.eventos.controller;

import com.gestao.eventos.dto.CreateEventoDto;
import com.gestao.eventos.dto.EventoDto;
import com.gestao.eventos.dto.UpdateEventoDto;
import com.gestao.eventos.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Gerenciamento de eventos com suporte a paginação, validação e soft delete")
public class EventoController {

    private final EventoService eventoService;

    @Operation(
        summary = "Listar eventos",
        description = "Retorna uma lista paginada de eventos ativos (não excluídos logicamente). " +
                      "Suporta ordenação por qualquer campo (ex: titulo, dataEvento) com direção asc/desc."
    )
    @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<EventoDto>> getAll(
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(eventoService.findAll(pageable));
    }

    @Operation(
        summary = "Buscar evento por ID",
        description = "Retorna os detalhes de um evento específico, desde que não tenha sido excluído."
    )
    @ApiResponse(responseCode = "200", description = "Evento encontrado")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado ou excluído")
    @GetMapping("/{id}")
    public ResponseEntity<EventoDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.findById(id));
    }

    @Operation(
        summary = "Criar novo evento",
        description = "Registra um novo evento no sistema. Todos os campos obrigatórios devem ser válidos."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Evento criado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoDto.class))
    )
    @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: título vazio, data no passado)")
    @PostMapping
    public ResponseEntity<EventoDto> create(@Valid @RequestBody CreateEventoDto dto) {
        return ResponseEntity.ok(eventoService.create(dto));
    }

    @Operation(
        summary = "Atualizar evento existente",
        description = "Atualiza os dados de um evento existente. O ID deve corresponder a um evento ativo."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Evento atualizado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = EventoDto.class))
    )
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado ou já excluído")
    @PutMapping("/{id}")
    public ResponseEntity<EventoDto> update(
            @Parameter(description = "ID do evento a ser atualizado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventoDto dto) {
        return ResponseEntity.ok(eventoService.update(id, dto));
    }

    @Operation(
        summary = "Excluir evento (soft delete)",
        description = "Marca o evento como excluído logicamente. O dado permanece no banco, mas não aparece nas listagens."
    )
    @ApiResponse(responseCode = "204", description = "Evento excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do evento a ser excluído", required = true)
            @PathVariable Long id) {
        eventoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}