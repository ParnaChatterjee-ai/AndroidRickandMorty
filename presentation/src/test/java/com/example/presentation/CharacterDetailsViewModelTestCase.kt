package com.example.presentation

import app.cash.turbine.test
import com.example.common.ResultState
import com.example.domain.models.CharacterDetails
import com.example.domain.models.Episode
import com.example.domain.models.Origins
import com.example.domain.usecases.GetCharacterDetailsUsecase
import com.example.presentation.viewmodels.CharacterDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterDetailsViewModelTestCase {
    lateinit var characterDetailUsecase: GetCharacterDetailsUsecase
    lateinit var charcterDetailsViewModel: CharacterDetailsViewModel
    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        characterDetailUsecase = mockk<GetCharacterDetailsUsecase>(relaxed = true)
        charcterDetailsViewModel = CharacterDetailsViewModel(characterDetailUsecase, testDispatcher)
    }

    //For checking the initial state
    @Test
    fun `when initial state of CharacterDetailsViewModel then it is in loading state`() =
        runTest(testDispatcher)
        {
            //When
            val state = charcterDetailsViewModel.charactersState.value
            //Then
            assertEquals(ResultState.Loading, state)
        }

    @Test
    fun `when return the details of character it is in success state`() =
        runTest(testDispatcher) {
            //Given
            val characterDetails = CharacterDetails(
                id = "1",
                name = "Rick",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                status = "Alive",
                species = "Human",
                gender = "male",
                origin = Origins(name = "Earth", dimension = "Replacement Dimension"),
                locations = com.example.domain.models.Locations(
                    id = "1",
                    name = "Earth",
                    dimension = "Dimension"
                ),
                episodes = listOf(
                    Episode("6", "Rick Potion #9", "January 27, 2014", "Episode1"),
                    Episode("7", "Raising Gazorpazorp", "March 10, 2014", "Episode2")
                )
            )

            coEvery { characterDetailUsecase.invoke("1") } returns characterDetails
            //When
            charcterDetailsViewModel.getCharacterDetails("1")
            //Then :Checking initial state loading or not
            assertEquals(ResultState.Loading, charcterDetailsViewModel.charactersState.value)

            charcterDetailsViewModel.charactersState.test {
                assertTrue(awaitItem() is ResultState)
                val successState = awaitItem()
                val actualDetails = flowOf((successState as ResultState.Success).data).toList()
                //Comparing with actual data
                assertEquals(characterDetails, actualDetails.get(0))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `when invoke getCharacterDetails with wrong character id then emit failure`() =
        runTest(testDispatcher) {
            //Given
            val exception = RuntimeException("Error fetching character results")
            val repository =
                mockk<com.example.domain.repository.CharacterRepository>(relaxed = true)
            coEvery { repository.getCharacterById("-1") } throws exception

            //When
            try {
                charcterDetailsViewModel.getCharacterDetails("-1")
            }
            //Then
            catch (e: RuntimeException) {
                assertEquals("Error fetching character results", "" + e)
            }
        }
}
