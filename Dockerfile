FROM openjdk:17

WORKDIR /app

# Copy the JAR file into the container
COPY target/JavaBankJavalin-1.0-SNAPSHOT.jar /app/myapp.jar

EXPOSE 8000

CMD ["java", "-jar", "myapp.jar"]