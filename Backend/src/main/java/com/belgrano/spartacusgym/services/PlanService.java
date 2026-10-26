package com.belgrano.spartacusgym.services;

import com.belgrano.spartacusgym.models.Plan;
import com.belgrano.spartacusgym.repositories.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Plan> obtenerTodos() {
        return planRepository.findAll();
    }

    public Plan guardarPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public void eliminarPlan(Long id) {
        planRepository.deleteById(id);
    }
}