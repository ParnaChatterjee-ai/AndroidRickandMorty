package com.example.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.common.ResultState
import com.example.domain.models.Characters
import com.example.presentation.R
import com.example.presentation.themes.color
import com.example.presentation.themes.dimens
import com.example.presentation.viewmodels.CharacterViewModel


@Composable
fun CharacterListScreen(
    viewModel: CharacterViewModel,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val characterState by viewModel.charactersState.collectAsStateWithLifecycle()
    remember { characterState }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.smallPadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.app_bar_height)
                .background(MaterialTheme.color.topbar_box)
        )
        {
            Text(
                text = stringResource(R.string.rick_morty_app),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.color.white)
        )
        {
            when (val state = characterState) {
                is ResultState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is ResultState.Success -> {
                    SetCharactersList(state.data.requireNoNulls(), onNavigateTo)
                }

                is ResultState.Error -> {
                    state.exception.localizedMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SetCharactersList(
    characterList: List<Characters>,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(all = MaterialTheme.dimens.smallPadding),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding)
    ) {
        items(characterList.size, key = { characterList[it].id }) { index ->
            SetCharacterItem(character = characterList[index], onNavigateTo)
        }
    }
}

@Composable
private fun SetCharacterItem(
    character: Characters,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.smallPadding)
            .clickable {
                onNavigateTo(character.id)
            }
    )
    {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
            )
            Text(
                text = character.name,
                modifier = Modifier
                    .padding(MaterialTheme.dimens.smallPadding)
                    .align(Alignment.BottomCenter),
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        }
    }

}


