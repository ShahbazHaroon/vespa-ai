# Vespa AI Semantic Search Project

This is a multi-module project for semantic search using Vespa, Spring Boot, and Python indexing.

[![Vespa.ai](https://img.shields.io/badge/Vespa-AI-blue)](https://vespa.ai)

## Overview

**Vespa.ai** is a platform for running large-scale, real-time AI-driven search, retrieval, data serving, ranking, and recommendation applications.

This project demonstrates how to build a **smart search engine** using Vespa, capable of hybrid retrieval (keyword + vector + structured data) and ranking with machine-learned models.

## Modules
- **indexer**: Python scripts for PDF text extraction and embeddings. Add your PDFs to `docs/`.
- **sb-vespa-ai**: Spring Boot application (e.g., for API endpoints).
- **vespa-app**: Vespa configuration (schemas, hosts, services).

### Key Capabilities

- **Hybrid Retrieval:** Combine keyword/text search, vector search, and structured data search.
- **On-the-fly Ranking:** Rank retrieved data using machine learning models and tensor computations.
- **Real-time & Scalable:** Supports large document volumes, many queries per second, and low latency.
- **Advanced AI Use Cases:** Recommendation, personalization, and retrieval-augmented generation (RAG).
- **API Access:** Vespa exposes HTTP/JSON APIs for feeding documents, running queries, and managing clusters.
- **Deployment Options:** Available as open-source or via Vespa Cloud.

### Example Use Case

An organization with many PDF documents, manuals, internal wikis, or support articles:

1. Extract text from documents and split into chunks.
2. Generate embeddings for each chunk.
3. Index the data in Vespa (text fields + embedding vectors + metadata).
4. Use a Spring Boot REST API to query Vespa and retrieve relevant chunks with hybrid search + ranking.

---

## Tech Stack

- **Vespa.ai** – Real-time search and ranking engine, vector database for semantic search
- **Spring Boot** – REST API for querying Vespa
- **Python** – Data indexing and PDF processing
- **Docker & Docker Compose** – Containerized environment for Vespa and the app
- **Maven** – Build tool for Spring Boot application
- **HTTP/JSON** – API communication with Vespa

---

## Prerequisites

- Docker & Docker Compose installed
- Java 17+ (for Spring Boot)
- Python 3.8+ (for the indexer)

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/vespa-ai-project.git
cd vespa-ai-project
```
Wait a few minutes for Vespa to start and initialize. The schema deployment is handled automatically by Docker Compose.

### 2. Start Vespa with Docker Compose
```bash
docker-compose up -d
```

### 3. Index Documents
```bash
cd indexer
pip install -r requirements.txt

# Create docs folder if it doesn't exist
mkdir -p docs

# Copy your PDF files into the 'docs' folder
# Then run the indexer
python index_docs.py
```

### 4. Run the Spring Boot API
```bash
cd ../springboot-app
./mvnw spring-boot:run
```
The API will start on http://localhost:8080.

### 5. Query the API
```bash
curl "http://localhost:8080/api/search?q=how do I reset password"
curl "http://localhost:8080/api/search?q=how to create company email account
curl "http://localhost:8080/api/search?q=what are best password security practices
```

### 6. Example Response
```bash
{
  "root": {
    "children": [
      {
        "id": "manuals.pdf-2",
        "fields": {
          "title": "manuals.pdf",
          "content": "To reset your password, go to ...",
          "score": 0.94
        }
      }
    ]
  }
}
```

---

## Docker Instructions (Optional)

### Run Vespa in a Docker container:
```bash
docker run -d --name vespa -p 8080:8080 vespaengine/vespa
```

### Use Docker Compose:
```bash
docker-compose up --build
```

1. This will automatically:
2. Start Vespa container on port 8080
3. Deploy schemas
4. Make the Spring Boot container API ready to use on port 8081

Access:

Swagger: http://localhost:8081/swagger-ui/index.html

Search: http://localhost:8081/api/v1/search?q=example