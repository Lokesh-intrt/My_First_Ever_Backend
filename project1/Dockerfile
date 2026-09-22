# =========================================================================
# STAGE 1: Build and Compile the Source Code
# =========================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

# Copy the dependency model file first to leverage Docker's caching engine
COPY pom.xml .

# Fetch your project dependencies so they are cached in an isolated layer
RUN mvn dependency:go-offline -B

# Copy your actual source code directories into the builder stage
COPY src ./src

# Compile the source code, trigger MapStruct processor code generations,
# and build the final executable production JAR file while skipping unit tests
RUN mvn clean package -DskipTests

# =========================================================================
# STAGE 2: Create the Lightweight Production Runtime Image
# =========================================================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy only the compiled execution artifact from the builder stage
COPY --from=builder /build/target/*.jar application.jar

# Document the container execution port boundary
EXPOSE 8082

# Start the Spring Boot application using memory-optimized JVM flags
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "application.jar"]
