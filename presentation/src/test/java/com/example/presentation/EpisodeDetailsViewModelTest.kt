package com.example.presentation

import app.cash.turbine.test
import com.example.common.ResultState
import com.example.domain.models.Characters
import com.example.domain.models.Episode
import com.example.domain.usecases.GetEpisodeDetailsUsecase
import com.example.presentation.viewmodels.EpisodeDetailsViewModel
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
class EpisodeDetailsViewModelTest {
    private lateinit var episodeDetailsUsecase: GetEpisodeDetailsUsecase
    private lateinit var episodeDetailsViewModel: EpisodeDetailsViewModel
    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        episodeDetailsUsecase = mockk<GetEpisodeDetailsUsecase>(relaxed = true)
        episodeDetailsViewModel = EpisodeDetailsViewModel(
            episodeDetailsUsecase,
            testDispatcher
        )
    }

    //For checking the initial state
    @Test
    fun `when initial state of EpisodeDetailsViewModel then it is in loading state`() =
        runTest(testDispatcher)
        {
            //When
            val state = episodeDetailsViewModel.episodeState.value
            //Then
            assertEquals(ResultState.Loading, state)
        }

    @Test
    fun `when return the details of episodes it is in success state`() =
        runTest(testDispatcher) {
            //Given
            val episodeDetails = Episode(
                id = "2",
                name = "Lawnmower Dog",
                airdate = "December 9, 2013",
                episode = "S01E02",
                characters = listOf(
                    Characters(
                        id = "1",
                        name = "Rick Sanchez",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    ),
                    Characters(
                        id = "2",
                        name = "Morty Smith",
                        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
                    )
                )
            )

            coEvery { episodeDetailsUsecase.invoke("2") } returns episodeDetails
            //When
            episodeDetailsViewModel.getEpisodeDetails("2")
            //Then :Checking initial state loading or not
            assertEquals(ResultState.Loading, episodeDetailsViewModel.episodeState.value)

            episodeDetailsViewModel.episodeState.test {
                assertTrue(awaitItem() is ResultState)
                val successState = awaitItem()
                val actualDetails = flowOf((successState as ResultState.Success).data).toList()
                //Comparing with actual data
                assertEquals(episodeDetails, actualDetails.get(0))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `when invoke getEpisodeDetails with wrong character id then emit failure`() =
        runTest(testDispatcher) {
            //Given
            val exception = RuntimeException("Error while fetching episode details")
            val repository =
                mockk<com.example.domain.repository.EpisodeRepository>(relaxed = true)
            coEvery { repository.getEpisodeById("-1") } throws exception

            //When
            try {
                episodeDetailsViewModel.getEpisodeDetails("-1")
            }
            //Then
            catch (e: RuntimeException) {
                assertEquals("Error while fetching episode details", "" + e)
            }
        }
}
