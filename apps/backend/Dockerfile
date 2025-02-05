# Use an OpenJDK 23 image as the base
FROM openjdk:23-jdk-slim AS base
# Install Maven (this is just an example; adjust versions and commands as needed)
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY apps/backend/pom.xml .
COPY apps/backend/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=base /app/target/currency-converter-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
