package org.jetbrains.research.ide.workshop.tutorial.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.jetbrains.research.ide.workshop.tutorial.helpers.CredentialsManager
import org.jetbrains.research.ide.workshop.tutorial.llm.OpenAIRequest
import org.jetbrains.research.ide.workshop.tutorial.llm.OpenAIResponse
import java.io.IOException

private const val openAiApiUrl = "https://api.openai.com/v1/chat/completions"

@Service
class MyHttpClientService : Disposable {
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            retryOnExceptionIf { _, cause -> cause is HttpRequestTimeoutException }
            exponentialDelay()
        }
    }

    suspend fun submit(requestData: OpenAIRequest): List<String> {
        val openApiKey = CredentialsManager.getInstance().getOpenAiApiKey()
        if (openApiKey.isNullOrEmpty())
            throw EmptyOrInvalidApiKeyException("OpenAI API Key is not provided")

        return client.let { client ->
            val request = prepareOpenAIRequest(openApiKey, requestData)
            client.prepareRequest(request).execute {
                parseHttpResponse(it)
            }
        }
    }

    private fun prepareOpenAIRequest(
        openApiKey: String,
        requestData: OpenAIRequest,
    ) = request {
        url(openAiApiUrl)
        accept(ContentType.Text.EventStream)
        contentType(ContentType.Application.Json)
        bearerAuth(openApiKey)
        setBody(requestData)
        method = HttpMethod.Post
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun parseHttpResponse(httpResponse: HttpResponse): List<String> {
        if (httpResponse.status != HttpStatusCode.OK) {
            throw IOException("HTTP response code: ${httpResponse.status}")
        }

        val json = Json { ignoreUnknownKeys = true }
        var result: List<String> = listOf()
        httpResponse.bodyAsChannel().toInputStream().use { inputStream ->
            val openAIResponse = json.decodeFromStream<OpenAIResponse>(inputStream)
            val choice = openAIResponse.choices.firstOrNull()
            if (choice != null) {
                result = choice.message.content.split(",").map { it.trim() }
            }
        }
        return result
    }

    override fun dispose() {
        client.close()
    }
}

class EmptyOrInvalidApiKeyException(message: String) : Exception(message)