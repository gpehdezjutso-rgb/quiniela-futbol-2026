# ── STAGE 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar pom.xml primero para aprovechar el cache de capas de Docker
# Si solo cambia código fuente (no dependencias), este paso no se re-ejecuta
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar el resto del proyecto y compilar el WAR
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── STAGE 2: Runtime ──────────────────────────────────────────────────────────
FROM tomcat:9.0-jdk17

# Limpiar apps de ejemplo de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR generado como ROOT.war para que sea accesible en /
COPY --from=builder /app/target/quiniela-mundial-2026.war \
     /usr/local/tomcat/webapps/ROOT.war

# Puerto que expone Tomcat
EXPOSE 8080

CMD ["catalina.sh", "run"]
