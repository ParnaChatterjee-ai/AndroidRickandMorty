package com.example.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.exception.ApolloException
import com.example.common.ResultState
import com.example.data.di.IoDispatcher
import com.example.domain.models.Episode
import com.example.domain.usecases.GetEpisodeDetailsUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val getEpisodeDetailsUseCase: GetEpisodeDetailsUsecase,
    @IoDispatcher private val iodispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _episodeState =
        MutableStateFlow<ResultState<Episode>>(ResultState.Loading)
    val episodeState: StateFlow<ResultState<Episode?>> = _episodeState

    fun getEpisodeDetails(id: String) {
        _episodeState.value = ResultState.Loading
        viewModelScope.launch(iodispatcher) {
            try {
                val result = getEpisodeDetailsUseCase.invoke(id)
                result?.let {
                    _episodeState.emit(ResultState.Success(result))// Emit the episode details
                }
            } catch (ex: ApolloException) {
                _episodeState.emit(ResultState.Error(exception = ex))
            } catch (ex: IOException) {
                _episodeState.emit(ResultState.Error(exception = ex))
            } catch (ex: TimeoutException) {
                _episodeState.emit(ResultState.Error(exception = ex))
            }
        }
    }

}

