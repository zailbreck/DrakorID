package id.co.drakorid.tv.ui.screens.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.Text
import id.co.drakorid.tv.ui.components.TvLoadingIndicator
import id.co.drakorid.tv.ui.theme.TvColors

@Composable
fun PlayerScreen(
    onBack: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current

    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    // Handle back press (D-pad back or remote back)
    BackHandler(onBack = onBack)

    // Create player when URL is ready
    LaunchedEffect(uiState.videoUrl) {
        val url = uiState.videoUrl ?: return@LaunchedEffect
        val exoPlayer = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
        player = exoPlayer
    }

    // Release on dispose
    DisposableEffect(Unit) {
        onDispose {
            player?.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            uiState.isLoading -> {
                TvLoadingIndicator(modifier = Modifier.align(Alignment.Center))
                Text(
                    text = "Memuat video...",
                    color = TvColors.textSecondary,
                    modifier = Modifier.align(Alignment.Center).padding(top = 60.dp)
                )
            }

            uiState.error != null -> {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gagal memutar video",
                        color = TvColors.error,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiState.error ?: "",
                        color = TvColors.textSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            player != null -> {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            this.player = player
                            useController = true
                            setShowNextButton(false)
                            setShowPreviousButton(false)
                            setShowFastForwardButton(true)
                            setShowRewindButton(true)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
