package com.example.domain.usecases


import com.example.domain.models.Characters
import com.example.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersUsecase @Inject constructor(private val repository: CharacterRepository){
    suspend  fun invoke(): List<Characters?> {
        return repository.getCharacters()
    }
}
