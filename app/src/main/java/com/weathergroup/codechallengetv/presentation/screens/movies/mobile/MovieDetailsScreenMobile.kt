package com.weathergroup.codechallengetv.presentation.screens.movies.mobile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.weathergroup.codechallengetv.presentation.LocalMovieRepository

object MovieDetailsScreenMobile {
    const val MovieIdBundleKey = "movieId"
}

@Composable
fun MovieDetailsScreenMobile(
    movieId: String,
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit
) {
    val movieRepository = LocalMovieRepository.current!!
    val movieDetails = remember {
        movieRepository.getMovieDetails(
            movieId = movieId,
            aspectRatio = "getMovies_2_3"
        )
    }

    BackHandler(onBack = onBackPressed)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        item {
            MovieDetailsMobile(
                movieDetails = movieDetails,
                goToMoviePlayer = goToMoviePlayer
            )
        }
    }
}
