package com.weathergroup.codechallengetv.presentation.screens.movies.tv

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.tv.foundation.lazy.list.TvLazyColumn
import com.weathergroup.codechallengetv.presentation.LocalMovieRepository

object MovieDetailsScreenTV {
    const val MovieIdBundleKey = "movieId"
}

@Composable
fun MovieDetailsScreenTV(
    movieId: String,
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit
) {
    val movieRepository = LocalMovieRepository.current!!
    val movieDetails = remember {
        movieRepository.getMovieDetails(
            movieId = movieId,
            aspectRatio = "getMovies_16_9"
        )
    }

    BackHandler(onBack = onBackPressed)

    TvLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        item {
            MovieDetailsTV(
                movieDetails = movieDetails,
                goToMoviePlayer = goToMoviePlayer
            )
        }
    }
}
