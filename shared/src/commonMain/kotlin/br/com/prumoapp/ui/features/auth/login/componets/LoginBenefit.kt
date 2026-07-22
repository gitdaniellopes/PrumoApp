package br.com.prumoapp.ui.features.auth.login.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.prumoapp.ui.theme.PrimaryColor
import br.com.prumoapp.ui.theme.PrumoAppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import prumoapp.shared.generated.resources.Res
import prumoapp.shared.generated.resources.ic_bolt
import prumoapp.shared.generated.resources.ic_cloud
import prumoapp.shared.generated.resources.ic_lock
import prumoapp.shared.generated.resources.login_benefit_cloud_desc
import prumoapp.shared.generated.resources.login_benefit_cloud_title
import prumoapp.shared.generated.resources.login_benefit_fast_desc
import prumoapp.shared.generated.resources.login_benefit_fast_title
import prumoapp.shared.generated.resources.login_benefit_secure_desc
import prumoapp.shared.generated.resources.login_benefit_secure_title

private data class LoginBenefit(
    val icon: Painter,
    val title: String,
    val description: String
)

@Composable
private fun loginBenefits() = listOf(
    LoginBenefit(
        icon = painterResource(Res.drawable.ic_lock),
        title = stringResource(Res.string.login_benefit_secure_title),
        description = stringResource(Res.string.login_benefit_secure_desc)
    ),
    LoginBenefit(
        icon = painterResource(Res.drawable.ic_cloud),
        title = stringResource(Res.string.login_benefit_cloud_title),
        description = stringResource(Res.string.login_benefit_cloud_desc)
    ),
    LoginBenefit(
        icon = painterResource(Res.drawable.ic_bolt),
        title = stringResource(Res.string.login_benefit_fast_title),
        description = stringResource(Res.string.login_benefit_fast_desc)
    )
)

@Composable
fun LoginBenefitSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        loginBenefits().forEach { benefit ->
            LoginBenefitItem(benefit = benefit)
        }
    }
}

@Composable
private fun LoginBenefitItem(
    benefit: LoginBenefit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(44.dp)
                .background(
                    color = PrimaryColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = benefit.icon,
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = benefit.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                text = benefit.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginBenefitSectionPreview() {
    PrumoAppTheme {
        LoginBenefitSection(
            modifier = Modifier.padding(24.dp)
        )
    }
}













