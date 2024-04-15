package org.jetbrains.research.ide.workshop.tutorial.llm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [See OpenAI documentation](https://platform.openai.com/docs/api-reference/chat/object)
 */
@Serializable
data class OpenAIResponse(
    @SerialName("id")
    val id: String,

    @SerialName("choices")
    val choices: List<ResponseChatChoice>,

    @SerialName("created")
    val created: Long,

    @SerialName("system_fingerprint")
    val systemFingerprint: String,

    @SerialName("object")
    val responseObject: String,

    @SerialName("usage")
    val usage: ResponseUsage,
)

@Serializable
data class ResponseChatChoice(
    @SerialName("finish_reason")
    val finishReason: String,

    @SerialName("index")
    val index: Long,

    @SerialName("message")
    val message: ResponseMessage,
)

@Serializable
data class ResponseMessage(
    @SerialName("role")
    val role: String,

    @SerialName("content")
    val content: String,
)

@Serializable
data class ResponseUsage(
    @SerialName("completion_tokens")
    val completionTokens: Long,

    @SerialName("prompt_tokens")
    val promptTokens: Long,

    @SerialName("total_tokens")
    val totalTokens: Long,
)