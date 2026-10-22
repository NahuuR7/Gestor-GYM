package com.belgrano.spartacusgym.repositories;

import com.belgrano.spartacusgym.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    // Al extender JpaRepository ya tenemos gratis métodos como:
    // save(), findAll(), findById(), deleteById()
}