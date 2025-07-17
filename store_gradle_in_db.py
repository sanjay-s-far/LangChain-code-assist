import os
import chromadb
import numpy as np

# --- Optional dummy embedding ---
def embedding_model(text):
    return np.zeros(768, dtype=np.float32)

if __name__ == "__main__":
    gradle_dir = r"C:\Users\ss500058\Downloads\langchain-code-assist\langchain-code-assist\Sample_project\spring-hello-world-project"
    persist_dir = r"C:\Users\ss500058\Downloads\langchain-code-assist\langchain-code-assist\Sample_project\spring-hello-world-project"
    os.makedirs(persist_dir, exist_ok=True)

    # ✅ New client initialization
    chroma_client = chromadb.PersistentClient(path=persist_dir)

    # Create or get collection
    collection = chroma_client.get_or_create_collection(name="gradle_files")

    file_id = 0
    for root, dirs, files in os.walk(gradle_dir):
        for file in files:
            if file.endswith(".gradle"):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, "r", encoding="utf-8") as f:
                        gradle_content = f.read()

                    collection.add(
                        documents=[gradle_content],
                        metadatas=[{"filename": file, "path": file_path}],
                        ids=[f"gradle_{file_id}"]
                    )

                    print(f"✅ Stored: {file}")
                    file_id += 1

                except Exception as e:
                    print(f"❌ Error storing {file}: {e}")

    print("✅ All .gradle files stored successfully.")