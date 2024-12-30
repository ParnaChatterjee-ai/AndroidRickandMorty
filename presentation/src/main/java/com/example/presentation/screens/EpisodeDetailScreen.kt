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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.common.ResultState
import com.example.domain.models.Characters
import com.example.domain.models.Episode
import com.example.presentation.R
import com.example.presentation.themes.color
import com.example.presentation.themes.dimens
import com.example.presentation.viewmodels.EpisodeDetailsViewModel

@Composable
fun EpisodeDetailScreen(
    viewModel: EpisodeDetailsViewModel,
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit
) {
    val episodeDetailsViewModel = remember { viewModel }
    val episodeState = episodeDetailsViewModel.episodeState.collectAsStateWithLifecycle()
    EpisodeDetails(episodeState, modifier, onBackButton)
}

@Composable
private fun EpisodeDetails(
    episodeState: State<ResultState<Episode?>>,
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit
) {
    Column(
        modifier.fillMaxWidth(1f),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.color.white)
        )
        {
            when (val state = episodeState.value) {
                is ResultState.Error -> state.exception.localizedMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ResultState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is ResultState.Success -> {
                    ShowEpisodeDetails(state.data, onBackButton)
                }
            }
        }
    }

}

@Composable
fun ShowEpisodeDetails(
    data: Episode?, onBackButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    //for topbar
    Column(
        modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.app_bar_height)
                .align(Alignment.CenterHorizontally)
                .background(MaterialTheme.color.topbar_box)
                .padding(top = MaterialTheme.dimens.largePadding)
        )
        {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = "Back Button",
                modifier = Modifier
                    .padding(all = MaterialTheme.dimens.mediumPadding)
                    .align(Alignment.CenterStart)
                    .clickable { onBackButton.invoke() }
            )
            Text(
                text = data?.name
                    ?: stringResource(R.string.episode_screen),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.color.black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        SetTitleContent(R.string.episodename, data?.episode)
        SetTitleContent(R.string.AIR_DATE, data?.airdate)
        data?.characters?.let { SetEpisodeCharacters(it) }
    }

}

@Composable
private fun SetTitleContent(
    title: Int, content: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.padding(
            top = MaterialTheme.dimens.mediumPadding,
            start = MaterialTheme.dimens.mediumPadding
        )
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W400,
            color = MaterialTheme.color.black,
            modifier = Modifier.padding(bottom = MaterialTheme.dimens.smallPadding)
        )
        Text(
            text = content ?: stringResource(R.string.unknown_content),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.W400,
            color = MaterialTheme.color.purple_700,

            )
    }
}

@Composable
private fun SetEpisodeCharacters(
    characterList: List<Characters>,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth(1f)) {
        Text(
            text = stringResource(R.string.characterS),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.W400,
            color = MaterialTheme.color.black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(
                    start = MaterialTheme.dimens.mediumPadding,
                    top = MaterialTheme.dimens.smallPadding,
                    bottom = MaterialTheme.dimens.smallPadding
                )
        )
        //Characters of Episode grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(all = MaterialTheme.dimens.smallPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding)
        ) {
            items(
                characterList.size,
                key = { characterList[it].id }
            ) { index ->
                SetCharacterItem(character = characterList[index])
            }
        }
    }
}

@Composable
private fun SetCharacterItem(
    character: Characters,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.smallPadding)
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
        }
    }
}

