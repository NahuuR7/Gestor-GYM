-- ==============================================================================
-- Script de Inicialización: Spartacus GYM - Gestor Pro
-- Base de Datos: MySQL
-- ==============================================================================

-- 1. CREACIÓN DE LA BASE DE DATOS
CREATE DATABASE IF NOT EXISTS spartacus_gym;
USE spartacus_gym;

-- 2. CREACIÓN DE TABLAS (Se usa IF NOT EXISTS para no pisar la configuración de Spring Boot)
CREATE TABLE IF NOT EXISTS planes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    duracion_dias INT,
    precio DECIMAL(38,2)
);

CREATE TABLE IF NOT EXISTS miembros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(255),
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    edad INT,
    turno VARCHAR(255),
    metodo_pago VARCHAR(255),
    plan_id BIGINT,
    fecha_vencimiento DATE,
    FOREIGN KEY (plan_id) REFERENCES planes(id)
);

-- 3. LIMPIEZA DE DATOS PREVIOS (Evita duplicados si el script se corre más de una vez)
DELETE FROM miembros;
DELETE FROM planes;

-- 4. CARGA DE DATOS: PLANES Y TARIFAS
-- Se insertan los id manualmente para que coincidan con las claves foráneas de los miembros
INSERT INTO planes (id, nombre, duracion_dias, precio) VALUES
(1, 'Pase Libre', 30, 15000.00),
(2, 'Clases Grupales', 30, 12000.00),
(3, 'Pase Diario (Visitante)', 1, 2500.00),
(4, 'Musculación Anual', 365, 120000.00);

-- 5. CARGA DE DATOS: MIEMBROS (Casos de prueba para la interfaz)
INSERT INTO miembros (dni, nombre, apellido, edad, turno, metodo_pago, plan_id, fecha_vencimiento) VALUES
-- CASOS VIGENTES (Etiqueta Cian): Fechas de vencimiento a futuro
('10101010', 'Marta', 'Ramírez', 24, 'TARDE', 'EFECTIVO', 2, '2026-11-20'),
('12121212', 'Jorge', 'Herrera', 29, 'NOCHE', 'TARJETA', 1, '2026-11-25'),
('29103429', 'Edu', 'Reynaga', 26, 'TARDE', 'TRANSFERENCIA', 1, '2026-11-15'),
('99887766', 'Arnold', 'Schwarzenegger', 70, 'MANANA', 'EFECTIVO', 3, '2026-10-27'),

-- CASO VENCIDO (Etiqueta Roja): Fecha en el pasado. El Dashboard NO lo contará como vigente.
('11223344', 'Homero', 'Simpson', 39, 'MANANA', 'EFECTIVO', 1, '2026-10-01'),

-- CASO SIN PLAN (Etiqueta Naranja): Cliente registrado pero sin asignación de membresía aún.
('33445566', 'Elena', 'Castro', 31, 'NOCHE', '', NULL, NULL);

-- ==============================================================================
-- FIN DEL SCRIPT
-- ==============================================================================