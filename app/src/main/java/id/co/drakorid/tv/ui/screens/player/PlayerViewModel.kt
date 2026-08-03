package id.co.drakorid.tv.ui.screens.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.drakorid.tv.data.repository.MovieRepository
import id.co.drakorid.tv.util.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val videoUrl: String? = null,
    val title: String = ""
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val episodeId: String = savedStateHandle["episodeId"] ?: ""
    private val movieTitle: String = savedStateHandle["title"] ?: ""

    private val _uiState = MutableStateFlow(PlayerUiState(title = movieTitle))
    val uiState: StateFlow<PlayerUiState> = _uiState

    init {
        loadStream()
    }

    fun loadStream() {
        viewModelScope.launch {
            _uiState.value = PlayerUiState(isLoading = true, title = movieTitle)

            val result = movieRepository.getPlayUrl(episodeId)
            when (result) {
                is AppResult.Success -> {
                    _uiState.value = PlayerUiState(
                        isLoading = false,
                        videoUrl = result.data.url,
                        title = movieTitle
                    )
                }
                is AppResult.Error -> {
                    _uiState.value = PlayerUiState(
                        isLoading = false,
                        error = result.message,
                        title = movieTitle
                    )
                }
                is AppResult.Loading -> {}
            }
        }
    }
}
