package com.example.domain.usecases


import com.example.domain.models.CharacterDetails
import com.example.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterDetailsUsecase @Inject constructor(private val repository: CharacterRepository){
    suspend  fun invoke(id:String): CharacterDetails? {
        return repository.getCharacterById(id)
    }
}

