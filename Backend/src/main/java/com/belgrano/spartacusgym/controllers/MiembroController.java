package com.belgrano.spartacusgym.controllers;

import com.belgrano.spartacusgym.models.Miembro;
import com.belgrano.spartacusgym.models.Turno;
import com.belgrano.spartacusgym.services.MiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/miembros")
@CrossOrigin(origins = "*") 
public class MiembroController {

    private final MiembroService miembroService;

    @Autowired
    public MiembroController(MiembroService miembroService) {
        this.miembroService = miembroService;
    }

    // OBTENER TODOS o FILTRAR POR TURNO
    @GetMapping
    public ResponseEntity<List<Miembro>> obtenerMiembros(@RequestParam(name = "turno", required = false) Turno turno) {
        if (turno != null) {
            return ResponseEntity.ok(miembroService.obtenerPorTurno(turno));
        }
        return ResponseEntity.ok(miembroService.obtenerTodos());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Miembro> obtenerMiembroPorId(@PathVariable("id") Long id) {
        return miembroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crearMiembro(@RequestBody Miembro miembro) {
        try {
            Miembro nuevoMiembro = miembroService.registrarMiembro(miembro);
            return new ResponseEntity<>(nuevoMiembro, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMiembro(@PathVariable("id") Long id, @RequestBody Miembro miembro) {
        try {
            Miembro miembroActualizado = miembroService.actualizarMiembro(id, miembro);
            return ResponseEntity.ok(miembroActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMiembro(@PathVariable("id") Long id) {
        try {
            miembroService.eliminarMiembro(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}