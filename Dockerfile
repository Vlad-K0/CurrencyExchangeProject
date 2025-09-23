# --- Этап 1: Сборка .war файла с помощью Maven и JDK 21 (LTS) ---
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# --- Этап 2: Финальный образ на базе Tomcat 11 и JDK 21 (LTS) ---
# Эта комбинация существует и является стабильной
FROM tomcat:11.0-jdk21-temurin

# Очищаем папку webapps от стандартных приложений
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем наш .war файл в папку приложений Tomcat
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080