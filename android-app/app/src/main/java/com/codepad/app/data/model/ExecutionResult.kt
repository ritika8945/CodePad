package com.codepad.app.data.model

data class ExecutionResult(
    val output: List<String> = emptyList(),
    val error: List<String> = emptyList(),
    val success: Boolean = false
)
