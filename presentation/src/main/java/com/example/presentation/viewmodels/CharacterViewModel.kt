package com.example.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.exception.ApolloException
import com.example.common.ResultState
import com.example.data.di.IoDispatcher
import com.example.domain.models.Characters
import com.example.domain.usecases.GetCharactersUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class CharacterViewModel @Inject constructor(private val getCharactersUseCase : GetCharactersUsecase,
                                             @IoDispatcher private val iodispatcher : CoroutineDispatcher
) :ViewModel(){
    private val _charactersState =
        MutableStateFlow<ResultState<List<Characters?>>>(ResultState.Loading)
    val charactersState: StateFlow<ResultState<List<Characters?>>> = _charactersState

    init{
        getAllCharacters()
    }


     fun getAllCharacters() {
       _charactersState.value = ResultState.Loading
        viewModelScope.launch(iodispatcher) {
            try {
                val result = getCharactersUseCase.invoke()
                if(result!= null && result.isNotEmpty())
                  _charactersState.emit(ResultState.Success(result))// Emit the updated list
            } catch (ex: ApolloException) {
                _charactersState.emit(ResultState.Error(exception = ex))
            }catch (ex: IOException){
                _charactersState.emit(ResultState.Error(exception = ex))
            }catch (ex: TimeoutException){
                _charactersState.emit(ResultState.Error(exception = ex))
            }
        }
    }

}



