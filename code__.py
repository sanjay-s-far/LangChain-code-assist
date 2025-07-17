
from langchain.prompts import PromptTemplate
import os

from langchain_google_genai import ChatGoogleGenerativeAI

os.environ["GOOGLE_API_KEY"] = "AIzaSyCnbHUPZfE18ZWaC6SfyeRuiPGEj__31e4"
# Step 1: Define your prompt
template = """
Generate a complete minimal Spring Boot application using Gradle that does the following:
- {requirement}
Include:
- `build.gradle` with required dependencies
- `Application.java` in appropriate package structure
- Any necessary controller or config classes
Project name: {project_name}
"""

prompt = PromptTemplate.from_template(template)

# Step 2: Initialize the LLM
llm = ChatGoogleGenerativeAI(model="gemini-2.0-flash", temperature=0.7)
# Step 3: Format your prompt
formatted_prompt = prompt.format(
    requirement="Expose a REST endpoint `/hello` that returns `Hello, World!` in JSON.",
    project_name="hello-world-service"
)

# Step 4: Generate the code
response = llm.invoke(formatted_prompt)

# Step 5: Save to project folder
project_dir = "C:\\Users\\ss500058\\Downloads\\langchain-code-assist\\langchain-code-assist\\"
os.makedirs(project_dir, exist_ok=True)

# You may need to parse the response intelligently (based on expected format)
# For simplicity, save the whole output for now
with open(os.path.join(project_dir, "output.txt"), "w", encoding="utf-8") as f:
    f.write(response.content)

print("✅ Project code generated. Check the 'generated_projects/hello-world-service/output.txt'")
