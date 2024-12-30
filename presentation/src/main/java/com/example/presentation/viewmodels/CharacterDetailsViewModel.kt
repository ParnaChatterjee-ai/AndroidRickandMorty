package com.example.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.exception.ApolloException
import com.example.common.ResultState
import com.example.data.di.IoDispatcher
import com.example.domain.models.CharacterDetails
import com.example.domain.usecases.GetCharacterDetailsUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(private val getCharacterDetailsUseCase : GetCharacterDetailsUsecase,
                                                    @IoDispatcher private val iodispatcher : CoroutineDispatcher
) :ViewModel(){
    private val _charactersState =
        MutableStateFlow<ResultState<CharacterDetails?>>(ResultState.Loading)
    val charactersState: StateFlow<ResultState<CharacterDetails?>> = _charactersState

     fun getCharacterDetails(id:String) {
        _charactersState.value = ResultState.Loading
        viewModelScope.launch(iodispatcher) {
            try {
                val result = getCharacterDetailsUseCase.invoke(id)
                result?.let {
                    _charactersState.emit(ResultState.Success(result))// Emit the character details
                }
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



