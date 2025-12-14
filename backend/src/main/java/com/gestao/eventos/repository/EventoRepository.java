package com.gestao.eventos.repository;

import com.gestao.eventos.entity.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("SELECT e FROM Evento e WHERE e.id = :id AND e.deleted = false")
    Optional<Evento> findNotDeletedById(@Param("id") Long id);

    @Query("SELECT e FROM Evento e WHERE e.deleted = false")
    Page<Evento> findAllNotDeleted(Pageable pageable);

    @Modifying
    @Query("UPDATE Evento e SET e.deleted = true WHERE e.id = :id")
    void markAsDeleted(@Param("id") Long id);
}