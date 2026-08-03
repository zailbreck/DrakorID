package id.co.drakorid.tv.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.drakorid.tv.data.repository.MovieRepository
import id.co.drakorid.tv.model.MovieEntity
import id.co.drakorid.tv.util.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val slider: List<MovieEntity> = emptyList(),
    val latest: List<MovieEntity> = emptyList(),
    val ongoing: List<MovieEntity> = emptyList(),
    val trending: List<MovieEntity> = emptyList(),
    val popular: List<MovieEntity> = emptyList(),
    val recommended: List<MovieEntity> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)

            val slider = movieRepository.getSlider()
            val latest = movieRepository.getLatest()
            val ongoing = movieRepository.getOngoing()
            val trending = movieRepository.getTrending()
            val popular = movieRepository.getPopular()
            val recommended = movieRepository.getRecommended()

            val errors = listOfNotNull(
                slider.errorMessage(),
                latest.errorMessage(),
                ongoing.errorMessage(),
                trending.errorMessage(),
                popular.errorMessage(),
                recommended.errorMessage()
            )

            _uiState.value = HomeUiState(
                isLoading = false,
                error = if (errors.size >= 6) "Gagal memuat konten — periksa koneksi" else null,
                slider = slider.getOrNull().orEmpty(),
                latest = latest.getOrNull().orEmpty(),
                ongoing = ongoing.getOrNull().orEmpty(),
                trending = trending.getOrNull().orEmpty(),
                popular = popular.getOrNull().orEmpty(),
                recommended = recommended.getOrNull().orEmpty()
            )
        }
    }
}
