# 🏥 SaludBoyacá - Sistema de Gestión de Citas Médicas

Sistema web desarrollado en **Java (Jakarta EE, JSP, Servlets)** con **MySQL**, orientado a la gestión de citas médicas, pacientes, médicos y autenticación segura con OTP.

Incluye arquitectura por capas (DAO - DTO - Controller), seguridad por sesión, generación de PDF y despliegue en contenedores Docker.

---

## 🚀 Características principales

- 🔐 Autenticación segura con OTP por correo
- 👥 Gestión de usuarios (Médicos, Recepcionistas, Enfermeros)
- 🏥 Gestión de pacientes
- 📅 Sistema completo de citas médicas
- ⏰ Validación de horarios de médicos
- 📄 Generación de PDF (citas / comprobantes)
- 🌐 Internacionalización (i18n con LocaleFilter)
- 🐳 Despliegue con Docker (Multi-stage build)
- ☁️ Compatible con Render, Koyeb, Cloud Run y Back4App

---

## 🧱 Tecnologías utilizadas

- Java 14+
- Jakarta EE (Servlets, JSP)
- Apache Tomcat 10+
- MySQL 8+
- Maven
- iText PDF
- JavaMail (OTP)
- Docker

---

## 📦 Estructura del proyecto

```
SaludBoyaca/
├── src/
│   ├── controller/        # Servlets
│   ├── dao/               # Acceso a datos
│   ├── dto/               # Objetos de datos
│   ├── util/              # OTPService, LocaleFilter, PDFGenerator
│   └── webapp/
│       ├── views/         # JSP
│       └── WEB-INF/
├── Dockerfile
├── pom.xml
└── README.md
```

---

## ⚙️ Requisitos

- JDK 14 o superior
- Maven 3.6+
- MySQL 8+
- Apache Tomcat 10+
- Docker (opcional)

---

## 🛠️ Instalación en otro PC

### 1. Clonar el proyecto

```bash
git clone https://github.com/tu-usuario/saludboyaca.git
cd saludboyaca
```

---

### 2. Crear base de datos

```sql
CREATE DATABASE saludboyaca;
```

Base de datos script:

```bash
src/main/resources/Script.sql
```

Configurar conexión en el proyecto:

```java
jdbc:mysql://localhost:3306/saludboyaca
usuario
contraseña
```

---

### 3. Compilar proyecto

```bash
mvn clean package
```

Se genera:

```
target/saludboyaca.war
```

---

### 4. Desplegar en Tomcat

Copiar WAR:

```bash
cp target/saludboyaca.war tomcat/webapps/
```

Iniciar Tomcat:

```bash
startup.bat   (Windows)
startup.sh    (Linux/Mac)
```

Abrir en navegador:

```
http://localhost:8080/saludboyaca
```

---


## 📧 Flujo de autenticación

1. Usuario ingresa credenciales  
2. Sistema valida usuario en BD  
3. Se genera OTP con SecureRandom  
4. OTP se envía al correo  
5. Usuario ingresa OTP  
6. Se crea sesión activa  

---

## 📅 Módulo de citas

- Crear cita  
- Editar cita  
- Eliminar cita  
- Ver detalle de cita  
- Validar disponibilidad de horario médico  
- Estados:
  - PROGRAMADA  
  - CONFIRMADA  
  - ATENDIDA  
  - CANCELADA  

---

## 🌐 Internacionalización (i18n)

Controlado por `LocaleFilter`

Idiomas disponibles:

- Español (default)
- English
- Italiano

---

## 🐳 Docker

### Construir imagen

```bash
docker build -t saludboyaca .
```

### Ejecutar contenedor

```bash
docker run -p 8080:8080 saludboyaca
```

---

## ☁️ Despliegue en la nube

Variables de entorno:

- DB_URL
- DB_USER
- DB_PASS
- EMAIL_PASS

Puerto: `8080`

---

## 📊 Arquitectura del sistema

```
JSP (Vista)
   ↓
Servlet (Controlador)
   ↓
DAO (Lógica de datos)
   ↓
MySQL (Base de datos)
```

---

## 📄 Generación de PDF

Usa **iTextPDF** para:

- Comprobantes de citas
- Listados de citas
- Reportes médicos

---

## 🧠 Aprendizajes del proyecto

- Arquitectura MVC real en Java EE
- Seguridad con OTP
- Uso de filtros (AuthFilter, LocaleFilter)
- Separación DAO / DTO
- Generación de PDF dinámico
- Despliegue en cloud con Docker
