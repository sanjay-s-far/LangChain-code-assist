from langchain_community.document_loaders import TextLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.chains import LLMChain
from langchain.prompts import PromptTemplate
from llm_manager import GeminiLLMManager
import os

# List of Gemini API keys (fallback supported)
API_KEYS = [
    "AIzaSyAAx6gaP1F46GVYbpiA3RVINS-Zlhw9FFs",
    "AIzaSyAnotherKeyExample1234567890",
    "AIzaSyBackupKeyExample0987654321"
]

# Initialize Gemini LLM with fallback support
manager = GeminiLLMManager(api_keys=API_KEYS)
llm = manager.get_llm()

if not llm:
    print("❌ No working Gemini LLM available. Exiting.")
    exit()

# Load project files
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

# Split documents into chunks
splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=100)
docs = splitter.split_documents(documents)

# Combine all chunks into a single context string
context = "\n\n".join([doc.page_content for doc in docs])

# Define prompt template
prompt_template = PromptTemplate(
    input_variables=["context", "question"],
    template="""
You are an assistant that answers questions based on the following Spring Boot project files:

{context}

Question: {question}
Answer:"""
)

# Create LLM chain
qa_chain = LLMChain(llm=llm, prompt=prompt_template)

# Interactive loop
while True:
    query = input("\nAsk a question about your Spring Boot project (or type 'exit'): ")
    if query.lower() == 'exit':
        break
    answer = qa_chain.invoke({"context": context, "question": query})
    print("\n🧠 Answer:\n", answer)
