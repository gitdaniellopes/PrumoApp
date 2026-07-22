package br.com.prumoapp.ui.common_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.prumoapp.ui.theme.OnPrimaryColor
import br.com.prumoapp.ui.theme.PrimaryColor
import br.com.prumoapp.ui.theme.PrumoAppTheme
import org.jetbrains.compose.resources.painterResource
import prumoapp.shared.generated.resources.Res
import prumoapp.shared.generated.resources.ic_logo_white

@Composable
fun HeaderCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    backgroundColor: Color = PrimaryColor,
    contentColor: Color = OnPrimaryColor,
    topPadding: Dp = 50.dp,
    bottomPadding: Dp = 60.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = topPadding, bottom = bottomPadding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_logo_white),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor.copy(alpha = 0.9f)
            )
        }
    }
}

@Preview
@Composable
fun HeaderCardPreview() {
    PrumoAppTheme {
        HeaderCard(
            title = "Prumo App",
            subtitle = "Sua economia começa aqui!"
        )
    }
}
