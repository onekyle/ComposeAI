
import AppScreenUiState.Loading
import AppScreenUiState.Success
import analytics.AnalyticsHelper
import analytics.LocalAnalyticsHelper
import analytics.NoOpAnalyticsHelper
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import ui.screens.chat.ChatScreen
import ui.screens.welcome.WelcomeScreen
import ui.theme.AppTheme

@Composable
fun App(
    analyticsHelper: AnalyticsHelper = NoOpAnalyticsHelper(),
    setup: @Composable () -> Unit = {},
) {
    // Waiting koinInject for Multiplatform to be released
    // https://insert-koin.io/docs/reference/koin-compose/multiplatform#koin-features-for-your-composable-wip
    val appScreenModel = remember { AppScreenModel() }
    var uiState: AppScreenUiState by mutableStateOf(Loading)

    // Load UI state
    LaunchedEffect(Unit) {
        appScreenModel.uiState.collect {
            uiState = it
        }
    }

    CompositionLocalProvider(LocalAnalyticsHelper provides analyticsHelper) {
        AppTheme {
            setup()

            Surface {
                val destination = startDestination(uiState)
                Crossfade (destination) { screen ->
                    when (screen) {
                        null -> Box(modifier = Modifier.fillMaxSize())
                        else -> Navigator(screen)
                    }
                }
            }
        }
    }
}

private fun startDestination(
    uiState: AppScreenUiState
): Screen? = when (uiState) {
    Loading -> null
    is Success -> when (uiState.isWelcomeShown) {
        true -> ChatScreen
        false -> WelcomeScreen
    }
}

