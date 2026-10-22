package com.belgrano.spartacusgym.services;

import com.belgrano.spartacusgym.models.Miembro;
import com.belgrano.spartacusgym.models.Turno;
import com.belgrano.spartacusgym.repositories.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MiembroService {

    private final MiembroRepository miembroRepository;

    @Autowired
    public MiembroService(MiembroRepository miembroRepository) {
        this.miembroRepository = miembroRepository;
    }

    public List<Miembro> obtenerTodos() {
        return miembroRepository.findAll();
    }

    public Optional<Miembro> obtenerPorId(Long id) {
        return miembroRepository.findById(id);
    }
    
    public List<Miembro> obtenerPorTurno(Turno turno) {
        return miembroRepository.findByTurno(turno);
    }

    public Miembro registrarMiembro(Miembro miembro) {
        // Validación de DNI duplicado
        Optional<Miembro> existente = miembroRepository.findByDni(miembro.getDni());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El DNI " + miembro.getDni() + " ya está registrado.");
        }
        return miembroRepository.save(miembro);
    }

    public Miembro actualizarMiembro(Long id, Miembro datosActualizados) {
        return miembroRepository.findById(id).map(miembro -> {
            // Si el DNI cambia, validar que el nuevo DNI no pertenezca a otra persona
            if (!miembro.getDni().equals(datosActualizados.getDni())) {
                Optional<Miembro> existente = miembroRepository.findByDni(datosActualizados.getDni());
                if (existente.isPresent()) {
                    throw new IllegalArgumentException("El DNI ya pertenece a otro miembro.");
                }
            }
            
            miembro.setDni(datosActualizados.getDni());
            miembro.setNombre(datosActualizados.getNombre());
            miembro.setApellido(datosActualizados.getApellido());
            miembro.setEdad(datosActualizados.getEdad());
            miembro.setTurno(datosActualizados.getTurno());
            
            return miembroRepository.save(miembro);
        }).orElseThrow(() -> new IllegalArgumentException("Miembro no encontrado con el ID: " + id));
    }

    public void eliminarMiembro(Long id) {
        if (!miembroRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar. El miembro no existe.");
        }
        miembroRepository.deleteById(id);
    }
}