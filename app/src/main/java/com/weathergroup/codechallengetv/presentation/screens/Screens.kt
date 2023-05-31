package com.weathergroup.codechallengetv.presentation.screens

import com.weathergroup.codechallengetv.presentation.screens.movies.tv.MovieDetailsScreenTV

enum class Screens(
    private val args: List<String>? = null
) {
    MobileHome,
    TVHome,
    TVMovieDetails(listOf(MovieDetailsScreenTV.MovieIdBundleKey)),
    MobileMovieDetails(listOf(MovieDetailsScreenTV.MovieIdBundleKey)),
    Dashboard,
    VideoPlayer;

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }
}
