FROM openjdk:17

WORKDIR /app

# Copy the JAR file into the container
COPY target/JavaBankJavalin-1.0-SNAPSHOT.jar /app/myapp.jar
COPY .env /app/.env
COPY wait-for-it.sh /app/wait-for-it.sh

RUN chmod +x /app/wait-for-it.sh

EXPOSE 8000

CMD ["/app/wait-for-it.sh", "postgres-server:5432", "--", "java", "-jar", "/app/myapp.jar"]