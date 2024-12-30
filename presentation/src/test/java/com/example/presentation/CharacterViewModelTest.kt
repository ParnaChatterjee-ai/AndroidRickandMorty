package com.example.presentation

import app.cash.turbine.test
import com.example.common.ResultState
import com.example.domain.models.Characters
import com.example.domain.repository.CharacterRepository
import com.example.domain.usecases.GetCharactersUsecase
import com.example.presentation.viewmodels.CharacterViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterViewModelTest {
    private lateinit var characterViewmodel: CharacterViewModel
    private  lateinit var charactersUseCase: GetCharactersUsecase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        charactersUseCase = mockk<GetCharactersUsecase>(relaxed = true)
        characterViewmodel = CharacterViewModel(charactersUseCase,testDispatcher)
    }
    @Test
    fun `should return charactersState is in loading state when loading characters`() =
        runTest(testDispatcher){
           val currentState =  characterViewmodel.charactersState.value
            assertEquals(ResultState.Loading,currentState)
    }

    @Test
    fun `get list of characters on success`() = runTest(testDispatcher) {

        val characters = listOf(
            Characters("a","RickSanchez","https://rickandmortyapi.com/avatar/1.jpeg"),
            Characters("b","MortySmith","https://rickandmortyapi.com/avatar/2.jpeg"),
            Characters("c","SummerSmith","https://rickandmortyapi.com/avatar/3.jpeg"))

        coEvery { charactersUseCase.invoke() } returns characters
        characterViewmodel.getAllCharacters()
        val currentState =  characterViewmodel.charactersState.value
        //For checking initial state loading state or not
        assertEquals(ResultState.Loading,currentState)

        //Collecting data from flow and compare
        characterViewmodel.charactersState.test {

            assertTrue(awaitItem() is ResultState)
            val successState = awaitItem()
            assertThat(successState).isInstanceOf(ResultState.Success::class.java)
            val actualCharacters = flowOf((successState as ResultState.Success).data).toList()
            assertThat(actualCharacters.get(0).size).isEqualTo(characters.size)

            cancelAndConsumeRemainingEvents()
        }
    }
    @Test
    fun `while fetching characters list test for runtime exception to show error`() = runTest(testDispatcher) {
        val exception = RuntimeException("Error fetching character results")
        val repository = mockk<CharacterRepository>(relaxed = true)
        coEvery { repository.getCharacters() } throws exception
        try {
            characterViewmodel.getAllCharacters()
        }
        catch (e :RuntimeException){
            assertEquals("Error fetching character results", ""+e)
        }
    }
}
