package id.co.drakorid.tv.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.drakorid.tv.data.repository.MovieRepository
import id.co.drakorid.tv.model.CategoryEntity
import id.co.drakorid.tv.model.MovieEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val categories: List<CategoryEntity> = emptyList(),
    val selectedCategory: String? = null,
    val movies: List<MovieEntity> = emptyList(),
    val isLoadingMovies: Boolean = false
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CategoryUiState(isLoading = true)
            val result = movieRepository.getCategories()
            _uiState.value = CategoryUiState(
                isLoading = false,
                categories = result.getOrNull().orEmpty(),
                error = result.errorMessage()
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            isLoadingMovies = true
        )
        viewModelScope.launch {
            val result = movieRepository.getByCategory(category)
            _uiState.value = _uiState.value.copy(
                movies = result.getOrNull().orEmpty(),
                isLoadingMovies = false,
                error = result.errorMessage()
            )
        }
    }
}
