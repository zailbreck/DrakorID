package id.co.drakorid.tv.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.drakorid.tv.data.repository.MovieRepository
import id.co.drakorid.tv.model.EpisodeEntity
import id.co.drakorid.tv.model.MovieEntity
import id.co.drakorid.tv.util.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val movie: MovieEntity? = null,
    val episodes: List<EpisodeEntity> = emptyList()
)

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: String = savedStateHandle["movieId"] ?: ""

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)

            val movieResult = movieRepository.getMovieInfo(movieId)
            val episodesResult = movieRepository.getEpisodes(movieId)

            _uiState.value = DetailUiState(
                isLoading = false,
                movie = movieResult.getOrNull(),
                episodes = episodesResult.getOrNull().orEmpty(),
                error = movieResult.errorMessage() ?: episodesResult.errorMessage()
            )
        }
    }

    fun toggleFavorite() {
        val movie = _uiState.value.movie ?: return
        viewModelScope.launch {
            val isFav = movie.isFavorite ?: false
            val result = if (isFav) {
                movieRepository.removeFavorite(movieId)
            } else {
                movieRepository.addFavorite(movieId)
            }
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    movie = movie.copy(isFavorite = !isFav)
                )
            }
        }
    }
}
