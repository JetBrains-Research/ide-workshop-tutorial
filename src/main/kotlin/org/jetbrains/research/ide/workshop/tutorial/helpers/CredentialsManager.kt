package org.jetbrains.research.ide.workshop.tutorial.helpers

import com.intellij.credentialStore.CredentialAttributes
import com.intellij.credentialStore.generateServiceName
import com.intellij.ide.passwordSafe.PasswordSafe
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service


private const val OPEN_API_KEY = "OPENAI_API_KEY"

/**
 * [See documentation on persisting sensitive data](https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html)
 */
@Service(Service.Level.APP)
class CredentialsManager {
    companion object {
        fun getInstance(): CredentialsManager = service<CredentialsManager>()
    }

    fun getOpenAiApiKey(): String? {
        return getCredentials(OPEN_API_KEY)
    }

    private fun getCredentials(key: String): String? {
        val attributes = createCredentialAttributes(key)
        val credentials = PasswordSafe.instance.get(attributes)
        return credentials?.getPasswordAsString()
    }

    private fun createCredentialAttributes(key: String): CredentialAttributes {
        return CredentialAttributes(
            generateServiceName("LLM", key)
        )
    }
}