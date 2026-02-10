FROM eclipse-temurin:17.0.18_8-jdk
WORKDIR /app
COPY task_16/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]