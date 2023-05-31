package com.weathergroup.codechallengetv.presentation.common.tv

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.ExperimentalTvFoundationApi
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.material3.Border
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveListScope
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.presentation.screens.dashboard.rememberChildPadding
import com.weathergroup.codechallengetv.presentation.theme.CodeChallengeTVBorderWidth
import com.weathergroup.codechallengetv.presentation.theme.CodeChallengeTVCardShape
import com.weathergroup.codechallengetv.presentation.utils.FocusGroup

enum class ItemDirection(val aspectRatio: Float) {
    Vertical(10.5f / 16f),
    Horizontal(16f / 9f);
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalTvFoundationApi::class
)
@Composable
fun TVMoviesRow(
    modifier: Modifier = Modifier,
    itemWidth: Dp = LocalConfiguration.current.screenWidthDp.dp.times(0.165f),
    itemDirection: ItemDirection = ItemDirection.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    focusedItemIndex: (index: Int) -> Unit = {},
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
                TvLazyRow(
                    pivotOffsets = PivotOffsets(parentFraction = 0.07f)
                ) {
                    item { Spacer(modifier = Modifier.padding(start = startPadding)) }

                    movieState.forEachIndexed { index, movie ->
                        item {
                            key(movie.id) {
                                MoviesRowItem(
                                    modifier = Modifier.restorableFocus(),
                                    focusedItemIndex = focusedItemIndex,
                                    index = index,
                                    itemWidth = itemWidth,
                                    itemDirection = itemDirection,
                                    onMovieClick = onMovieClick,
                                    movie = movie,
                                    showItemTitle = showItemTitle,
                                    showIndexOverImage = showIndexOverImage
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.padding(end = 20.dp)) }
                    }

                    item { Spacer(modifier = Modifier.padding(start = endPadding)) }
                }
            }
        }
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalTvFoundationApi::class
)
@Composable
fun ImmersiveListScope.ImmersiveListMoviesRow(
    modifier: Modifier = Modifier,
    itemWidth: Dp = LocalConfiguration.current.screenWidthDp.dp.times(0.165f),
    itemDirection: ItemDirection = ItemDirection.Vertical,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    focusedItemIndex: (index: Int) -> Unit = {},
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
                TvLazyRow(
                    pivotOffsets = PivotOffsets(parentFraction = 0.07f)
                ) {
                    item { Spacer(modifier = Modifier.padding(start = startPadding)) }

                    movieState.forEachIndexed { index, movie ->
                        item {
                            key(movie.id) {
                                MoviesRowItem(
                                    modifier = Modifier
                                        .immersiveListItem(index)
                                        .restorableFocus(),
                                    focusedItemIndex = focusedItemIndex,
                                    index = index,
                                    itemWidth = itemWidth,
                                    itemDirection = itemDirection,
                                    onMovieClick = onMovieClick,
                                    movie = movie,
                                    showItemTitle = showItemTitle,
                                    showIndexOverImage = showIndexOverImage
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.padding(end = 20.dp)) }
                    }

                    item { Spacer(modifier = Modifier.padding(start = endPadding)) }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
private fun MoviesRowItem(
    modifier: Modifier = Modifier,
    focusedItemIndex: (index: Int) -> Unit,
    index: Int,
    itemWidth: Dp,
    itemDirection: ItemDirection,
    onMovieClick: (movie: Movie) -> Unit,
    movie: Movie,
    showItemTitle: Boolean,
    showIndexOverImage: Boolean
) {
    var isItemFocused by remember { mutableStateOf(false) }

    StandardCardLayout(
        modifier = Modifier
            .width(itemWidth)
            .onFocusChanged {
                isItemFocused = it.isFocused
                if (isItemFocused) {
                    focusedItemIndex(index)
                }
            }
            .focusProperties {
                if (index == 0) {
                    left = FocusRequester.Cancel
                }
            }
            .then(modifier),
        title = {
            MoviesRowItemText(
                showItemTitle = showItemTitle,
                isItemFocused = isItemFocused,
                movie = movie
            )
        },
        imageCard = {
            CardLayoutDefaults.ImageCard(
                onClick = { onMovieClick(movie) },
                shape = CardDefaults.shape(CodeChallengeTVCardShape),
                border = CardDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = CodeChallengeTVBorderWidth,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = CodeChallengeTVCardShape
                    )
                ),
                scale = CardDefaults.scale(focusedScale = 1f),
                interactionSource = it
            ) {
                MoviesRowItemImage(
                    modifier = Modifier.aspectRatio(itemDirection.aspectRatio),
                    showIndexOverImage = showIndexOverImage,
                    movie = movie,
                    index = index

                )
            }
        }
    )
}

@Composable
private fun MoviesRowItemImage(
    modifier: Modifier = Modifier,
    showIndexOverImage: Boolean,
    movie: Movie,
    index: Int
) {
    Box(contentAlignment = Alignment.CenterStart) {
        AsyncImage(
            modifier = modifier
                .fillMaxWidth()
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        if (showIndexOverImage) {
                            drawRect(
                                color = Color.Black.copy(
                                    alpha = 0.1f
                                )
                            )
                        }
                    }
                },
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(movie.posterUri)
                .build(),
            contentDescription = "movie poster of ${movie.name}",
            contentScale = ContentScale.Crop
        )
        if (showIndexOverImage) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "#${index.inc()}",
                style = MaterialTheme.typography.displayLarge
                    .copy(
                        shadow = Shadow(
                            offset = Offset(0.5f, 0.5f),
                            blurRadius = 5f
                        ),
                        color = Color.White
                    ),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MoviesRowItemText(
    showItemTitle: Boolean,
    isItemFocused: Boolean,
    movie: Movie
) {
    if (showItemTitle) {
        val movieNameAlpha by animateFloatAsState(
            targetValue = if (isItemFocused) 1f else 0f,
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
}
