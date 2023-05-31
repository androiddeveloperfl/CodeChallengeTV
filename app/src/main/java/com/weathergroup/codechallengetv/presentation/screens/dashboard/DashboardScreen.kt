package com.weathergroup.codechallengetv.presentation.screens.dashboard

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.weathergroup.codechallengetv.presentation.screens.Screens
import com.weathergroup.codechallengetv.presentation.screens.home.mobile.HomeScreenMobile
import com.weathergroup.codechallengetv.presentation.screens.home.tv.HomeScreenTV
import com.weathergroup.codechallengetv.presentation.utils.Padding

val ParentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)

@Composable
fun rememberChildPadding(direction: LayoutDirection = LocalLayoutDirection.current): Padding {
    return remember {
        Padding(
            start = ParentPadding.calculateStartPadding(direction) + 8.dp,
            top = ParentPadding.calculateTopPadding(),
            end = ParentPadding.calculateEndPadding(direction) + 0.dp,
            bottom = ParentPadding.calculateBottomPadding()
        )
    }
}

@Composable
fun DashboardScreen(
    openTVMovieDetailsScreen: (movieId: String) -> Unit,
    openMobileMovieDetailsScreen: (movieId: String) -> Unit,
    openVideoPlayer: () -> Unit,
    isTvDevice: Boolean
) {
    val navController = rememberNavController()
    val startDestination: String = if (isTvDevice) {
        Screens.TVHome()
    } else {
        Screens.MobileHome()
    }

    var currentDestination: String? by remember { mutableStateOf(null) }

    fun handleBackPress() {
    }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestination = destination.route
        }
    }

    Box(
        modifier = Modifier.onPreviewKeyEvent {
            if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
                handleBackPress()
                return@onPreviewKeyEvent true
            }
            false
        }
    ) {
        val navHostTopPaddingDp by animateDpAsState(
            targetValue = 0.dp,
            animationSpec = tween(),
            label = ""
        )

        NavHost(
            modifier = Modifier.padding(top = navHostTopPaddingDp),
            navController = navController,
            startDestination = startDestination,
            builder = {
                composable(Screens.TVHome()) {
                    HomeScreenTV(
                        onMovieClick = { selectedMovie ->
                            openTVMovieDetailsScreen(selectedMovie.id)
                        }
                    )
                }
                composable(Screens.MobileHome()) {
                    HomeScreenMobile(
                        onMovieClick = { selectedMovie ->
                            openMobileMovieDetailsScreen(selectedMovie.id)
                        },
                        goToMoviePlayer = openVideoPlayer
                    )
                }
            }
        )
    }
}
