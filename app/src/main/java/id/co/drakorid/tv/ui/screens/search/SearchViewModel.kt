package id.co.drakorid.tv.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.drakorid.tv.data.repository.MovieRepository
import id.co.drakorid.tv.model.MovieEntity
import id.co.drakorid.tv.util.AppResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: List<MovieEntity> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(isSearching = false, results = emptyList())
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)
            _uiState.value = _uiState.value.copy(isSearching = true, error = null)

            val result = movieRepository.search(query)
            _uiState.value = _uiState.value.copy(
                isSearching = false,
                results = result.getOrNull().orEmpty(),
                error = result.errorMessage()
            )
        }
    }

    fun clear() {
        searchJob?.cancel()
        _uiState.value = SearchUiState()
    }
}
