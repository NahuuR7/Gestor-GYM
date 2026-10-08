package com.belgrano.spartacusgym.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "miembros")
public class Miembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private Integer edad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDate.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}