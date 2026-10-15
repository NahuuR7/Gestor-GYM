package com.belgrano.spartacusgym.repositories;

import com.belgrano.spartacusgym.models.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, Long> {
    
    // Spring Data JPA creará la consulta SQL automáticamente gracias a este nombre:
    // SELECT * FROM miembros WHERE dni = ?
    Optional<Miembro> findByDni(String dni);
    
    // SELECT count(*) > 0 FROM miembros WHERE dni = ?
    boolean existsByDni(String dni);
}