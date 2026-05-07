# =========================
# ETAPA 1 -> BUILD MAVEN
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom primero para cache
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar proyecto completo
COPY src ./src

# Compilar WAR
RUN mvn clean package -DskipTests


# =========================
# ETAPA 2 -> TOMCAT
# =========================
FROM tomcat:10.1-jdk21

# Eliminar apps por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar WAR compilado
COPY --from=build /app/target/SaludBoyaca.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
