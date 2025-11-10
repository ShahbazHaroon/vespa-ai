# sb-redis-annotation
This is a sample Spring Boot project for Redis using annotation

Advantages of This Approach:
Performance: For GET requests, the cache will prevent repetitive database calls, improving response time.

Data Consistency: When new data is added, deleted, or updated, only the relevant cache entries are modified, ensuring that the cache stays in sync with the database.

Selective Cache Updates: By using @CacheEvict, @CachePut, and @Cacheable appropriately, you can ensure that your cache only contains relevant and up-to-date data.

## Run Locally
```bash
./mvnw spring-boot:run
or
mvn spring-boot:run
or
java -jar target/app.jar

## Run in Docker:
docker build -t springboot-h2-demo .
docker run -p 8080:8080 springboot-h2-demo

## Run with Docker Compose:
> Note: When using Docker Compose, you don’t need to manually run `docker build` — it’s handled by `docker compose up --build`.
docker compose up --build

# Stop containers
docker compose down