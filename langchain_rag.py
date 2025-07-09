from langchain.document_loaders import TextLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.embeddings import HuggingFaceEmbeddings
from langchain.vectorstores import FAISS
from langchain.chains import RetrievalQA
from langchain.chat_models import ChatAnthropic
import os

# Load documents
file_paths = [
    'build.gradle',
    'settings.gradle',
    'HELP.md',
    'src/main/java/com/example/demo/DemoApplication.java',
    'src/main/java/com/example/demo/HelloController.java',
    'src/main/resources/application.properties'
]

documents = []
for path in file_paths:
    if os.path.exists(path):
        loader = TextLoader(path)
        documents.extend(loader.load())

print(documents)
# Split documents
# splitter = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=50)
# docs = splitter.split_documents(documents)

# # Embed documents using local model
# embedding_model = HuggingFaceEmbeddings(model_name="sentence-transformers/all-MiniLM-L6-v2")
# db = FAISS.from_documents(docs, embedding_model)

# # Create retriever
# retriever = db.as_retriever()

# # Set up Claude via LangChain
# llm = ChatAnthropic(
#     model="claude-3-sonnet-20240229",  # or claude-3-haiku
#     temperature=0,
#     max_tokens=1024,
#     anthropic_api_key="your_claude_api_key"
# )

# # Create RAG chain
# qa_chain = RetrievalQA.from_chain_type(llm=llm, retriever=retriever)

# # Interactive loop
# while True:
#     query = input("\nAsk a question about your Spring Boot project (or type 'exit'): ")
#     if query.lower() == 'exit':
#         break
#     answer = qa_chain.run(query)
#     print("\n🧠 Answer:\n", answer)
