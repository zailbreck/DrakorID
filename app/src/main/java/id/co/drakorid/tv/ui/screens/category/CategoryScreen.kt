package id.co.drakorid.tv.ui.screens.category

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
import id.co.drakorid.tv.model.CategoryEntity
import id.co.drakorid.tv.ui.components.TvMovieCard
import id.co.drakorid.tv.ui.theme.TvColors

@Composable
fun CategoryScreen(
    onMovieClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kategori",
                        color = TvColors.textPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    CategoryBackButton(onBack)
                }
            }

            // Category chips
            item {
                LazyRow(
                    modifier = Modifier.padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = uiState.selectedCategory == category.name,
                            onClick = { category.name?.let(viewModel::selectCategory) }
                        )
                    }
                }
            }

            // Movies
            when {
                uiState.isLoadingMovies -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TvLoadingIndicator()
                        }
                    }
                }

                uiState.movies.isNotEmpty() -> {
                    item {
                        LazyRow(
                            modifier = Modifier.padding(top = 16.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.movies) { movie ->
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
                }

                uiState.selectedCategory != null -> {
                    item {
                        Text(
                            text = "Tidak ada film di kategori ini",
                            color = TvColors.textMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var focused by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .onFocusChanged { focused = it.isFocused }
            .focusable()
            .background(
                color = when {
                    isSelected -> TvColors.focusBackground
                    focused -> TvColors.focusBackground.copy(alpha = 0.6f)
                    else -> TvColors.cardBackground
                },
                shape = RoundedCornerShape(20.dp)
            )
            .then(
                if (focused || isSelected) {
                    Modifier.border(2.dp, TvColors.focusBorder, RoundedCornerShape(20.dp))
                } else {
                    Modifier.border(1.dp, TvColors.cardBorder, RoundedCornerShape(20.dp))
                }
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = category.name ?: "",
                color = if (focused || isSelected) TvColors.focusBorder else TvColors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (category.totalMovies != null) {
                Text(
                    text = "${category.totalMovies} film",
                    color = TvColors.textMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryBackButton(onBack: () -> Unit) {
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
