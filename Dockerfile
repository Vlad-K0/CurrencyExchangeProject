# Используем официальный образ Tomcat
FROM tomcat:9.0-jdk17-openjdk-slim

# Копируем собранный WAR-файл в папку webapps Tomcat
# Убедись, что твой проект собран командой 'mvn clean package'
# И что имя файла совпадает с именем твоего артефакта
COPY target/CurrencyExchangeProject-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/

# Настройка переменных окружения для подключения к базе данных
ENV DB_URL=jdbc:postgresql://database:5432/currency_exchange
ENV DB_USER=postgres
ENV DB_PASSWORD=postgres

# Запускаем Tomcat
CMD ["catalina.sh", "run"]