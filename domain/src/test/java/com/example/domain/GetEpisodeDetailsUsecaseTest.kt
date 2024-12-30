package com.example.domain

import com.example.domain.models.Characters
import com.example.domain.models.Episode
import com.example.domain.repository.EpisodeRepository
import com.example.domain.usecases.GetEpisodeDetailsUsecase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetEpisodeDetailsUsecaseTest {
    private val repository = mockk<EpisodeRepository>(relaxed = true)
    private lateinit var getEpisodeDetailsUsecase: GetEpisodeDetailsUsecase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        getEpisodeDetailsUsecase = GetEpisodeDetailsUsecase(repository)
    }

    @Test
    fun `when returns data from EpisodeRepository then receive episode details on success`() =
        runTest(testDispatcher) {
            //Given
            val expectedResult = Episode(
                id = "3",
                name = "Anatomy Park",
                airdate = "December 16, 2013",
                episode = "S01E03",
                listOf(
                    Characters(
                        "1", "Rick Sanchez",
                        "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    ),
                    Characters(
                        "2", "Morty Smith",
                        "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    )
                )
            )
            // Mocking the suspending function
            coEvery { repository.getEpisodeById("3") } returns expectedResult
            //When
            val testcaseResult = getEpisodeDetailsUsecase.invoke("3")
            //Then
            Assert.assertEquals(expectedResult, testcaseResult)
        }

    @Test
    fun `when EpisodeRepository returns no data then returns empty episode details`() =
        runTest(testDispatcher) {
            //Given
            val expectedEpisodeDetails = null
            coEvery { repository.getEpisodeById("3") } returns expectedEpisodeDetails

            //When
            val testcaseEpisodeDetails = getEpisodeDetailsUsecase.invoke("3")
            //Then
            Assert.assertEquals(expectedEpisodeDetails, testcaseEpisodeDetails)
        }


    @Test
    fun `when EpisodeRepository received exception then GetEpisodeDetailsUsecase caught the exception`() =
        runTest(testDispatcher) {
            //Given
            val exception = RuntimeException("Error while fetching episode details")
            coEvery { repository.getEpisodeById("1") } throws exception
            //When
            try {
                getEpisodeDetailsUsecase.invoke("1")
            }
            //Then
            catch (e: RuntimeException) {
                assertEquals("java.lang.RuntimeException: Error while fetching episode details", "" + e)
            }
        }
}
