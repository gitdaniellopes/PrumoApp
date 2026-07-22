package br.com.prumoapp.ui.features.auth.login.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.prumoapp.ui.common_components.HeaderCard
import br.com.prumoapp.ui.features.auth.login.LoginUiState
import br.com.prumoapp.ui.theme.BackgroundColor
import br.com.prumoapp.ui.theme.PrumoAppTheme
import org.jetbrains.compose.resources.stringResource
import prumoapp.shared.generated.resources.Res
import prumoapp.shared.generated.resources.login_subtitle
import prumoapp.shared.generated.resources.login_terms
import prumoapp.shared.generated.resources.login_title

@Preview
@Composable
fun LoginContentPreview() {
    PrumoAppTheme {
        LoginContent(uiState = LoginUiState.Idle)
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onGoogleSignIn: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.fillMaxSize()
            .background(BackgroundColor)
    ) {
        HeaderCard(
            title = stringResource(Res.string.login_title),
            subtitle = stringResource(Res.string.login_subtitle),
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = 300.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoginBenefitSection(
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GoogleSignInButton(
                        isLoading = uiState is LoginUiState.Loading,
                        onClick = onGoogleSignIn,
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    )

                    if (uiState is LoginUiState.Error){
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.errorMessage,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.login_terms),
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }












}