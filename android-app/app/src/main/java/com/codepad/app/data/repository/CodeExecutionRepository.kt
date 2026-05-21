package com.codepad.app.data.repository

import com.codepad.app.data.model.ExecutionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CodeExecutionRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val DEFAULT_BASE_URL = "https://codepad-j320.onrender.com"
        private const val EXECUTE_PATH = "/api/execute"
        private val JSON_TYPE = "application/json; charset=utf-8".toMediaType()
    }

    var baseUrl: String = DEFAULT_BASE_URL

    suspend fun executeCode(
        code: String,
        language: String,
        input: String = ""
    ): ExecutionResult = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("code", code)
                put("language", language)
                put("input", input)
            }

            val request = Request.Builder()
                .url("$baseUrl$EXECUTE_PATH")
                .post(json.toString().toRequestBody(JSON_TYPE))
                .header("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext ExecutionResult(
                    error = listOf("Server error: ${response.code}"),
                    success = false
                )
            }

            val result = JSONObject(body)
            val output = result.optJSONArray("output")?.toStringList() ?: emptyList()
            val error = result.optJSONArray("error")?.toStringList() ?: emptyList()
            val success = result.optBoolean("success", false)

            ExecutionResult(output = output, error = error, success = success)
        } catch (e: Exception) {
            ExecutionResult(
                error = listOf("Execution failed: ${e.localizedMessage ?: "Unknown error"}"),
                success = false
            )
        }
    }

    private fun JSONArray.toStringList(): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until length()) {
            val s = optString(i, "")
            if (s.isNotBlank()) list.add(s)
        }
        return list
    }
}
