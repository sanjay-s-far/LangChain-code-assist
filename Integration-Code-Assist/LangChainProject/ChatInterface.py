import asyncio
from pathlib import Path
import gradio as gr
from ContextTemplateBuilder import builder

preview_doc = Path(r"/Integration-Code-Assist/FinishedFiles")

def retrieve_latest_preview_file(file_name):
    print("Retrieve Block called")
    matching_files = [f for f in preview_doc.iterdir() if f.is_file() and f.name == file_name]
    if not matching_files:
        print(f"No file found with name: {file_name}")
        return "No preview file found."
    with open(matching_files[0], "r", encoding="utf-8") as f:
        return f.read()

def generate_preview_from_requirement(user_msg, project_name):
    try:
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)
        output = loop.run_until_complete(builder(user_msg, project_name))
        print(f"output file name : {output}")
        return output
    except Exception as e:
        return f"Error occurred: {str(e)}"

with gr.Blocks() as demo:
    gr.Markdown("## Spring Boot Project Generator")
    gr.Markdown("Fill the form below to generate your project and get a preview.")

    with gr.Row():
        with gr.Column(scale=1):
            project_name_box = gr.Textbox(label="Project Name")
            requirement_box = gr.Textbox(label="Requirement", lines=4, placeholder="Describe your requirement...")
            submit_btn = gr.Button("Generate Project")

        with gr.Column(scale=2):
            chatbot = gr.Chatbot(label="Generated Preview")
            clear = gr.Button("Clear Chat")

    def handle_form_submission(project_name, requirement, history):
        if not project_name or not requirement:
            return history + [["Form Submission", "Please fill both Project Name and Requirement."]]

        combined_input = f"Project: {project_name}\nRequirement: {requirement}"
        generate_preview_from_requirement(requirement, project_name)
        preview = retrieve_latest_preview_file(f"{project_name}.txt")
        return history + [[f"{project_name}", preview]]

    submit_btn.click(
        handle_form_submission,
        inputs=[project_name_box, requirement_box, chatbot],
        outputs=chatbot,
    )

    clear.click(lambda: [], None, chatbot, queue=False)

demo.launch()
