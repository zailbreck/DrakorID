package id.co.drakorid.tv.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import id.co.drakorid.tv.model.MovieEntity
import id.co.drakorid.tv.ui.components.PhoneLoadingIndicator
import id.co.drakorid.tv.ui.components.PhoneMovieCard
import id.co.drakorid.tv.ui.components.PhoneSectionHeader
import id.co.drakorid.tv.ui.theme.TvColors

@Composable
fun HomeScreen(
    onMovieClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF020617))) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DRAKORID",
                color = TvColors.primary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(onClick = onSearchClick) {
                    Text("🔍", fontSize = 18.sp)
                }
                IconButton(onClick = onCategoryClick) {
                    Text("☰", fontSize = 18.sp)
                }
            }
        }

        when {
            uiState.isLoading && uiState.latest.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    PhoneLoadingIndicator()
                }
            }

            uiState.error != null && uiState.latest.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Gagal memuat", color = TvColors.error, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(uiState.error ?: "", color = TvColors.textSecondary, fontSize = 12.sp)
                    TextButton(onClick = { viewModel.loadHome() }) {
                        Text("Coba Lagi", color = TvColors.primary)
                    }
                }
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Featured slider
                    if (uiState.slider.isNotEmpty()) {
                        item {
                            FeaturedBanner(movies = uiState.slider, onMovieClick = onMovieClick)
                        }
                    }

                    // Movie rows
                    item { PhoneSectionHeader("Terbaru") }
                    item { MovieRow(uiState.latest, onMovieClick) }

                    item { PhoneSectionHeader("Ongoing") }
                    item { MovieRow(uiState.ongoing, onMovieClick) }

                    item { PhoneSectionHeader("Trending") }
                    item { MovieRow(uiState.trending, onMovieClick) }

                    item { PhoneSectionHeader("Populer") }
                    item { MovieRow(uiState.popular, onMovieClick) }

                    item { PhoneSectionHeader("Rekomendasi") }
                    item { MovieRow(uiState.recommended, onMovieClick) }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun FeaturedBanner(
    movies: List<MovieEntity>,
    onMovieClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(movies.take(5)) { movie ->
            Box(
                modifier = Modifier
                    .width(340.dp)
                    .height(200.dp)
                    .padding(end = 12.dp)
                    .clickable { movie.id?.let(onMovieClick) },
                contentAlignment = Alignment.BottomStart
            ) {
                AsyncImage(
                    model = movie.banner ?: movie.poster,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = movie.title ?: "",
                        color = TvColors.textPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    val meta = listOfNotNull(movie.year, movie.rating?.let { "★ %.1f".format(it) }).joinToString(" · ")
                    if (meta.isNotEmpty()) {
                        Text(text = meta, color = TvColors.textSecondary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieRow(
    movies: List<MovieEntity>,
    onMovieClick: (String) -> Unit
) {
    if (movies.isEmpty()) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text("Tidak ada data", color = TvColors.textMuted, fontSize = 12.sp)
        }
        return
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(movies) { movie ->
            PhoneMovieCard(
                title = movie.title ?: "Unknown",
                posterUrl = movie.poster,
                rating = movie.rating,
                year = movie.year,
                onClick = { movie.id?.let(onMovieClick) },
                modifier = Modifier.width(120.dp)
            )
        }
    }
}
