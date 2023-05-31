package com.weathergroup.codechallengetv.data.util

object StringConstants {

    object Assets {
        const val Top250Movies = "movies.json"
        const val MostPopularMovies = "movies.json"
        const val InTheaters = "movies.json"
    }

    object Composable {
        object ContentDescription {
            fun moviePoster(movieName: String) = "Movie poster of $movieName"
        }

        const val HomeScreenTrendingTitle = "Trending"
        const val TopTenMoviesTitle = "Top 10 in the US"
        const val NewReleaseMoviesTitle = "New Releases"
        const val PopularFilmsTitle = "Popular Films"

        const val VideoPlayerControlPlaylistButton = "Playlist Button"
        const val VideoPlayerControlClosedCaptionsButton = "Playlist Button"
        const val VideoPlayerControlSettingsButton = "Playlist Button"
        const val VideoPlayerControlPlayPauseButton = "Playlist Button"
        const val SampleVideoUrl =
            "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
    }
}
