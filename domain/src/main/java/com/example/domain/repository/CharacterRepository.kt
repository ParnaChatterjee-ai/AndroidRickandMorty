package com.example.domain.repository


import com.example.domain.models.CharacterDetails
import com.example.domain.models.Characters
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(): List<Characters?>
    suspend fun getCharacterById(id:String) : CharacterDetails?
}
