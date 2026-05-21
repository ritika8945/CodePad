package com.codepad.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codepad.app.data.model.Language
import com.codepad.app.data.repository.CodeExecutionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CodePadUiState(
    val code: String = Language.ALL.first().defaultSnippet,
    val input: String = "",
    val output: List<String> = emptyList(),
    val selectedLanguage: Language = Language.ALL.first(),
    val isExecuting: Boolean = false,
    val activeTab: Tab = Tab.EDITOR,
    val serverUrl: String = "https://codepad-j320.onrender.com",
    val showSettings: Boolean = false
)

enum class Tab { EDITOR, INPUT, CONSOLE }

class CodePadViewModel : ViewModel() {

    private val repository = CodeExecutionRepository()
    private val _uiState = MutableStateFlow(CodePadUiState())
    val uiState: StateFlow<CodePadUiState> = _uiState.asStateFlow()

    fun updateCode(code: String) {
        _uiState.update { it.copy(code = code) }
    }

    fun updateInput(input: String) {
        _uiState.update { it.copy(input = input) }
    }

    fun clearInput() {
        _uiState.update { it.copy(input = "") }
    }

    fun selectLanguage(language: Language) {
        _uiState.update {
            it.copy(
                selectedLanguage = language,
                code = language.defaultSnippet
            )
        }
    }

    fun switchTab(tab: Tab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun toggleSettings() {
        _uiState.update { it.copy(showSettings = !it.showSettings) }
    }

    fun updateServerUrl(url: String) {
        _uiState.update { it.copy(serverUrl = url) }
        repository.baseUrl = url
    }

    fun clearOutput() {
        _uiState.update { it.copy(output = emptyList()) }
    }

    fun newFile() {
        val defaultLang = Language.ALL.first()
        _uiState.update {
            it.copy(
                code = defaultLang.defaultSnippet,
                selectedLanguage = defaultLang,
                input = "",
                output = emptyList()
            )
        }
    }

    fun executeCode() {
        val state = _uiState.value
        if (state.code.isBlank()) return

        _uiState.update { it.copy(isExecuting = true, output = listOf("Executing code...")) }

        viewModelScope.launch {
            val result = repository.executeCode(
                code = state.code,
                language = state.selectedLanguage.id,
                input = state.input
            )

            val combinedOutput = mutableListOf<String>()
            combinedOutput.addAll(result.output)
            result.error.forEach { line ->
                combinedOutput.add("Error: $line")
            }
            if (combinedOutput.isEmpty()) {
                combinedOutput.add(
                    if (result.success) "Code executed successfully."
                    else "Execution completed with no output."
                )
            }

            _uiState.update {
                it.copy(
                    output = combinedOutput,
                    isExecuting = false,
                    activeTab = Tab.CONSOLE
                )
            }
        }
    }
}
