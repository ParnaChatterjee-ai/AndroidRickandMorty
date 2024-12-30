package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.presentation.R
import com.example.presentation.screens.CharacterDetailScreen
import com.example.presentation.screens.CharacterListScreen
import com.example.presentation.screens.EpisodeDetailScreen
import com.example.presentation.screens.ShowNoRecord
import com.example.presentation.viewmodels.CharacterDetailsViewModel
import com.example.presentation.viewmodels.CharacterViewModel
import com.example.presentation.viewmodels.EpisodeDetailsViewModel

@Composable
fun RMNavGraph(viewModelprovider: ViewModelProvider) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = RMScreenRoutes.HomeScreen) {

        //For Character List Screen

        composable<RMScreenRoutes.HomeScreen> {
            val characterViewModel: CharacterViewModel =
                viewModelprovider.get(CharacterViewModel::class.java)
            CharacterListScreen(characterViewModel,
                onNavigateTo = { characterId: String ->
                    navController.navigate(RMScreenRoutes.CharacterDetail(characterId))
                })
        }
        //For CharacterDetails Screen

        composable<RMScreenRoutes.CharacterDetail> { navBackStackEntry ->
            val characterDetails: RMScreenRoutes.CharacterDetail =
                navBackStackEntry.toRoute<RMScreenRoutes.CharacterDetail>()
            val characterDetailViewModel: CharacterDetailsViewModel =
                viewModelprovider[CharacterDetailsViewModel::class.java]

            var id by rememberSaveable { mutableStateOf("") }
            if (!id.contentEquals(characterDetails.characterId)) {
                characterDetailViewModel.getCharacterDetails(characterDetails.characterId)
                id = characterDetails.characterId
            }
            if (characterDetails.characterId != null) {
                CharacterDetailScreen(
                    characterDetailViewModel,
                    onBackButton = navController::navigateUp,
                    onNavigateTo = { episodeId: String ->
                        navController.navigate(RMScreenRoutes.EpisodeDetail(episodeId))
                    }
                )
            } else {
                ShowNoRecord(stringResource(R.string.no_character_details))
            }
        }

        //For Episode Details Screen

        composable<RMScreenRoutes.EpisodeDetail> { navBackStackEntry ->
            val episodeDetails: RMScreenRoutes.EpisodeDetail =
                navBackStackEntry.toRoute<RMScreenRoutes.EpisodeDetail>()
            val episodeDetailViewModel: EpisodeDetailsViewModel =
                viewModelprovider[EpisodeDetailsViewModel::class.java]

            var id by rememberSaveable { mutableStateOf("") }
            episodeDetailViewModel.getEpisodeDetails(episodeDetails.episodeId)

             if (!id.contentEquals(episodeDetails.episodeId)) {
             episodeDetailViewModel.getEpisodeDetails(episodeDetails.episodeId)
             id = episodeDetails.episodeId
         }
            EpisodeDetailScreen(
                episodeDetailViewModel,
                onBackButton = navController::navigateUp
            )
        }
    }
}




