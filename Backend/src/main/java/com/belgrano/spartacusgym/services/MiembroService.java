package com.belgrano.spartacusgym.services;

import com.belgrano.spartacusgym.dto.MiembroDTO;
import com.belgrano.spartacusgym.models.Miembro;
import com.belgrano.spartacusgym.repositories.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MiembroService {

    @Autowired
    private MiembroRepository miembroRepository;

    public Page<MiembroDTO> obtenerPaginados(String turno, String busqueda, Pageable pageable) {
        Page<Miembro> miembros;
        
        if (!turno.isEmpty() && !busqueda.isEmpty()) {
            // Usa la nueva consulta optimizada (Turno + Nombre/Apellido/DNI)
            miembros = miembroRepository.buscarPorTurnoYTextoExtenso(turno, busqueda, pageable);
        } else if (!turno.isEmpty()) {
            // Busca solo por turno
            miembros = miembroRepository.findByTurno(turno, pageable);
        } else if (!busqueda.isEmpty()) {
            // Le enviamos la palabra clave a los 3 campos: Nombre, Apellido y DNI
            miembros = miembroRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDniContainingIgnoreCase(
                    busqueda, busqueda, busqueda, pageable);
        } else {
            // Sin filtros, trae a todos
            miembros = miembroRepository.findAll(pageable);
        }
        
        return miembros.map(this::convertirADto);
    }

    public long contarTotal() {
        return miembroRepository.count();
    }

    public long contarPorTurno(String turno) {
        return miembroRepository.countByTurno(turno);
    }

    public long contarActivos() {
        return miembroRepository.countByFechaVencimientoGreaterThanEqual(LocalDate.now());
    }

    public MiembroDTO guardarMiembro(Miembro miembro) {
        if (miembro.getPlan() != null && miembro.getPlan().getDuracionDias() != null) {
            miembro.setFechaVencimiento(LocalDate.now().plusDays(miembro.getPlan().getDuracionDias()));
        } else {
            miembro.setFechaVencimiento(null);
        }
        
        Miembro guardado = miembroRepository.save(miembro);
        return convertirADto(guardado);
    }

    public void eliminarMiembro(Long id) {
        miembroRepository.deleteById(id);
    }

    private MiembroDTO convertirADto(Miembro miembro) {
        MiembroDTO dto = new MiembroDTO();
        dto.setId(miembro.getId());
        dto.setDni(miembro.getDni());
        dto.setNombre(miembro.getNombre());
        dto.setApellido(miembro.getApellido());
        dto.setEdad(miembro.getEdad());
        dto.setTurno(miembro.getTurno());
        dto.setPlan(miembro.getPlan());
        dto.setMetodoPago(miembro.getMetodoPago());
        dto.setFechaVencimiento(miembro.getFechaVencimiento());
        return dto;
    }
}