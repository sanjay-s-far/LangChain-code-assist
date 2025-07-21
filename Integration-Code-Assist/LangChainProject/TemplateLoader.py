import random
import string

import chromadb
from chromadb.utils import embedding_functions
from pathlib import Path

if __name__ == "__main__":
    template_dir = Path(r"/Users/sanjays/Documents/Inegration Code Assist /Templates")
    presistance_memory = r"/Users/sanjays/Documents/Inegration Code Assist /LangChainProject/Template_pr_memory"

    chroma_client = chromadb.PersistentClient(path=presistance_memory)
    google_ef = embedding_functions.GoogleGenerativeAiEmbeddingFunction(api_key="AIzaSyCnbHUPZfE18ZWaC6SfyeRuiPGEj__31e4")
    collection = chroma_client.get_or_create_collection(name="integration_standard_templates", embedding_function=google_ef)

    for file_path in template_dir.rglob("*"):
        if file_path.is_file() and file_path.name !=".DS_Store":
            try:
                with open(file_path, "r", encoding="utf-8") as f:
                    template_content = f.read()
                random_id = ''.join(random.choices(string.ascii_letters + string.digits, k=5))
                collection.add(
                    documents=[template_content],
                    metadatas=[{"filename": file_path.name}],
                    ids=[f"{file_path.name}_{random_id}"]
                )

            except Exception as e:
                print(f" Error storing {file_path.name}: {e}")

    results = collection.query(
        query_texts=["pubsub"], # Chroma will embed this for you
        n_results=2 # how many results to return
        )
    print(results)