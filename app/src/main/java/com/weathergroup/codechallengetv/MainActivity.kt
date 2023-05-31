package com.weathergroup.codechallengetv

import android.app.UiModeManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import com.google.gson.Gson
import com.weathergroup.codechallengetv.data.repositories.MovieRepositoryImpl
import com.weathergroup.codechallengetv.data.util.AssetsReader
import com.weathergroup.codechallengetv.presentation.LocalMovieRepository
import com.weathergroup.codechallengetv.presentation.screens.Screens
import com.weathergroup.codechallengetv.presentation.screens.dashboard.DashboardScreen
import com.weathergroup.codechallengetv.presentation.screens.movies.mobile.MovieDetailsScreenMobile
import com.weathergroup.codechallengetv.presentation.screens.movies.tv.MovieDetailsScreenTV
import com.weathergroup.codechallengetv.presentation.screens.videoPlayer.VideoPlayerScreen
import com.weathergroup.codechallengetv.presentation.theme.CodeChallengeTVTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
            val isTvDevice: Boolean =
                uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
            val movieRepository = remember {
                MovieRepositoryImpl(
                    AssetsReader(context, Gson())
                )
            }

            CompositionLocalProvider(LocalMovieRepository provides movieRepository) {
                App(isTvDevice)
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun App(isTvDevice: Boolean) {
    CodeChallengeTVTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) {
                val navController = rememberNavController()
                var isComingBackFromDifferentScreen by remember { mutableStateOf(false) }

                NavHost(
                    navController = navController,
                    startDestination = Screens.Dashboard(),
                    builder = {
                        composable(
                            route = Screens.TVMovieDetails(),
                            arguments = listOf(
                                navArgument(MovieDetailsScreenTV.MovieIdBundleKey) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val movieId =
                                backStackEntry.arguments?.getString(
                                    MovieDetailsScreenTV.MovieIdBundleKey
                                )
                            movieId?.let { nnMovieId ->

                                MovieDetailsScreenTV(
                                    movieId = nnMovieId,
                                    goToMoviePlayer = {
                                        navController.navigate(Screens.VideoPlayer())
                                    },
                                    onBackPressed = {
                                        if (navController.navigateUp()) {
                                            isComingBackFromDifferentScreen = true
                                        }
                                    }
                                )
                            }
                        }
                        composable(
                            route = Screens.MobileMovieDetails(),
                            arguments = listOf(
                                navArgument(MovieDetailsScreenMobile.MovieIdBundleKey) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val movieId =
                                backStackEntry.arguments?.getString(
                                    MovieDetailsScreenMobile.MovieIdBundleKey
                                )
                            movieId?.let { nnMovieId ->
                                MovieDetailsScreenMobile(
                                    movieId = nnMovieId,
                                    goToMoviePlayer = {
                                        navController.navigate(Screens.VideoPlayer())
                                    },
                                    onBackPressed = {
                                        if (navController.navigateUp()) {
                                            isComingBackFromDifferentScreen = true
                                        }
                                    }
                                )
                            }
                        }
                        composable(route = Screens.Dashboard()) {
                            DashboardScreen(
                                openTVMovieDetailsScreen = { movieId ->
                                    navController.navigate(
                                        Screens.TVMovieDetails.withArgs(movieId)
                                    )
                                },
                                openMobileMovieDetailsScreen = { movieId ->
                                    navController.navigate(
                                        Screens.MobileMovieDetails.withArgs(movieId)
                                    )
                                },
                                openVideoPlayer = {
                                    navController.navigate(Screens.VideoPlayer())
                                },
                                isTvDevice = isTvDevice
                            )
                        }
                        composable(route = Screens.VideoPlayer()) {
                            VideoPlayerScreen(
                                onBackPressed = {
                                    if (navController.navigateUp()) {
                                        isComingBackFromDifferentScreen = true
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}
