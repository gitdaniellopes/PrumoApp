package br.com.prumoapp.data.auth

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import br.com.prumoapp.core.logging.AppLogger
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

private const val TAG = "AndroidGoogleAuthUiProvider"

class AndroidGoogleAuthUiProvider(
    private val context: Context,
    private val credentialManager: CredentialManager
) : GoogleSignInProvider {

    override suspend fun signInWithGoogle(): GoogleSignInResult? {

        return try {
            val result = credentialManager.getCredential(
                context = context,
                request = getCredentialRequest()
            )
            handleSignIn(result.credential)
        } catch (e: GetCredentialCancellationException) {
            AppLogger.e(TAG, "Login cancelado pelo usuário: ${e.message}", e)
            null
        } catch (e: NoCredentialException) {
            AppLogger.e(TAG, "Nenhuma credencial encontrada: ${e.message}", e)
            GoogleSignInResult.Error("Nenhum conta do goole encontrada no dispositivo")
        } catch (e: GetCredentialException) {
            AppLogger.e(TAG, "Erro no CredentialManager: ${e.message}", e)
            GoogleSignInResult.Error("Erro ao acessar as credenciais do google")
        } catch (e: Exception) {
            AppLogger.e(TAG, "Erro inesperado: ${e.message}", e)
            GoogleSignInResult.Error("Erro inesperado ao acessar as credenciais do google")
        }
    }

    private fun handleSignIn(credential: Credential): GoogleSignInResult? = when {
        credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                GoogleSignInResult.Credential(
                    idToken = googleIdTokenCredential.idToken
                )
            } catch (e: GoogleIdTokenParsingException) {
                AppLogger.e(TAG, "Erro ao processar token do google: ${e.message}", e)
                GoogleSignInResult.Error("Erro ao processar a credencial do google")
            }
        }

        else -> {
            AppLogger.e(TAG, "Tipo de credencial não suportada: ${credential.type}")
            GoogleSignInResult.Error("Tipo de credencial não suportada")
        }
    }

    private fun getCredentialRequest(): GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(getGoogleIdOption())
        .build()

    private fun getGoogleIdOption(): GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(GOOGLE_WEB_CLIENT_ID)
        .setAutoSelectEnabled(false)
        .build()
}