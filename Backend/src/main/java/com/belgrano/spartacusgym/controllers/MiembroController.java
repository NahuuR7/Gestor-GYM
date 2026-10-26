package com.belgrano.spartacusgym.controllers;

import com.belgrano.spartacusgym.dto.MiembroDTO;
import com.belgrano.spartacusgym.models.Miembro;
import com.belgrano.spartacusgym.services.MiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/miembros")
public class MiembroController {

    @Autowired
    private MiembroService miembroService;

    @GetMapping
    public Page<MiembroDTO> obtenerMiembros(
            @RequestParam(name = "turno", defaultValue = "") String turno,
            @RequestParam(name = "busqueda", defaultValue = "") String busqueda,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
            
        Pageable pageable = PageRequest.of(page, size);
        return miembroService.obtenerPaginados(turno, busqueda, pageable);
    }

    @GetMapping("/estadisticas")
    public Map<String, Long> obtenerEstadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", miembroService.contarTotal());
        
        // AHORA MANDAMOS EL NÚMERO REAL DE ACTIVOS
        stats.put("activos", miembroService.contarActivos()); 
        
        stats.put("manana", miembroService.contarPorTurno("MANANA"));
        stats.put("tarde", miembroService.contarPorTurno("TARDE"));
        stats.put("noche", miembroService.contarPorTurno("NOCHE"));
        return stats;
    }

    @PostMapping
    public ResponseEntity<MiembroDTO> guardarMiembro(@Valid @RequestBody Miembro miembro) {
        return new ResponseEntity<>(miembroService.guardarMiembro(miembro), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiembroDTO> actualizarMiembro(@PathVariable(name = "id") Long id, @Valid @RequestBody Miembro miembro) {
        miembro.setId(id);
        return new ResponseEntity<>(miembroService.guardarMiembro(miembro), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMiembro(@PathVariable(name = "id") Long id) {
        miembroService.eliminarMiembro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}