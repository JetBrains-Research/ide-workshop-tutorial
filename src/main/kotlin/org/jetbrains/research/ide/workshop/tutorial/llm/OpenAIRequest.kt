package org.jetbrains.research.ide.workshop.tutorial.llm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [See OpenAI documentation](https://beta.openai.com/docs/api-reference/chat)
 */
@Serializable
data class OpenAIRequest(
    @SerialName("messages")
    val messages: List<ResponseMessage>,

    @SerialName("model")
    val model: String,

    @SerialName("frequency_penalty")
    val frequencyPenalty: Double? = null,

    @SerialName("logit_bias")
    val logitBias: Map<String, Int>? = null,

    @SerialName("temperature")
    val temperature: Double? = null,

    @SerialName("top_p")
    val topP: Double? = null,

    @SerialName("n")
    val suggestionCount: Int? = null,

    @SerialName("stream")
    var stream: Boolean? = null,

    @SerialName("stop")
    val stop: String? = null,

    @SerialName("max_tokens")
    val maxTokens: Int? = null,

    @SerialName("presence_penalty")
    val presencePenalty: Double? = null,

    @SerialName("user")
    val user: String? = null,
)
