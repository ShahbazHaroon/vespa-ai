import os
import requests
from sentence_transformers import SentenceTransformer
from PyPDF2 import PdfReader
import json

VESPA_ENDPOINT = "http://localhost:8080/document/v1/documents/documents/docid/"

model = SentenceTransformer("all-MiniLM-L6-v2")

def extract_text(pdf_path):
    reader = PdfReader(pdf_path)
    return "\n".join(page.extract_text() or "" for page in reader.pages)

def chunk_text(text, max_len=500):
    words = text.split()
    for i in range(0, len(words), max_len):
        yield " ".join(words[i:i+max_len])

def index_document(pdf_path):
    title = os.path.basename(pdf_path)
    text = extract_text(pdf_path)
    for i, chunk in enumerate(chunk_text(text)):
        embedding = model.encode(chunk).tolist()
        doc_id = f"{title}-{i}"
        payload = {
            "fields": {
                "id": doc_id,
                "title": title,
                "content": chunk,
                "embedding": embedding,
                "author": "Unknown",
                "published_date": "2025-01-01"
            }
        }
        resp = requests.post(VESPA_ENDPOINT + doc_id, json=payload)
        print(doc_id, resp.status_code)

if __name__ == "__main__":
    os.makedirs("docs", exist_ok=True)
    for filename in os.listdir("docs"):
        if filename.lower().endswith(".pdf"):
            index_document(os.path.join("docs", filename))
