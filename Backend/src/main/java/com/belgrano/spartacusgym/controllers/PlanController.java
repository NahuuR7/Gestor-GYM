package com.belgrano.spartacusgym.controllers;

import com.belgrano.spartacusgym.models.Plan;
import com.belgrano.spartacusgym.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@CrossOrigin(origins = "*")
public class PlanController {

    @Autowired
    private PlanService planService;

    // A diferencia de los miembros, los planes no necesitan paginación porque suelen ser pocos (5 a 10)
    @GetMapping
    public List<Plan> obtenerPlanes() {
        return planService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<Plan> crearPlan(@RequestBody Plan plan) {
        return new ResponseEntity<>(planService.guardarPlan(plan), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plan> actualizarPlan(@PathVariable("id") Long id, @RequestBody Plan plan) {
        plan.setId(id);
        return new ResponseEntity<>(planService.guardarPlan(plan), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlan(@PathVariable("id") Long id) {
        planService.eliminarPlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}