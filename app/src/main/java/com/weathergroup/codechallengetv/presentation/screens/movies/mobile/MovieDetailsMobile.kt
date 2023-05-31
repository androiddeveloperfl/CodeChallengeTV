package com.weathergroup.codechallengetv.presentation.screens.movies.mobile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weathergroup.codechallengetv.R
import com.weathergroup.codechallengetv.data.entities.MovieDetails
import com.weathergroup.codechallengetv.data.util.StringConstants
import com.weathergroup.codechallengetv.presentation.screens.dashboard.rememberChildPadding
import com.weathergroup.codechallengetv.presentation.screens.movies.DotSeparatedRow
import com.weathergroup.codechallengetv.presentation.theme.CodeChallengeTVButtonShape

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieDetailsMobile(
    movieDetails: MovieDetails,
    goToMoviePlayer: () -> Unit
) {
    val screenConfiguration = LocalConfiguration.current
    val screenHeight = screenConfiguration.screenHeightDp
    val childPadding = rememberChildPadding()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight.dp.times(0.9f))
    ) {
        MovieImageWithGradients(
            movieDetails = movieDetails,
            bringIntoViewRequester = bringIntoViewRequester
        )

        Column(modifier = Modifier.fillMaxWidth(1f)) {
            Spacer(modifier = Modifier.height(screenHeight.dp.times(0.5f)))

            Column(
                modifier = Modifier.padding(start = childPadding.start)
            ) {
                MovieLargeTitle(movieTitle = movieDetails.name)

                Column(
                    modifier = Modifier.alpha(0.75f)
                ) {
                    MovieDescription(description = movieDetails.description)
                    DotSeparatedRow(
                        modifier = Modifier.padding(top = 20.dp),
                        texts = listOf(
                            movieDetails.pgRating,
                            movieDetails.duration
                        )
                    )
                }
                WatchNowButton(
                    modifier = Modifier,
                    goToMoviePlayer = goToMoviePlayer,
                    shouldRequestFocus = true
                )
            }
        }
    }
}

@Composable
private fun WatchNowButton(
    modifier: Modifier = Modifier,
    goToMoviePlayer: () -> Unit,
    shouldRequestFocus: Boolean
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (shouldRequestFocus) {
            focusRequester.requestFocus()
        }
    }

    Button(
        onClick = goToMoviePlayer,
        modifier = modifier
            .padding(top = 24.dp)
            .focusRequester(focusRequester),
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

@Composable
private fun MovieDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier.padding(top = 8.dp),
        maxLines = 5
    )
}

@Composable
private fun MovieLargeTitle(movieTitle: String) {
    Text(
        text = movieTitle,
        style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        maxLines = 2
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieImageWithGradients(
    movieDetails: MovieDetails,
    bringIntoViewRequester: BringIntoViewRequester
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(movieDetails.posterUri)
            .crossfade(true).build(),
        contentDescription = StringConstants
            .Composable
            .ContentDescription
            .moviePoster(movieDetails.name),
        modifier = Modifier
            .fillMaxSize()
            .bringIntoViewRequester(bringIntoViewRequester),
        contentScale = ContentScale.FillHeight
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface
                    ),
                    startY = 600f
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent
                    ),
                    endX = 1000f,
                    startX = 300f
                )
            )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent
                    ),
                    start = Offset(x = 500f, y = 500f),
                    end = Offset(x = 1000f, y = 0f)
                )
            )
    )
}
