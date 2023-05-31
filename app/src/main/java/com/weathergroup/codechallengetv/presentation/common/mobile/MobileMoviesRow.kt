package com.weathergroup.codechallengetv.presentation.common.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.presentation.common.tv.ItemDirection
import com.weathergroup.codechallengetv.presentation.screens.dashboard.rememberChildPadding
import com.weathergroup.codechallengetv.presentation.theme.CodeChallengeTVCardShape
import com.weathergroup.codechallengetv.presentation.utils.FocusGroup

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MobileMoviesRow(
    modifier: Modifier = Modifier,
    itemWidth: Dp,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit = {}
) {
    Column(
        modifier = modifier.focusGroup()
    ) {
        title?.let { nnTitle ->
            Text(
                text = nnTitle,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(start = startPadding)
                    .padding(vertical = 16.dp)
            )
        }

        AnimatedContent(
            targetState = movies,
            label = ""
        ) { movieState ->
            FocusGroup {
                LazyRow {
                    item { Spacer(modifier = Modifier.padding(start = startPadding)) }

                    movieState.forEachIndexed { _, movie ->
                        item {
                            key(movie.id) {
                                MoviesRowItem(
                                    modifier = Modifier
                                        .restorableFocus()
                                        .horizontalScroll(
                                            rememberScrollState()
                                        ),
                                    itemWidth = itemWidth,
                                    itemDirection = itemDirection,
                                    onMovieClick = onMovieClick,
                                    movie = movie
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.padding(end = 8.dp)) }
                    }

                    item { Spacer(modifier = Modifier.padding(start = endPadding)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesRowItem(
    modifier: Modifier = Modifier,
    itemWidth: Dp,
    itemDirection: ItemDirection,
    onMovieClick: (movie: Movie) -> Unit,
    movie: Movie
) {
    StandardCardLayout(
        modifier = Modifier
            .width(itemWidth)
            .then(modifier),
        title = {
            MoviesRowItemText(
                movie = movie
            )
        },
        imageCard = {
            Card(
                onClick = { onMovieClick(movie) },
                shape = CodeChallengeTVCardShape
            ) {
                MoviesRowItemImage(
                    modifier = Modifier.aspectRatio(itemDirection.aspectRatio),
                    movie = movie
                )
            }
        }
    )
}

@Composable
private fun MoviesRowItemImage(
    modifier: Modifier = Modifier,
    movie: Movie
) {
    Box(contentAlignment = Alignment.CenterStart) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                    }
                },
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(movie.posterUri)
                .build(),
            contentDescription = "movie poster of ${movie.name}",
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
private fun MoviesRowItemText(
    movie: Movie
) {
    val movieNameAlpha by animateFloatAsState(
        targetValue = 1f,
        label = ""
    )
    Text(
        text = movie.name,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.SemiBold
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .alpha(movieNameAlpha)
            .fillMaxWidth()
            .padding(top = 4.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
