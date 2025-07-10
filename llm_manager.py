# llm_manager.py

from langchain_google_genai import ChatGoogleGenerativeAI
from typing import List, Optional

class GeminiLLMManager:
    def __init__(self, api_keys: List[str], model: str = "gemini-pro"):
        self.api_keys = api_keys
        self.model = model
        self.current_index = 0
        self.llm = self._initialize_llm()

    def _initialize_llm(self) -> Optional[ChatGoogleGenerativeAI]:
        while self.current_index < len(self.api_keys):
            try:
                llm = ChatGoogleGenerativeAI(
                    model=self.model,
                    google_api_key=self.api_keys[self.current_index]
                )
                test_response = llm.invoke("Hello")
                if test_response:
                    return llm
            except Exception as e:
                print(f"API key at index {self.current_index} failed: {e}")
                self.current_index += 1
        print("All API keys failed.")
        return None

    def get_llm(self) -> Optional[ChatGoogleGenerativeAI]:
        return self.llm

    def list_available_llms(self) -> List[str]:
        return [f"Gemini ({key[:5]}...)" for key in self.api_keys]
