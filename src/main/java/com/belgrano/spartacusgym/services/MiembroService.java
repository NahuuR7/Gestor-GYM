package com.belgrano.spartacusgym.services;

import com.belgrano.spartacusgym.models.Miembro;
import com.belgrano.spartacusgym.repositories.MiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MiembroService {

    private final MiembroRepository miembroRepository;

    @Autowired
    public MiembroService(MiembroRepository miembroRepository) {
        this.miembroRepository = miembroRepository;
    }

    // Obtener todos los miembros (para la tabla del frontend)
    @Transactional(readOnly = true)
    public List<Miembro> obtenerTodos() {
        return miembroRepository.findAll();
    }

    // Obtener un miembro por su ID
    @Transactional(readOnly = true)
    public Optional<Miembro> obtenerPorId(Long id) {
        return miembroRepository.findById(id);
    }

    // Guardar un nuevo miembro (con validaciones de negocio)
    @Transactional
    public Miembro registrarMiembro(Miembro miembro) {
        // Validar DNI duplicado
        if (miembroRepository.existsByDni(miembro.getDni())) {
            throw new IllegalArgumentException("Ya existe un miembro registrado con el DNI: " + miembro.getDni());
        }
        
        // Validar edad mínima
        if (miembro.getEdad() < 16) {
            throw new IllegalArgumentException("El miembro debe tener al menos 16 años para ingresar al gimnasio.");
        }
        
        return miembroRepository.save(miembro);
    }

    // Actualizar un miembro existente
    @Transactional
    public Miembro actualizarMiembro(Long id, Miembro datosActualizados) {
        Miembro miembroExistente = miembroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Miembro no encontrado con ID: " + id));

        // Actualizamos los campos
        miembroExistente.setNombre(datosActualizados.getNombre());
        miembroExistente.setApellido(datosActualizados.getApellido());
        miembroExistente.setEdad(datosActualizados.getEdad());
        miembroExistente.setTurno(datosActualizados.getTurno());
        miembroExistente.setObservaciones(datosActualizados.getObservaciones());
        
        // No actualizamos el DNI ni la fecha de registro por seguridad

        return miembroRepository.save(miembroExistente);
    }

    // Eliminar un miembro
    @Transactional
    public void eliminarMiembro(Long id) {
        if (!miembroRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar. Miembro no encontrado con ID: " + id);
        }
        miembroRepository.deleteById(id);
    }
}