package id.co.drakorid.tv.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.co.drakorid.tv.ui.components.TvLoadingIndicator
import androidx.tv.material3.Text
import id.co.drakorid.tv.model.MovieEntity
import id.co.drakorid.tv.ui.components.TvMovieCard
import id.co.drakorid.tv.ui.theme.TvColors

@Composable
fun SearchScreen(
    onMovieClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val textFieldFocus = remember { FocusRequester() }
    var textFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        textFieldFocus.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            // Back button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                BackButton(onBack)
            }

            // Search input
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(
                        color = if (textFocused) TvColors.focusBackground else TvColors.cardBackground,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .then(
                        if (textFocused) {
                            Modifier.border(3.dp, TvColors.focusBorder, RoundedCornerShape(10.dp))
                        } else {
                            Modifier.border(1.dp, TvColors.cardBorder, RoundedCornerShape(10.dp))
                        }
                    )
                    .focusRequester(textFieldFocus)
                    .onFocusChanged { textFocused = it.isFocused }
                    .focusable()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = uiState.query,
                    onValueChange = viewModel::onQueryChange,
                    textStyle = TextStyle(
                        color = TvColors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(TvColors.focusBorder),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Status
            when {
                uiState.isSearching -> {
                    TvLoadingIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 48.dp)
                    )
                }

                uiState.query.isBlank() -> {
                    Text(
                        text = "Ketik judul drama atau film untuk mencari",
                        color = TvColors.textMuted,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 48.dp)
                    )
                }

                uiState.results.isEmpty() -> {
                    Text(
                        text = "Tidak ada hasil untuk \"${uiState.query}\"",
                        color = TvColors.textMuted,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 48.dp)
                    )
                }

                else -> {
                    SearchResults(uiState.results, onMovieClick)
                }
            }
        }
    }
}

@Composable
private fun SearchResults(
    movies: List<MovieEntity>,
    onMovieClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(top = 24.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
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
                modifier = Modifier.width(140.dp)
            )
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit) {
    var focused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .background(
                color = if (focused) TvColors.focusBackground else TvColors.cardBackground,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = "← Kembali",
            color = if (focused) TvColors.focusBorder else TvColors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
