package id.co.drakorid.tv.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.co.drakorid.tv.ui.components.TvLoadingIndicator
import androidx.tv.material3.Text
import id.co.drakorid.tv.model.MovieEntity
import id.co.drakorid.tv.ui.components.TvMovieCard
import id.co.drakorid.tv.ui.components.TvSectionHeader
import id.co.drakorid.tv.ui.theme.TvColors

@Composable
fun HomeScreen(
    onMovieClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val topBarFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        topBarFocus.requestFocus()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF020617))) {
        if (uiState.isLoading && uiState.latest.isEmpty()) {
            TvLoadingIndicator(modifier = Modifier.align(Alignment.Center))
            return@Box
        }

        if (uiState.error != null && uiState.latest.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gagal memuat konten",
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
            return@Box
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            item {
                TopNavigationBar(
                    topBarFocus = topBarFocus,
                    onSearchClick = onSearchClick,
                    onCategoryClick = onCategoryClick
                )
            }

            if (uiState.slider.isNotEmpty()) {
                item {
                    FeaturedSlider(movies = uiState.slider, onMovieClick = onMovieClick)
                }
            }

            MovieRowItem(uiState, onMovieClick)
        }
    }
}

@Composable
private fun TopNavigationBar(
    topBarFocus: FocusRequester,
    onSearchClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "DRAKORID TV",
            color = TvColors.primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TopBarButton("Cari", onSearchClick, topBarFocus, isFirst = true)
            TopBarButton("Kategori", onCategoryClick, topBarFocus, isFirst = false)
        }
    }
}

@Composable
private fun TopBarButton(
    text: String,
    onClick: () -> Unit,
    sharedFocus: FocusRequester,
    isFirst: Boolean
) {
    var focused by remember { mutableStateOf(false) }
    val modifier = Modifier
        .then(if (isFirst) Modifier.focusRequester(sharedFocus) else Modifier)
        .onFocusChanged { focused = it.isFocused }
        .focusable()
        .background(
            color = if (focused) TvColors.focusBackground else TvColors.cardBackground,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp)
        )
        .padding(horizontal = 20.dp, vertical = 10.dp)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = if (focused) TvColors.focusBorder else TvColors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FeaturedSlider(
    movies: List<MovieEntity>,
    onMovieClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(movies.take(10)) { movie ->
            var focused by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .width(480.dp)
                    .height(270.dp)
                    .padding(end = 16.dp)
                    .onFocusChanged { focused = it.isFocused }
                    .focusable()
                    .background(TvColors.cardBackground)
                    .then(
                        if (focused) {
                            Modifier.border(
                                width = 3.dp,
                                color = TvColors.focusBorder,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
                            )
                        } else Modifier
                    ),
                contentAlignment = Alignment.BottomStart
            ) {
                coil.compose.AsyncImage(
                    model = movie.banner ?: movie.poster,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                )
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = movie.title ?: "",
                        color = TvColors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    val meta = listOfNotNull(
                        movie.year,
                        movie.rating?.let { "★ %.1f".format(it) }
                    ).joinToString(" · ")
                    if (meta.isNotEmpty()) {
                        Text(
                            text = meta,
                            color = TvColors.textSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieRow(
    title: String,
    movies: List<MovieEntity>,
    onMovieClick: (String) -> Unit
) {
    if (movies.isEmpty()) return

    Column(modifier = Modifier.padding(top = 8.dp)) {
        TvSectionHeader(title)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies) { movie ->
                var focused by remember { mutableStateOf(false) }
                val requester = remember { FocusRequester() }

                TvMovieCard(
                    title = movie.title ?: "Unknown",
                    posterUrl = movie.poster,
                    rating = movie.rating,
                    year = movie.year,
                    isFocused = focused,
                    focusRequester = requester,
                    onFocusChanged = { focused = it },
                    onClick = { movie.id?.let(onMovieClick) },
                    modifier = Modifier.width(130.dp)
                )
            }
        }
    }
}

private fun LazyListScope.MovieRowItem(
    uiState: HomeUiState,
    onMovieClick: (String) -> Unit
) {
    item { MovieRow("Terbaru", uiState.latest, onMovieClick) }
    item { MovieRow("Ongoing", uiState.ongoing, onMovieClick) }
    item { MovieRow("Trending", uiState.trending, onMovieClick) }
    item { MovieRow("Populer", uiState.popular, onMovieClick) }
    item { MovieRow("Rekomendasi", uiState.recommended, onMovieClick) }
}
