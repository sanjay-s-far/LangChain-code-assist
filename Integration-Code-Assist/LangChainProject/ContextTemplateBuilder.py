import os
from langchain_community.vectorstores import Chroma
from langchain_core.prompts import PromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI, GoogleGenerativeAIEmbeddings

chroma_dir = "/Integration-Code-Assist/LangChainProject/Template_pr_memory"
project_dir = "/Integration-Code-Assist/FinishedFiles/"
os.makedirs(project_dir, exist_ok=True)
os.environ["GOOGLE_API_KEY"] = "AIzaSyCnbHUPZfE18ZWaC6SfyeRuiPGEj__31e4"


template = """
You are given some relevant code templates and context below:
{context}
Generate a complete minimal Spring Boot application using Gradle that does the following:
- {requirement}
Include:
- `build.gradle` with required dependencies
- Gradle Wrapper files (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.properties`) using Gradle version 8.5
- `settings.gradle` file
- A main application class named `{project_name}Application.java` in an appropriate package structure, ideally combining the project name and logical structure
- Use proper configuration and maintain a clean, standard package structure
Project name: {project_name}
"""

fallback_template = """
Generate a complete minimal Spring Boot application using Gradle that does the following:
- {requirement}
Include:
- `build.gradle` with required dependencies
- Gradle Wrapper files (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.properties`) using Gradle version 8.5
- `settings.gradle` file
- A main application class named `{project_name}Application.java` in an appropriate package structure, ideally combining the project name and logical structure
- Use proper configuration and maintain a clean, standard package structure
Project name: {project_name}
"""
def builder(requirement, project_name):
    similarity_threshold = 0.6
    embeddings = GoogleGenerativeAIEmbeddings(model="models/embedding-001")
    vectorstore = Chroma(persist_directory=chroma_dir, embedding_function=embeddings)

    docs_with_scores = vectorstore.similarity_search_with_score(requirement, k=3)
    relevant_context = "\n\n".join(
        doc.page_content for doc, score in docs_with_scores if score > similarity_threshold
    )
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


    llm = ChatGoogleGenerativeAI(model="gemini-2.0-flash", temperature=0.7)
    response = llm.invoke(formatted_prompt)

    output_path = os.path.join(project_dir, f"{project_name}.txt")
    with open(output_path, "w", encoding="utf-8") as f:
        f.write(response.content)