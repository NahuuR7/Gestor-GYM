package com.belgrano.spartacusgym.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Atrapa los errores si los datos no cumplen los requisitos (@NotBlank, @Size)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    // Atrapa cualquier otro error crítico para que la app no colapse
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrores(Exception ex) {
        
        // LÍNEA MÁGICA: Obliga a Java a imprimir la falla real en la consola
        ex.printStackTrace(); 
        
        return new ResponseEntity<>("Ocurrió un error en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}