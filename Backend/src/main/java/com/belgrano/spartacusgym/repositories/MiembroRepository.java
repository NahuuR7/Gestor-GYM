package com.belgrano.spartacusgym.repositories;

import com.belgrano.spartacusgym.models.Miembro;
import com.belgrano.spartacusgym.models.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, Long> {
    // Método para validar si un DNI ya existe antes de guardar
    Optional<Miembro> findByDni(String dni);
    
    // Método para el filtro de la pantalla principal
    List<Miembro> findByTurno(Turno turno);
}