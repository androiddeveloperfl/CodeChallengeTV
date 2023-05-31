package com.weathergroup.codechallengetv.presentation.screens.home.tv

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.data.util.StringConstants
import com.weathergroup.codechallengetv.presentation.LocalMovieRepository
import com.weathergroup.codechallengetv.presentation.common.tv.TVMoviesRow

@Composable
fun HomeScreenTV(
    onMovieClick: (movie: Movie) -> Unit
) {
    val tvLazyListState = rememberTvLazyListState()
    val movieRepository = LocalMovieRepository.current!!
    val trendingMovies = remember { movieRepository.getTrendingMovies() }
    val top10Movies = remember { movieRepository.getTop10Movies() }
    val nowPlayingMovies = remember { movieRepository.getNowPlayingMovies() }

    val pivotOffset = remember { PivotOffsets() }
    val pivotOffsetForImmersiveList = remember { PivotOffsets(0f, 0f) }
    var immersiveListHasFocus by remember { mutableStateOf(false) }
    TvLazyColumn(
        modifier = Modifier.fillMaxSize(),
        pivotOffsets = if (immersiveListHasFocus) pivotOffsetForImmersiveList else pivotOffset,
        state = tvLazyListState
    ) {
        item {
            Top10MoviesListTV(
                modifier = Modifier.onFocusChanged {
                    immersiveListHasFocus = it.hasFocus
                },
                moviesState = top10Movies,
                onMovieClick = onMovieClick
            )
        }
        item {
            TVMoviesRow(
                modifier = Modifier.padding(top = 16.dp),
                movies = trendingMovies,
                title = StringConstants.Composable.HomeScreenTrendingTitle,
                onMovieClick = onMovieClick
            )
        }
        item {
            TVMoviesRow(
                modifier = Modifier.padding(top = 16.dp),
                movies = nowPlayingMovies,
                title = StringConstants.Composable.NewReleaseMoviesTitle,
                onMovieClick = onMovieClick
            )
        }
        item {
            Spacer(
                modifier = Modifier.padding(
                    bottom = LocalConfiguration.current.screenHeightDp.dp.times(0.19f)
                )
            )
        }
    }
}
