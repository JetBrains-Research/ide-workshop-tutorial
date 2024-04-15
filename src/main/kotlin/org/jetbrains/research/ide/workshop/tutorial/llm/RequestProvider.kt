package org.jetbrains.research.ide.workshop.tutorial.llm

import org.jetbrains.research.ide.workshop.tutorial.services.MyHttpClientService

private const val GPT_3_5_TURBO = "gpt-3.5-turbo"
private const val GPT_4_TURBO = "gpt-4-turbo-preview"

class RequestProvider {
    private val prompt =
        """You are an experienced Kotlin developer with comprehensive knowledge in code quality.
           Generate 5 clear and concise names for the following Kotlin function.
           Follow proper naming conventions and the function's logic.
           As a result, return a list of 5 names separated by comma.
           Here is the function body: 
        """

    suspend fun sendRequest(userMessage: String): List<String> {
        val request = prepareRequest(prompt + userMessage)
        return MyHttpClientService().submit(request)
    }

    private fun prepareRequest(
        userMessage: String,
    ): OpenAIRequest {
        val openAIRequest = OpenAIRequest(
            model = GPT_4_TURBO,
            messages = listOf(ResponseMessage("user", userMessage))
        )
        return openAIRequest
    }
}