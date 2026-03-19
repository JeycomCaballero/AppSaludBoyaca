
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


DROP SCHEMA IF EXISTS `biblioteca` ;


CREATE SCHEMA IF NOT EXISTS `biblioteca` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `biblioteca` ;


CREATE TABLE IF NOT EXISTS `autor` (
  `id_autor` INT NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(80) NOT NULL,
  `apellidos` VARCHAR(80) NOT NULL,
  `nacionalidad` VARCHAR(90) NOT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  CHECK(nombres != NULL),
  CHECK(apellidos != NULL),
  CHECK(nacionalidad != NULL),
  PRIMARY KEY (`id_autor`))
ENGINE = InnoDB

DEFAULT CHARACTER SET = utf8mb4;



CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(70) NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id_categoria`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;



CREATE TABLE IF NOT EXISTS `editorial` (
  `id_editorial` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(70) NOT NULL,
  `pais` VARCHAR(90) NOT NULL,
  `sitio_web` VARCHAR(90) NOT NULL,
  CHECK(nombre != NULL),
CHECK(sitio_web != NULL),
  PRIMARY KEY (`id_editorial`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;



CREATE TABLE IF NOT EXISTS `libro` (
  `id_libro` INT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(80) NOT NULL,
  `isbn` VARCHAR(50) NOT NULL,
  `año_publicacion` DATE,
  `num_paginas` INT NOT NULL,
  `disponible` TINYINT(1) NOT NULL,
  `id_categoria` INT NOT NULL,
  `id_editorial` INT NOT NULL,
  CHECK(titulo != NULL),
CHECK(isbn != NULL),
CHECK(año_publicacion != NULL),
CHECK(id_categoria != NULL),
CHECK(id_editorial != NULL),
  PRIMARY KEY (`id_libro`),
  CONSTRAINT `fk_libro_categoria1`
    FOREIGN KEY (`id_categoria`)
    REFERENCES `categoria` (`id_categoria`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_libro_editorial1`
    FOREIGN KEY (`id_editorial`)
    REFERENCES `editorial` (`id_editorial`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)

ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE INDEX `fk_libro_categoria1_idx` ON `libro` (`id_categoria` ASC) VISIBLE;

CREATE INDEX `fk_libro_editorial1_idx` ON `libro` (`id_editorial` ASC) VISIBLE;

CREATE UNIQUE INDEX `isbn_UNIQUE` ON `libro` (`isbn` ASC) VISIBLE;


CREATE TABLE IF NOT EXISTS `libro_autor` (
  `id_autor_libro` INT NOT NULL AUTO_INCREMENT,
  `id_libro` INT NOT NULL,
  `id_autor` INT NOT NULL,
  PRIMARY KEY (`id_autor_libro`),
  CONSTRAINT `fk_libro_autor_libro`
    FOREIGN KEY (`id_libro`)
    REFERENCES `libro` (`id_libro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_libro_autor_autor1`
    FOREIGN KEY (`id_autor`)
    REFERENCES `autor` (`id_autor`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE INDEX `fk_libro_autor_libro_idx` ON `libro_autor` (`id_libro` ASC) VISIBLE;

CREATE INDEX `fk_libro_autor_autor1_idx` ON `libro_autor` (`id_autor` ASC) VISIBLE;

CREATE UNIQUE INDEX `uk_libro_autor` ON `libro_autor` (`id_libro` ASC, `id_autor` ASC) VISIBLE;



CREATE TABLE IF NOT EXISTS `rol` (
  `id_rol` INT NOT NULL AUTO_INCREMENT,
  `tipoRol` VARCHAR(45) NULL,
  PRIMARY KEY (`id_rol`))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `documento` INT NOT NULL,
  `nombre` VARCHAR(70) NOT NULL,
  `apellido` VARCHAR(70) NOT NULL,
  `email` VARCHAR(90) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL,
  `estado` TINYINT(1) NOT NULL,
  `id_rol` INT NOT NULL,
  CHECK(documento != NULL),
CHECK(nombre != NULL),
CHECK(apellido != NULL),
CHECK(email != NULL),
  PRIMARY KEY (`id_usuario`),
  CONSTRAINT `fk_usuario_rol1`
    FOREIGN KEY (`id_rol`)
    REFERENCES `rol` (`id_rol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE UNIQUE INDEX `documento_UNIQUE` ON `usuario` (`documento` ASC) VISIBLE;

CREATE INDEX `fk_usuario_rol1_idx` ON `usuario` (`id_rol` ASC) VISIBLE;



CREATE TABLE IF NOT EXISTS `prestamo` (
  `id_prestamo` INT NOT NULL AUTO_INCREMENT,
  `fecha_prestamo` DATE NOT NULL,
  `fecha_devolucion_esperada` DATE NOT NULL,
  `fecha_devolucion_real` DATE NULL,
  `estado` TINYINT NULL,
  `id_libro` INT NOT NULL,
  `id_usuario` INT NOT NULL,
  CHECK(id_libro != NULL),
CHECK(id_usuario != NULL),
  PRIMARY KEY (`id_prestamo`),
  CONSTRAINT `fk_prestamo_libro1`
    FOREIGN KEY (`id_libro`)
    REFERENCES `libro` (`id_libro`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_prestamo_usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `usuario` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE INDEX `fk_prestamo_libro1_idx` ON `prestamo` (`id_libro` ASC) VISIBLE;

CREATE INDEX `fk_prestamo_usuario1_idx` ON `prestamo` (`id_usuario` ASC) VISIBLE;



CREATE TABLE IF NOT EXISTS `multa` (
  `id_multa` INT NOT NULL AUTO_INCREMENT,
  `monto` DOUBLE NOT NULL,
  `fecha_generacion` DATE NOT NULL,
  `fecha_pago` DATE NULL,
  `estado` TINYINT(1) NOT NULL,
  `id_prestamo` INT NOT NULL,
  CHECK(id_prestamo != NULL),
  PRIMARY KEY (`id_multa`),
  CONSTRAINT `fk_multa_prestamo1`
    FOREIGN KEY (`id_prestamo`)
    REFERENCES `prestamo` (`id_prestamo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE INDEX `fk_multa_prestamo1_idx` ON `multa` (`id_prestamo` ASC) VISIBLE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
