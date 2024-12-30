package com.example.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.di.IoDispatcher
import com.example.domain.usecases.GetCharacterDetailsUsecase
import com.example.domain.usecases.GetCharactersUsecase
import com.example.domain.usecases.GetEpisodeDetailsUsecase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ViewModelfactory @Inject constructor(
    private val getCharactersUseCase: GetCharactersUsecase,
    private val getCharacterDetailUseCase: GetCharacterDetailsUsecase,
    private val getEpisodeDetailUseCase: GetEpisodeDetailsUsecase,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CharacterViewModel::class.java) -> {
                CharacterViewModel(getCharactersUseCase,coroutineDispatcher) as T
            }
            modelClass.isAssignableFrom(CharacterDetailsViewModel::class.java)-> {
                CharacterDetailsViewModel(getCharacterDetailUseCase,coroutineDispatcher) as T
            }
            modelClass.isAssignableFrom(EpisodeDetailsViewModel::class.java) -> {
                EpisodeDetailsViewModel(getEpisodeDetailUseCase,coroutineDispatcher) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

