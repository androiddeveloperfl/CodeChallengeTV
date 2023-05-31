package com.weathergroup.codechallengetv.presentation

import androidx.compose.runtime.compositionLocalOf
import com.weathergroup.codechallengetv.data.repositories.MovieRepository

val LocalMovieRepository = compositionLocalOf<MovieRepository?> { null }
