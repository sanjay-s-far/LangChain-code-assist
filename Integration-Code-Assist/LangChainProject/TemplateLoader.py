import random
import string
from pathlib import Path  
import chromadb
from chromadb.utils import embedding_functions

if __name__ == "__main__":
    # Define paths
    template_dir = Path(r"C:\\Users\\sd116131\\LangChain-code-assist\\Integration-Code-Assist\\Templates")
    presistance_memory = r"C:\\Users\\sd116131\\LangChain-code-assist\\Integration-Code-Assist\\LangChainProject\\Template_pr_memory"

    # Initialize ChromaDB client
    chroma_client = chromadb.PersistentClient(path=presistance_memory)

    # Set up embedding function
    google_ef = embedding_functions.GoogleGenerativeAiEmbeddingFunction(
        api_key="AIzaSyCnbHUPZfE18ZWaC6SfyeRuiPGEj__31e4"
    )

    # Create or get collection
    collection = chroma_client.get_or_create_collection(
        name="integration_standard_templates",
        embedding_function=google_ef
    )

    # Upsert documents from template directory
    for file_path in template_dir.rglob("*"):
        if file_path.is_file() and file_path.name != ".DS_Store":
            try:
                with open(file_path, "r", encoding="utf-8") as f:
                    template_content = f.read()
                random_id = ''.join(random.choices(string.ascii_letters + string.digits, k=5))
                doc_id = f"{file_path.name}_{random_id}"

                collection.upsert(
                    documents=[template_content],
                    metadatas=[{"filename": file_path.name}],
                    ids=[doc_id]
                )
                print(f"Upserted: {doc_id}")

            except Exception as e:
                print(f"Error storing {file_path.name}: {e}")

    # Query the collection
    results = collection.query(
        query_texts=["pubsub"],  # Chroma will embed this for you
        n_results=2
    )
    print("\nQuery Results:")
    print(results)

    # View all documents in the collection
    print("\nAll Documents in Collection:")
    all_docs = collection.get()
    for doc_id, metadata in zip(all_docs['ids'], all_docs['metadatas']):
        print(f"ID: {doc_id}, Filename: {metadata['filename']}")
