package id.co.drakorid.tv.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.co.drakorid.tv.ui.components.TvLoadingIndicator
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import id.co.drakorid.tv.model.EpisodeEntity
import id.co.drakorid.tv.ui.theme.TvColors

@Composable
fun MovieDetailScreen(
    onPlay: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movie = uiState.movie

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF020617))) {
        if (uiState.isLoading && movie == null) {
            TvLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        if (movie == null) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Detail tidak ditemukan", color = TvColors.textPrimary)
                FocusableButton("← Kembali", onBack)
            }
            return@Box
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Header banner with gradient
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    AsyncImage(
                        model = movie.banner ?: movie.poster,
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF020617).copy(alpha = 0.7f),
                                        Color(0xFF020617)
                                    )
                                )
                            )
                    )

                    // Back button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(20.dp)
                    ) {
                        FocusableButton("← Kembali", onBack)
                    }
                }
            }

            // Title + meta
            item {
                Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)) {
                    Text(
                        text = movie.title ?: "",
                        color = TvColors.textPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    val meta = listOfNotNull(
                        movie.year,
                        movie.duration,
                        movie.country,
                        movie.rating?.let { "★ %.1f".format(it) }
                    ).joinToString(" · ")
                    if (meta.isNotEmpty()) {
                        Text(
                            text = meta,
                            color = TvColors.textSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    if (movie.genre != null) {
                        Text(
                            text = movie.genre,
                            color = TvColors.primary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Description
            if (!movie.description.isNullOrBlank()) {
                item {
                    Text(
                        text = movie.description,
                        color = TvColors.textSecondary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        maxLines = 6,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }
            }

            // Episodes
            if (uiState.episodes.isNotEmpty()) {
                item {
                    Text(
                        text = "Episode (${uiState.episodes.size})",
                        color = TvColors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 32.dp, top = 24.dp, bottom = 12.dp)
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.episodes) { episode ->
                            EpisodeCard(episode, onClick = { episode.id?.let(onPlay) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeCard(
    episode: EpisodeEntity,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(200.dp)
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .background(
                color = if (focused) TvColors.focusBackground else TvColors.cardBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (focused) {
                    Modifier.border(3.dp, TvColors.focusBorder, RoundedCornerShape(8.dp))
                } else {
                    Modifier.border(1.dp, TvColors.cardBorder, RoundedCornerShape(8.dp))
                }
            )
            .padding(12.dp)
    ) {
        // Episode thumbnail
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(TvColors.gradientStart, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = episode.thumbnail,
                contentDescription = episode.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "E${episode.episodeNumber ?: ""}",
                color = TvColors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = episode.title ?: "Episode ${episode.episodeNumber ?: ""}",
            color = TvColors.textPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (episode.duration != null) {
            Text(
                text = episode.duration,
                color = TvColors.textSecondary,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun FocusableButton(
    text: String,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .background(
                color = if (focused) TvColors.focusBackground else TvColors.cardBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (focused) TvColors.focusBorder else TvColors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
