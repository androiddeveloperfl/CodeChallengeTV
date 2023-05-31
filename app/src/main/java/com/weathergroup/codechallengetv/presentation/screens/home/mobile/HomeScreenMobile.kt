package com.weathergroup.codechallengetv.presentation.screens.home.mobile

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.data.util.StringConstants
import com.weathergroup.codechallengetv.presentation.LocalMovieRepository
import com.weathergroup.codechallengetv.presentation.common.mobile.MobileMoviesRow
import com.weathergroup.codechallengetv.presentation.screens.dashboard.rememberChildPadding

@Composable
fun HomeScreenMobile(
    onMovieClick: (movie: Movie) -> Unit,
    goToMoviePlayer: () -> Unit
) {
    val childPadding = rememberChildPadding()
    val lazyListState = rememberLazyListState()
    val movieRepository = LocalMovieRepository.current!!
    val featuredMovies = remember { movieRepository.getFeaturedMovies() }
    val trendingMovies = remember { movieRepository.getTrendingMovies() }
    val top10Movies = remember { movieRepository.getTop10Movies() }
    val nowPlayingMovies = remember { movieRepository.getNowPlayingMovies() }
    val carouselMovies = remember { movieRepository.getMovies_2_3() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState
    ) {
        item {
            MobileMoviesRow(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                title = StringConstants.Composable.NewReleaseMoviesTitle,
                itemWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.3f),
                movies = nowPlayingMovies,
                onMovieClick = onMovieClick
            )
        }
        item {
            MobileMoviesRow(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                title = StringConstants.Composable.PopularFilmsTitle,
                itemWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.4f),
                movies = featuredMovies,
                onMovieClick = onMovieClick
            )
        }
        item {
            MobileMoviesRow(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                title = StringConstants.Composable.TopTenMoviesTitle,
                itemWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.4f),
                movies = top10Movies,
                onMovieClick = onMovieClick
            )
        }
        item {
            MobileMoviesRow(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                title = StringConstants.Composable.HomeScreenTrendingTitle,
                itemWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.3f),
                movies = trendingMovies,
                onMovieClick = onMovieClick
            )
        }
        item {
            FeaturedMoviesCarouselMobile(
                movies = carouselMovies,
                padding = childPadding,
                goToMoviePlayer = goToMoviePlayer
            )
        }
        item {
            Spacer(
                modifier = Modifier.padding(
                    bottom = LocalConfiguration.current.screenHeightDp.dp.times(0.05f)
                )
            )
        }
    }
}
