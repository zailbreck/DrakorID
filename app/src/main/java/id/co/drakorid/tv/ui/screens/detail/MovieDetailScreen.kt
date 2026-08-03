package id.co.drakorid.tv.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import id.co.drakorid.tv.model.EpisodeEntity
import id.co.drakorid.tv.ui.components.PhoneLoadingIndicator
import id.co.drakorid.tv.ui.theme.TvColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    onPlay: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movie = uiState.movie

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie?.title ?: "Detail", color = TvColors.textPrimary) },
                navigationIcon = { TextButton(onClick = onBack) { Text("←", color = TvColors.primary, fontSize = 20.sp) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF020617))
            )
        },
        containerColor = Color(0xFF020617)
    ) { padding ->
        if (uiState.isLoading && movie == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                PhoneLoadingIndicator()
            }
            return@Scaffold
        }

        if (movie == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tidak ditemukan", color = TvColors.textPrimary)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Banner
            Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                AsyncImage(
                    model = movie.banner ?: movie.poster,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(colors = listOf(Color.Transparent, Color(0xFF020617)))
                ))
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(movie.title ?: "", color = TvColors.textPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                val meta = listOfNotNull(movie.year, movie.duration, movie.country, movie.rating?.let { "★ %.1f".format(it) }).joinToString(" · ")
                if (meta.isNotEmpty()) {
                    Text(meta, color = TvColors.textSecondary, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
                }
                if (movie.genre != null) {
                    Text(movie.genre, color = TvColors.primary, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
                if (!movie.description.isNullOrBlank()) {
                    Text(movie.description, color = TvColors.textSecondary, fontSize = 14.sp, lineHeight = 22.sp, modifier = Modifier.padding(top = 12.dp))
                }
            }

            // Episodes
            if (uiState.episodes.isNotEmpty()) {
                Text("Episode", color = TvColors.textPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp, top = 8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.episodes) { episode ->
                        EpisodeCard(episode) { episode.id?.let(onPlay) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun EpisodeCard(episode: EpisodeEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(160.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = TvColors.cardBackground),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "E${episode.episodeNumber ?: ""}",
                color = TvColors.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = episode.title ?: "",
                color = TvColors.textPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 6.dp)
            )
            if (episode.duration != null) {
                Text(episode.duration, color = TvColors.textSecondary, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
