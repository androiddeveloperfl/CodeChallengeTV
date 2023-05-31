package com.weathergroup.codechallengetv.data.repositories

import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.data.entities.MovieDetails

interface MovieRepository {
    fun getFeaturedMovies(): List<Movie>
    fun getTrendingMovies(): List<Movie>
    fun getTop10Movies(): List<Movie>
    fun getNowPlayingMovies(): List<Movie>
    fun getMovieDetails(movieId: String, aspectRatio: String): MovieDetails
    fun getMovies_16_9(): List<Movie>
    fun getMovies_2_3(): List<Movie>
}
