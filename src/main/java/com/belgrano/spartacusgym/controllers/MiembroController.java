package com.belgrano.spartacusgym.controllers;

import com.belgrano.spartacusgym.models.Miembro;
import com.belgrano.spartacusgym.services.MiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/miembros")
@CrossOrigin(origins = "*") // Permite que tu frontend HTML local se conecte sin errores de CORS
public class MiembroController {

    private final MiembroService miembroService;

    @Autowired
    public MiembroController(MiembroService miembroService) {
        this.miembroService = miembroService;
    }

    // GET: http://localhost:8080/api/miembros
    @GetMapping
    public ResponseEntity<List<Miembro>> obtenerTodosLosMiembros() {
        return ResponseEntity.ok(miembroService.obtenerTodos());
    }

    // GET: http://localhost:8080/api/miembros/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Miembro> obtenerMiembroPorId(@PathVariable Long id) {
        return miembroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: http://localhost:8080/api/miembros
    @PostMapping
    public ResponseEntity<?> crearMiembro(@RequestBody Miembro miembro) {
        try {
            Miembro nuevoMiembro = miembroService.registrarMiembro(miembro);
            return new ResponseEntity<>(nuevoMiembro, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Devuelve un error 400 Bad Request si falla una validación (ej. DNI duplicado)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT: http://localhost:8080/api/miembros/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMiembro(@PathVariable Long id, @RequestBody Miembro miembro) {
        try {
            Miembro miembroActualizado = miembroService.actualizarMiembro(id, miembro);
            return ResponseEntity.ok(miembroActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: http://localhost:8080/api/miembros/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMiembro(@PathVariable Long id) {
        try {
            miembroService.eliminarMiembro(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}