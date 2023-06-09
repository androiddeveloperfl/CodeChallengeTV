package com.weathergroup.codechallengetv.presentation.screens.videoPlayer

import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.weathergroup.codechallengetv.data.util.StringConstants
import com.weathergroup.codechallengetv.presentation.screens.videoPlayer.components.VideoPlayerControls
import com.weathergroup.codechallengetv.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import com.weathergroup.codechallengetv.presentation.utils.handleDPadKeyEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    mediaUri: Uri = Uri.parse(StringConstants.Composable.SampleVideoUrl),
    onBackPressed: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var contentCurrentPosition: Long by remember { mutableStateOf(0L) }
    val videoPlayerState = rememberVideoPlayerState(hideSeconds = 4)
    val currentView = LocalView.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )
                val source = HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(mediaUri))

                setMediaSource(source)
                prepare()
            }
    }

    BackHandler(onBack = onBackPressed)

    LaunchedEffect(Unit) {
        while (true) {
            delay(300)
            contentCurrentPosition = exoPlayer.currentPosition
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    LaunchedEffect(Unit) {
        with(exoPlayer) {
            playWhenReady = true
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    Box {
        DisposableEffect(
            AndroidView(
                modifier = Modifier
                    .handleDPadKeyEvents(
                        onEnter = {
                            if (!videoPlayerState.isDisplayed) {
                                coroutineScope.launch {
                                    videoPlayerState.showControls()
                                }
                            }
                        }
                    )
                    .focusable()
                    .background(Color.Black),
                factory = {
                    PlayerView(context).apply {
                        hideController()
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    }
                }
            )
        ) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> {
                        exoPlayer.pause()
                        exoPlayer.playWhenReady = false
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        exoPlayer.playWhenReady = true
                        exoPlayer.play()
                    }

                    Lifecycle.Event.ON_CREATE -> {
                        currentView.keepScreenOn = true
                    }

                    Lifecycle.Event.ON_STOP -> {
                        exoPlayer.pause()
                        exoPlayer.playWhenReady = false
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        currentView.keepScreenOn = false
                        exoPlayer.stop()
                        exoPlayer.clearMediaItems()
                    }

                    else -> {}
                }
            }
            val lifecycle = lifecycleOwner.value.lifecycle
            lifecycle.addObserver(observer)

            onDispose {
                exoPlayer.release()
                lifecycle.removeObserver(observer)
            }
        }
        VideoPlayerControls(
            modifier = Modifier.align(Alignment.BottomCenter),
            isPlaying = exoPlayer.isPlaying,
            onPlayPauseToggle = { shouldPlay ->
                if (shouldPlay) {
                    exoPlayer.play()
                } else {
                    exoPlayer.pause()
                }
            },
            contentProgressInMillis = contentCurrentPosition,
            contentDurationInMillis = exoPlayer.duration,
            state = videoPlayerState,
            onSeek = { seekProgress ->
                exoPlayer.seekTo(exoPlayer.duration.times(seekProgress).toLong())
            }
        )
    }
}
