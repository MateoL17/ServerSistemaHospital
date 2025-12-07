-- Inicialización de la base de datos hospital_db

-- Crear usuario si no existe (MySQL 8+)
CREATE USER IF NOT EXISTS 'hospital_user'@'%' IDENTIFIED BY 'hospital123';
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'hospital_user'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- Usar la base de datos
USE hospital_db;

-- Tabla paciente
CREATE TABLE IF NOT EXISTS paciente (
                                        cedula VARCHAR(10) PRIMARY KEY,
                                        nombre VARCHAR(100) NOT NULL,
                                        correo VARCHAR(100) NOT NULL UNIQUE,
                                        edad INT NOT NULL CHECK (edad > 0 AND edad <= 120),
                                        direccion VARCHAR(200),
                                        activo BOOLEAN DEFAULT TRUE,
                                        fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_paciente_activo ON paciente(activo);
CREATE INDEX idx_paciente_nombre ON paciente(nombre);
CREATE INDEX idx_paciente_correo ON paciente(correo);

-- Datos de prueba (opcional)
INSERT INTO paciente (cedula, nombre, correo, edad, direccion) VALUES
                                                                   ('1718123293', 'Moises Caicedo', 'moices.caicedo23@gmail.com', 24, 'Av. Amazonas'),
                                                                   ('0912345678', 'María Lopez', 'maria.lopez@gmail.com', 20, 'Av. 6 de diciembre'),
                                                                   ('1723456789', 'Fernando López', 'fernando.lopez@gmail.com', 40, 'Av. Shyris')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- Procedimiento para limpiar datos de prueba
DELIMITER $$
CREATE PROCEDURE limpiar_datos_prueba()
BEGIN
    DELETE FROM paciente WHERE cedula LIKE '99%';
END $$
DELIMITER ;