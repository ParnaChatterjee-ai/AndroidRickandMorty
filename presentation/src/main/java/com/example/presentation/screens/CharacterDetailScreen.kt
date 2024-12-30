package com.example.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.common.ResultState
import com.example.domain.models.CharacterDetails
import com.example.domain.models.Episode
import com.example.domain.models.Locations
import com.example.domain.models.Origins
import com.example.presentation.R
import com.example.presentation.themes.color
import com.example.presentation.themes.dimens
import com.example.presentation.viewmodels.CharacterDetailsViewModel

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterDetailsViewModel,
    onBackButton: () -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    val characterState = viewModel.charactersState.collectAsStateWithLifecycle()
    CharacterDetails(characterState,onBackButton, onNavigateTo,modifier)
}

@Composable
private fun CharacterDetails(
    characterState: State<ResultState<CharacterDetails?>>,
    onBackButton: () -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxWidth(1f),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.color.white)
        ) {
            when (val state = characterState.value) {
                is ResultState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is ResultState.Error -> {
                    state.exception.localizedMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is ResultState.Success -> {
                    ShowCharacterDetails(state.data, onBackButton,onNavigateTo)
                }
            }
        }
    }
}

@Composable
private fun ShowCharacterDetails(
    data: CharacterDetails?,
    onBackButton: () -> Unit,
    onNavigateTo:(String) -> Unit,
    modifier: Modifier = Modifier
) {//for topbar
    Column(
        modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.app_bar_height)
                .align(Alignment.CenterHorizontally)
                .background(MaterialTheme.color.topbar_box)
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
                text = data?.name ?: stringResource(R.string.character_detail_screen),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.color.black,
                modifier = Modifier.align(Alignment.Center)
            )
        }//for Character Image and details
        Column(Modifier.verticalScroll(rememberScrollState()))
        {
            if (data != null) {
                AsyncImage(
                    model = data.image,
                    contentDescription = data.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                )
            }
            data?.let { DisplayStatus(it) }
            data?.let { DisplaySpices(it) }
            data?.origin?.let { DisplayOrigin(it) }
            data?.locations?.let { DisplayLocation(it) }
            DisplayEpisodes(data?.episodes,onNavigateTo)
        }
    }
}

@Composable
private fun DisplayStatus(
    data: CharacterDetails,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Spacer(Modifier.padding(top = MaterialTheme.dimens.mediumPadding))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.color.white)
                .padding(MaterialTheme.dimens.mediumPadding)
        ) {
            if (data != null) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.color.black,
                    modifier = Modifier.padding(
                        bottom = MaterialTheme.dimens.smallPadding
                    )
                )
            }
        }
        //status
        SetTitleContent(R.string.status, data?.status)
    }

}

@Composable
private fun DisplaySpices(
    data: CharacterDetails,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        //Species
        SetTitleContent(R.string.species, data?.species)
        //Gender
        SetTitleContent(R.string.gender, data?.gender)
    }
}

@Composable
private fun DisplayOrigin(
    origin: Origins,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth())
    {
        Row(
            modifier = Modifier.padding(
                bottom = MaterialTheme.dimens.smallPadding,
                top = MaterialTheme.dimens.mediumPadding,
                start = MaterialTheme.dimens.mediumPadding
            )
        ) {
            Text(
                text = stringResource(R.string.origin),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.color.black
            )
        }
        SetTitleContent(R.string.origin_name, origin.name)
        SetTitleContent(R.string.origin_dimension, origin.dimension)
    }
}

@Composable
private fun DisplayLocation(
    data: Locations,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth())
    {
        Row(
            modifier = Modifier.padding(
                bottom = MaterialTheme.dimens.smallPadding,
                top = MaterialTheme.dimens.mediumPadding,
                start = MaterialTheme.dimens.mediumPadding
            )
        ) {
            Text(
                text = stringResource(R.string.location),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.color.black
            )
        }
        SetTitleContent(R.string.location_name, data.name)
        SetTitleContent(R.string.location_dimension, data?.dimension)
    }
}

@Composable
private fun DisplayEpisodes(
    episodes: List<Episode>?,
    onNavigateTo:(String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(
                bottom = MaterialTheme.dimens.smallPadding,
                top = MaterialTheme.dimens.mediumPadding,
                start = MaterialTheme.dimens.mediumPadding
            )
        ) {
            Text(
                text = stringResource(R.string.episodes),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.color.black
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(all = MaterialTheme.dimens.mediumPadding),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallPadding)

        )
        {
            episodes?.size?.let {
                items(
                    it
                ) { index ->
                    EpisodesItem(episodes[index],onNavigateTo)
                }
            }

        }
        Spacer(Modifier.padding(top = MaterialTheme.dimens.smallPadding))
    }
}

@Composable
private fun EpisodesItem(episode: Episode,onNavigateTo:(String) -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(MaterialTheme.dimens.elevation),
        modifier = modifier
            .width(MaterialTheme.dimens.cardWidth)
            .height(MaterialTheme.dimens.cardHeight)
            .clickable { onNavigateTo(episode.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(setGrediantColor())
                .padding(MaterialTheme.dimens.smallPadding)

        ) {
            Text(
                text = episode.name,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.color.white,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SetTitleContent(
    title: Int, content: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.padding(
            bottom = MaterialTheme.dimens.smallPadding,
            start = MaterialTheme.dimens.mediumPadding
        )
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.color.black
        )
        Text(
            text = content?.uppercase() ?: stringResource(R.string.unknown_content),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.color.purple_700,
            modifier = Modifier.padding(start = MaterialTheme.dimens.smallPadding)
        )
    }
}

@Composable
private fun setGrediantColor(): Brush {
    return Brush.linearGradient(
        listOf(
            MaterialTheme.color.episode_start,
            MaterialTheme.color.episode_end
        )
    )
}

