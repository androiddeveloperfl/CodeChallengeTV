package com.weathergroup.codechallengetv.presentation.screens.videoPlayer.components

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VideoPlayerState internal constructor(
    @IntRange(from = 0)
    val hideSeconds: Int
) {
    var isDisplayed by mutableStateOf(false)
    private val countDownTimer = MutableStateFlow(value = hideSeconds)

    init {
        MainScope().launch {
            countDownTimer.collectLatest { time ->
                if (time > 0) {
                    isDisplayed = true
                    delay(1000)
                    countDownTimer.emit(countDownTimer.value - 1)
                } else {
                    isDisplayed = false
                }
            }
        }
    }

    suspend fun showControls(seconds: Int = hideSeconds) {
        countDownTimer.emit(seconds)
    }
}

/**
 * Create and remember a [VideoPlayerState] instance. Useful when trying to control the state of
 * the [VideoPlayerControls]-related composable.
 * @return A remembered instance of [VideoPlayerState].
 * @param hideSeconds How many seconds should the controls be visible before being hidden.
 * */
@Composable
fun rememberVideoPlayerState(@IntRange(from = 0) hideSeconds: Int = 2) =
    remember { VideoPlayerState(hideSeconds = hideSeconds) }
