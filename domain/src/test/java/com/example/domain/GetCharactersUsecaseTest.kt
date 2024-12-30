package com.example.domain

import com.example.domain.models.Characters
import com.example.domain.repository.CharacterRepository
import com.example.domain.usecases.GetCharactersUsecase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCharactersUsecaseTest {
    private lateinit var getCharacterusecaseTest : GetCharactersUsecase
    private val repository = mockk<CharacterRepository>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    @Before
    fun setup(){
        getCharacterusecaseTest = GetCharactersUsecase(repository)
    }

    @Test
    fun `should return list of Characters when repository returns data`()= runTest(testDispatcher) {
        val expectedResult = listOf(Characters("1","aaa","1.jpg"),
            Characters("2","bbb","2.jpg"),
            Characters("3","ccc","3.jpg"))
      coEvery { repository.getCharacters() } returns expectedResult
        val testCaseCharacters = getCharacterusecaseTest.invoke()
        assertEquals(expectedResult,testCaseCharacters)
    }

    @Test
    fun ` for runtime exception to show error`() = runTest(testDispatcher) {
        val exception = RuntimeException("Error fetching character results")
        coEvery { repository.getCharacters() } throws exception
        try {
            val result = getCharacterusecaseTest.invoke()
        }
        catch( e :RuntimeException)  {
            assertEquals("java.lang.RuntimeException: Error fetching character results", ""+e)
        }
    }

    @Test
    fun `should return empty list when repository returns no data`() = runTest {
        val emptyList = emptyList<Characters>()
        // Mocking the suspending function using coEvery
        coEvery { repository.getCharacters() } returns emptyList
        val result = getCharacterusecaseTest.invoke()
        assertEquals(emptyList, result)
    }
}


