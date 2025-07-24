import os
from pathlib import Path
from langchain_community.vectorstores import Chroma
from langchain_core.prompts import PromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI, GoogleGenerativeAIEmbeddings

# === RAG path setup ===
chroma_dir = r"C:\Users\sd116131\LangChain-code-assist\Integration-Code-Assist\LangChainProject\Template_pr_memory"
project_dir = r"C:\Users\sd116131\LangChain-code-assist\Integration-Code-Assist\FinishedFiles"
os.makedirs(project_dir, exist_ok=True)
os.environ["GOOGLE_API_KEY"] = "AIzaSyCnbHUPZfE18ZWaC6SfyeRuiPGEj__31e4"

# === Prompt templates ===
template = """
You are a software engineer assistant.

Below is a Business Requirements Document (BRD) context:
{context}

From this BRD, generate a complete minimal Spring Boot application using Gradle that satisfies all identified functional and non-functional requirements.

🧩 Functional Requirements (FR):
- Use the extracted FR list and implement or stub each one.
- Include endpoint definitions in appropriate controllers.
- For each FR, add a comment like: `// {FR_ID}: {FR_Description} (Priority: {Priority})`

⚙️ Non-Functional Requirements (NFR):
- Reflect NFRs in configuration, structure, or comments.
- Examples:
  - For uptime: add comment about monitoring.
  - For encryption: use Spring Security config.
  - For scalability: modular and stateless components.

📦 Output Requirements:
1. **File Structure**
    - `{project_name}/`
      - `build.gradle`
      - `settings.gradle`
      - `gradlew`, `gradlew.bat`, `gradle/wrapper/`
      - `src/main/java/{package_structure}/`
          - `{project_name}Application.java`
          - `controller/`
          - `config/`
      - `src/main/resources/application.properties`
      - `brd/{brd_filename}` (place uploaded BRD here)
      - `README.md` (map each FR/NFR to implemented feature)

2. **Code Expectations**
    - Use `@RestController` for API endpoints
    - Include `SecurityConfig.java` if auth is mentioned
    - Use comments to trace BRD mapping in code
    - Stub features with `TODO` if incomplete (e.g., FR4: Mobile support)

📝 Guidelines:
- Be clean and minimal, not exhaustive.
- Prioritize clarity and traceability from BRD to code.
- Use Java 17 and Gradle 8.5
- Output should be structured like a real Spring Boot repo.

Project Name: {project_name}
"""

fallback_template = """
Generate a minimal Spring Boot project using Gradle named **{project_name}**.

- Use Java 17 and Gradle 8.5
- Include:
  - `build.gradle`, `settings.gradle`, Gradle wrapper files
  - `src/main/java/.../{project_name}Application.java`
  - 1–2 sample endpoints
  - `SecurityConfig.java` with basic HTTP auth
  

S
"""


# === MAIN BUILDER FUNCTION ===
def builder(requirement, project_name):
    similarity_threshold = 0.6
    embeddings = GoogleGenerativeAIEmbeddings(model="models/embedding-001")
    vectorstore = Chroma(persist_directory=chroma_dir, embedding_function=embeddings)

    # Search the vectorstore using the requirement
    docs_with_scores = vectorstore.similarity_search_with_score(requirement, k=3)

    # Filter by score
    relevant_context = "\n\n".join(
        doc.page_content for doc, score in docs_with_scores if score > similarity_threshold
    )

    # Format prompt
    if relevant_context.strip():
        prompt = PromptTemplate.from_template(template)
        formatted_prompt = prompt.format(
            context=relevant_context,
            requirement=requirement,
            project_name=project_name
        )
        print("Using retrieved context from ChromaDB.")
    else:
        prompt = PromptTemplate.from_template(fallback_template)
        formatted_prompt = prompt.format(
            requirement=requirement,
            project_name=project_name
        )
        print("No relevant context found. Falling back to default prompt.")

    # LLM call
    llm = ChatGoogleGenerativeAI(model="gemini-2.0-flash", temperature=0.7)
    response = llm.invoke(formatted_prompt)

    # Save base response
    output_path = os.path.join(project_dir, f"{project_name}.txt")
    with open(output_path, "w", encoding="utf-8") as f:
        f.write(response.content)


    return f"{project_name}.txt"