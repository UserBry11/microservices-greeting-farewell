# Use a clean, lightweight Java 17 runtime
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy your built JAR from target/ to the container
COPY target/*.jar app.jar

# Run it
ENTRYPOINT ["java", "-jar", "app.jar"]
