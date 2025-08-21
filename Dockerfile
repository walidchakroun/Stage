# Use an OpenJDK base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR
COPY target/examen-coursclassroom-1.0-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","/app/app.jar"]