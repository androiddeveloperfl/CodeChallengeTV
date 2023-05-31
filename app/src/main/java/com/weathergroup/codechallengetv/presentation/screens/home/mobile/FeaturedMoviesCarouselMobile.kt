package com.weathergroup.codechallengetv.presentation.screens.home.mobile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.weathergroup.codechallengetv.R
import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.data.util.StringConstants
import com.weathergroup.codechallengetv.presentation.theme.CodeChallengeTVBorderWidth
import com.weathergroup.codechallengetv.presentation.theme.CodeChallengeTVButtonShape
import com.weathergroup.codechallengetv.presentation.utils.Padding
import com.weathergroup.codechallengetv.presentation.utils.handleDPadKeyEvents

@OptIn(
    ExperimentalTvMaterial3Api::class
)
@Composable
fun FeaturedMoviesCarouselMobile(
    movies: List<Movie>,
    padding: Padding,
    goToMoviePlayer: () -> Unit
) {
    val carouselHeight = LocalConfiguration.current.screenHeightDp.dp.times(0.60f)
    var carouselCurrentIndex by rememberSaveable { mutableStateOf(0) }
    val carouselState = remember { CarouselState(initialActiveItemIndex = carouselCurrentIndex) }

    LaunchedEffect(carouselState.activeItemIndex) {
        carouselCurrentIndex = carouselState.activeItemIndex
    }

    val carouselBorderAlpha by animateFloatAsState(
        targetValue = 1f,
        label = ""
    )

    AnimatedContent(
        targetState = movies,
        label = "Featured Carousel animation",
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = padding.start, end = padding.start, top = padding.top)
            .height(carouselHeight)
            .border(
                width = CodeChallengeTVBorderWidth,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = carouselBorderAlpha),
                shape = ShapeDefaults.Medium
            )
            .clip(ShapeDefaults.Medium)
    ) { moviesState ->
        Carousel(
            modifier = Modifier
                .fillMaxSize()
                .handleDPadKeyEvents(onEnter = goToMoviePlayer),
            itemCount = moviesState.size,
            carouselState = carouselState,
            carouselIndicator = {
                Box(
                    modifier = Modifier
                        .padding(32.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        .graphicsLayer {
                            clip = true
                            shape = ShapeDefaults.ExtraSmall
                        }
                        .align(Alignment.BottomEnd)
                ) {
                    CarouselDefaults.IndicatorRow(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        itemCount = moviesState.size,
                        activeItemIndex = carouselState.activeItemIndex
                    )
                }
            },
            contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000))
                .togetherWith(fadeOut(tween(durationMillis = 1000))),
            contentTransformEndToStart = fadeIn(tween(durationMillis = 1000))
                .togetherWith(fadeOut(tween(durationMillis = 1000))),
            content = { index ->
                val movie = remember(index) { moviesState[index] }
                CarouselItem(
                    background = {
                        Box {
                            AsyncImage(
                                model = movie.posterUri,
                                contentDescription = StringConstants
                                    .Composable
                                    .ContentDescription
                                    .moviePoster(movie.name),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.5f)
                                            )
                                        )
                                    )
                            )
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = movie.name,
                                style = MaterialTheme.typography.displayMedium.copy(
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        offset = Offset(x = 2f, y = 4f),
                                        blurRadius = 2f
                                    )
                                ),
                                maxLines = 5
                            )
                            Text(
                                text = movie.description,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.65f
                                    ),
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        offset = Offset(x = 2f, y = 4f),
                                        blurRadius = 2f
                                    )
                                ),
                                maxLines = 10,
                                modifier = Modifier.padding(top = 80.dp)
                            )

                            AnimatedVisibility(
                                visible = true,
                                content = {
                                    Spacer(
                                        modifier = Modifier.height(
                                            LocalConfiguration.current.screenHeightDp.dp.times(0.5f)
                                        )
                                    )
                                    WatchNowButton(goToMoviePlayer = goToMoviePlayer)
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun WatchNowButton(goToMoviePlayer: () -> Unit) {
    Button(
        onClick = goToMoviePlayer,
        modifier = Modifier.padding(top = 8.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        shape = CodeChallengeTVButtonShape
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.watch_now),
            style = MaterialTheme.typography.titleSmall
        )
    }
}
