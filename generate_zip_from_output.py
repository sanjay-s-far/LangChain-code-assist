import os
import re
import zipfile


# --- Helper to clean file path strings ---
def clean_path(path: str) -> str:
    # Remove quotes, backticks, and strip whitespace
    return re.sub(r"[\"'`]", "", path.strip())


# Path to your output.txt (generated LLM output)
output_path = r"C:\Users\ss500058\Downloads\langchain-code-assist\langchain-code-assist\output.txt"

# Base directory where you'll write the extracted files
base_dir = r"C:\Users\ss500058\Downloads\langchain-code-assist\hello-world-service"

# Read the full LLM output text
with open(output_path, "r", encoding="utf-8") as f:
    text = f.read()

# --- STEP 1: Extract file paths and their code blocks ---
# Regex matches lines like: **2. src/main/java/com/example/helloworld/Application.java**
# and follows with ```lang ... ```
pattern = re.compile(
    r"\*\*\d+\.\s+([^*]+)\*\*.*?\n```(?:\w+)?\n(.*?)\n```",
    re.DOTALL
)

matches = pattern.findall(text)

if not matches:
    print("⚠️ No files/code blocks matched the pattern. Check your output.txt formatting.")
else:
    print(f"✅ Found {len(matches)} files in the output.")

# --- STEP 2: Write each file content to disk ---
for file_path_raw, code in matches:
    # Clean path
    file_path = clean_path(file_path_raw)

    # Construct full path
    full_path = os.path.join(base_dir, file_path)

    # Ensure parent folders exist
    os.makedirs(os.path.dirname(full_path), exist_ok=True)

    # Write file
    with open(full_path, "w", encoding="utf-8") as f:
        f.write(code.strip())

    print(f"✅ Saved: {file_path}")

# --- STEP 3: Zip the whole project folder ---
zip_file = os.path.join(os.path.dirname(base_dir), "world-service.zip")

with zipfile.ZipFile(zip_file, 'w', zipfile.ZIP_DEFLATED) as zipf:
    for foldername, _, filenames in os.walk(base_dir):
        for filename in filenames:
            filepath = os.path.join(foldername, filename)
            # Store files relative to the parent of base_dir
            relpath = os.path.relpath(filepath, os.path.dirname(base_dir))
            zipf.write(filepath, relpath)

print(f"\n✅ Project zipped here: {zip_file}")
