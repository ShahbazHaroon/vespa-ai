# ---- Build Stage ----
FROM eclipse-temurin:17-jdk-jammy AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage Docker layer caching
COPY pom.xml mvnw ./
COPY .mvn .mvn

 Download dependencies (cached between builds)
RUN ./mvnw dependency:go-offline -B

# Copy application source code
COPY src src

# Build application without running tests
RUN ./mvnw clean package -DskipTests -B

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy only the built JAR (avoid copying everything) into the container at /app
COPY --from=builder /app/target/*.jar app.jar

# Run as non-root user
# Create non-root user (use fixed UID/GID for reproducibility)
RUN groupadd -r spring && useradd -r -g spring spring

USER spring

# Expose the port your application will run on
EXPOSE 8080

# Use ENTRYPOINT for immutable command and CMD for arguments
ENTRYPOINT ["java", "-jar", "/app/sb-vespa-ai.jar"]
CMD []