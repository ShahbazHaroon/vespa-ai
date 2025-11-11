# ---- Build Stage ----
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy only POM files first to leverage Docker caching
# Parent POM
COPY pom.xml ./
# Child POM
COPY sb-vespa-ai/pom.xml ./sb-vespa-ai/pom.xml

RUN mvn dependency:go-offline -B \
    && mvn verify -DskipTests -B

# Copy full source code
COPY sb-vespa-ai ./sb-vespa-ai
COPY indexer ./indexer

# Build the sb-vespa-ai module specifically
# -pl sb-vespa-ai → build only this module
# -am → also build any modules it depends on (like parent)
RUN mvn clean package -pl sb-vespa-ai -am -DskipTests -B

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy only the built JAR (avoid copying everything) into the container at /app
COPY --from=builder /app/sb-vespa-ai/target/sb-vespa-ai-*.jar sb-vespa-ai.jar

# Run as non-root user
# Create non-root user (use fixed UID/GID for reproducibility)
RUN groupadd -r spring && useradd -r -g spring spring

USER spring

# Expose the port your application will run on
EXPOSE 8080

# Use ENTRYPOINT for immutable command and CMD for arguments
ENTRYPOINT ["java", "-jar", "/app/sb-vespa-ai.jar"]
CMD []
