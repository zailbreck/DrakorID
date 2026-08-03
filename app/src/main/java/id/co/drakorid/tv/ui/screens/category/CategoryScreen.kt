package id.co.drakorid.tv.ui.screens.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.co.drakorid.tv.model.CategoryEntity
import id.co.drakorid.tv.ui.components.PhoneLoadingIndicator
import id.co.drakorid.tv.ui.components.PhoneMovieCard
import id.co.drakorid.tv.ui.theme.TvColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onMovieClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kategori", color = TvColors.textPrimary) },
                navigationIcon = { TextButton(onClick = onBack) { Text("←", color = TvColors.primary, fontSize = 20.sp) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF020617))
            )
        },
        containerColor = Color(0xFF020617)
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Category chips
            if (uiState.categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.categories.take(5).forEach { category ->
                        val isSelected = uiState.selectedCategory == category.name
                        FilterChip(
                            selected = isSelected,
                            onClick = { category.name?.let(viewModel::selectCategory) },
                            label = { Text(category.name ?: "", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TvColors.focusBackground,
                                selectedLabelColor = TvColors.primary
                            )
                        )
                    }
                }
            }

            // Movies
            when {
                uiState.isLoadingMovies -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        PhoneLoadingIndicator()
                    }
                }
                uiState.movies.isNotEmpty() -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 120.dp),
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(uiState.movies) { movie ->
                            PhoneMovieCard(
                                title = movie.title ?: "Unknown",
                                posterUrl = movie.poster,
                                rating = movie.rating,
                                year = movie.year,
                                onClick = { movie.id?.let(onMovieClick) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                uiState.selectedCategory != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tidak ada film", color = TvColors.textMuted)
                    }
                }
            }
        }
    }
}
