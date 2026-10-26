package com.belgrano.spartacusgym.repositories;

import com.belgrano.spartacusgym.models.Miembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, Long> {
    
    // 1. Busca solo por turno
    Page<Miembro> findByTurno(String turno, Pageable pageable);
    
    // 2. NUEVO: Busca por Nombre, Apellido O DNI
    Page<Miembro> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDniContainingIgnoreCase(
            String nombre, String apellido, String dni, Pageable pageable);

    // 3. NUEVO: Consulta JPQL optimizada que respeta el Turno y busca en los 3 campos de texto
    @Query("SELECT m FROM Miembro m WHERE m.turno = :turno AND " +
           "(LOWER(m.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(m.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(m.dni) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    Page<Miembro> buscarPorTurnoYTextoExtenso(@Param("turno") String turno, @Param("busqueda") String busqueda, Pageable pageable);

    // Contadores para el Dashboard
    long countByTurno(String turno);
    long countByFechaVencimientoGreaterThanEqual(LocalDate fecha);
}