package com.weathergroup.codechallengetv.data.repositories

import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.data.entities.MovieDetails
import com.weathergroup.codechallengetv.data.models.MoviesResponse
import com.weathergroup.codechallengetv.data.util.AssetsReader
import com.weathergroup.codechallengetv.data.util.StringConstants

class MovieRepositoryImpl(assetsReader: AssetsReader) : MovieRepository {
    private val _top250Movies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.Top250Movies)
        ?: emptyList()

    private val _mostPopularMovies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.MostPopularMovies)
        ?: emptyList()

    private val _inTheatersMovies = assetsReader
        .readJsonFile<MoviesResponse>(fileName = StringConstants.Assets.InTheaters)
        ?: emptyList()

    override fun getFeaturedMovies(): List<Movie> {
        return _top250Movies
            .filterIndexed { index, _ -> listOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19).contains(index) }
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_16_9,
                    name = it.title,
                    description = it.description
                )
            }
    }

    override fun getTrendingMovies(): List<Movie> {
        return _mostPopularMovies.subList(fromIndex = 0, toIndex = 10).map {
            Movie(
                id = it.id,
                description = it.description,
                name = it.title,
                posterUri = it.image_2_3
            )
        }
    }

    override fun getTop10Movies(): List<Movie> {
        return _top250Movies.subList(fromIndex = 20, toIndex = 30).map {
            Movie(
                id = it.id,
                posterUri = it.image_16_9,
                name = it.title,
                description = it.description
            )
        }
    }

    override fun getNowPlayingMovies(): List<Movie> {
        return _inTheatersMovies
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.description
                )
            }
            .subList(fromIndex = 0, toIndex = 10)
    }

    override fun getMovieDetails(movieId: String, aspectRatio: String): MovieDetails {
        var movies: List<Movie> = emptyList()
        when (aspectRatio) {
            "getMovies_16_9" -> movies = getMovies_16_9()
            "getMovies_2_3" -> movies = getMovies_2_3()
        }
        val movie = movies.find { it.id == movieId }
        return MovieDetails(
            id = movie?.id ?: String(),
            posterUri = movie?.posterUri ?: String(),
            name = movie?.name ?: String(),
            description = movie?.description ?: String(),
            pgRating = "PG-13",
            duration = "1h 59m"
        )
    }

    override fun getMovies_16_9(): List<Movie> {
        return _top250Movies
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_16_9,
                    name = it.title,
                    description = it.description
                )
            }
    }

    override fun getMovies_2_3(): List<Movie> {
        return _top250Movies
            .map {
                Movie(
                    id = it.id,
                    posterUri = it.image_2_3,
                    name = it.title,
                    description = it.description
                )
            }
    }
}
