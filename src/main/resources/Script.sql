-- =============================
-- TABLAS
-- =============================

CREATE TABLE usuarios (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombres VARCHAR(80) NOT NULL,
 apellidos VARCHAR(80) NOT NULL,
 documento VARCHAR(20) NOT NULL UNIQUE,
 email VARCHAR(100) NOT NULL UNIQUE,
 username VARCHAR(50) NOT NULL UNIQUE,
 password VARCHAR(255) NOT NULL,
 rol ENUM('MEDICO','RECEPCIONISTA','ENFERMERO') NOT NULL,
 especialidad VARCHAR(80),
 lang_preferido VARCHAR(5) DEFAULT 'es',
 activo TINYINT(1) DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE pacientes (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombres VARCHAR(80) NOT NULL,
 apellidos VARCHAR(80) NOT NULL,
 documento VARCHAR(20) NOT NULL UNIQUE,
 fecha_nacimiento DATE NOT NULL,
 telefono VARCHAR(20),
 email VARCHAR(100),
 eps VARCHAR(80) NOT NULL,
 vereda_barrio VARCHAR(80)
) ENGINE=InnoDB;

CREATE TABLE especialidades (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(80) NOT NULL,
 descripcion VARCHAR(200)
) ENGINE=InnoDB;

CREATE TABLE horarios (
 id INT AUTO_INCREMENT PRIMARY KEY,
 id_medico INT NOT NULL,
 dia_semana TINYINT NOT NULL,
 hora_inicio TIME NOT NULL,
 hora_fin TIME NOT NULL,
 max_citas INT DEFAULT 10,
 FOREIGN KEY (id_medico) REFERENCES usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE citas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 id_paciente INT NOT NULL,
 id_medico INT NOT NULL,
 id_especialidad INT NOT NULL,
 fecha_cita DATE NOT NULL,
 hora_cita TIME NOT NULL,
 motivo VARCHAR(300),
 estado ENUM('PROGRAMADA','CONFIRMADA','ATENDIDA','CANCELADA') DEFAULT 'PROGRAMADA',
 observaciones VARCHAR(500),
 fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
 id_registrado_por INT,
 FOREIGN KEY (id_paciente) REFERENCES pacientes(id),
 FOREIGN KEY (id_medico) REFERENCES usuarios(id),
 FOREIGN KEY (id_especialidad) REFERENCES especialidades(id),
 FOREIGN KEY (id_registrado_por) REFERENCES usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE otp_tokens (
 id INT AUTO_INCREMENT PRIMARY KEY,
 id_usuario INT NOT NULL,
 codigo VARCHAR(6) NOT NULL,
 fecha_gen DATETIME DEFAULT CURRENT_TIMESTAMP,
 expira_en DATETIME NOT NULL,
 usado TINYINT(1) DEFAULT 0,
 FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE log_accesos (
 id INT AUTO_INCREMENT PRIMARY KEY,
 id_usuario INT,
 username VARCHAR(50),
 accion VARCHAR(50) NOT NULL,
 ip VARCHAR(45),
 resultado ENUM('EXITO','FALLO') NOT NULL,
 fecha DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =============================
-- DATOS DE PRUEBA
-- =============================

INSERT INTO especialidades (nombre) VALUES
 ('Medicina General'),
 ('Odontología'),
 ('Pediatría'),
 ('Ginecología'),
 ('Optometría');

INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad) VALUES
 ('Carlos Ernesto', 'Pedraza Rondón', '1052345678', 'cpedraza@saludboyaca.gov.co', 'cpedraza', 'admin123', 'MEDICO', 'Medicina General'),
 ('Laura Fernanda', 'Gómez Ruiz', '1052345681', 'lgomez@saludboyaca.gov.co', 'lgomez', 'medico123', 'MEDICO', 'Pediatría'),
 ('María Eugenia', 'Suárez Cely', '1052345679', 'msuarez@saludboyaca.gov.co', 'msuarez', 'enfermero1', 'ENFERMERO', NULL),
 ('Jorge Hernando', 'Báez Morales', '1052345680', 'jbaez@saludboyaca.gov.co', 'jbaez', 'recep123', 'RECEPCIONISTA', NULL);

INSERT INTO pacientes (nombres, apellidos, documento, fecha_nacimiento, telefono, email, eps, vereda_barrio) VALUES
 ('Juan David', 'Rodríguez Pérez', '1001234567', '2005-06-15', '3101234567', 'juan@gmail.com', 'Nueva EPS', 'Centro'),
 ('Ana María', 'López Torres', '1001234568', '1998-03-22', '3119876543', 'ana@gmail.com', 'Sanitas', 'La Pradera');

INSERT INTO horarios (id_medico, dia_semana, hora_inicio, hora_fin, max_citas) VALUES
 (1, 1, '08:00:00', '12:00:00', 8),
 (1, 3, '14:00:00', '18:00:00', 8),
 (2, 2, '08:00:00', '12:00:00', 10);

INSERT INTO citas (id_paciente, id_medico, id_especialidad, fecha_cita, hora_cita, motivo, estado, id_registrado_por) VALUES
 (1, 1, 1, '2026-05-10', '09:00:00', 'Dolor de cabeza', 'PROGRAMADA', 4),
 (2, 2, 3, '2026-05-11', '10:00:00', 'Control pediátrico', 'CONFIRMADA', 4);