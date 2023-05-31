package com.weathergroup.codechallengetv.presentation.screens.home.tv

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ImmersiveList
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weathergroup.codechallengetv.data.entities.Movie
import com.weathergroup.codechallengetv.data.util.StringConstants
import com.weathergroup.codechallengetv.presentation.common.tv.ImmersiveListMoviesRow
import com.weathergroup.codechallengetv.presentation.common.tv.ItemDirection
import com.weathergroup.codechallengetv.presentation.screens.dashboard.rememberChildPadding

@Composable
fun Top10MoviesListTV(
    modifier: Modifier = Modifier,
    moviesState: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit
) {
    var currentItemIndex by remember { mutableStateOf(0) }
    var isListFocused by remember { mutableStateOf(false) }
    var listCenterOffset by remember { mutableStateOf(Offset.Zero) }

    var currentYCoord: Float? by remember { mutableStateOf(null) }

    ImmersiveList(
        modifier = modifier.onGloballyPositioned { currentYCoord = it.positionInWindow().y },
        background = { _, listHasFocus ->
            isListFocused = listHasFocus
            val gradientColor = MaterialTheme.colorScheme.surface
            AnimatedVisibility(
                visible = isListFocused,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier
                    .height(LocalConfiguration.current.screenHeightDp.times(0.8f).dp)
                    .drawWithCache {
                        onDrawWithContent {
                            if (listCenterOffset == Offset.Zero) {
                                listCenterOffset = center
                            }
                            drawContent()
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        gradientColor,
                                        Color.Transparent
                                    ),
                                    startX = size.width.times(0.2f),
                                    endX = size.width.times(0.7f)
                                )
                            )
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        gradientColor
                                    ),
                                    endY = size.width.times(0.3f)
                                )
                            )
                            drawRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        gradientColor,
                                        Color.Transparent
                                    ),
                                    start = Offset(
                                        size.width.times(0.2f),
                                        size.height.times(0.5f)
                                    ),
                                    end = Offset(
                                        size.width.times(0.9f),
                                        0f
                                    )
                                )
                            )
                        }
                    }
            ) {
                val movie = remember(moviesState, currentItemIndex) {
                    moviesState[currentItemIndex]
                }
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            LocalConfiguration.current.screenHeightDp.times(0.8f).dp
                        ),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterUri)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        },
        list = {
            Column {
                if (isListFocused) {
                    val movie = remember(moviesState, currentItemIndex) {
                        moviesState[currentItemIndex]
                    }
                    Column(
                        modifier = Modifier.padding(
                            start = rememberChildPadding().start,
                            bottom = 32.dp
                        )
                    ) {
                        Text(text = movie.name, style = MaterialTheme.typography.displaySmall)
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            text = movie.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                ImmersiveListMoviesRow(
                    itemWidth = LocalConfiguration.current.screenWidthDp.dp.times(0.28f),
                    itemDirection = ItemDirection.Horizontal,
                    movies = moviesState,
                    title = if (isListFocused) {
                        null
                    } else {
                        StringConstants.Composable.TopTenMoviesTitle
                    },
                    showItemTitle = !isListFocused,
                    onMovieClick = onMovieClick,
                    showIndexOverImage = true,
                    focusedItemIndex = { focusedIndex -> currentItemIndex = focusedIndex }
                )
            }
        }
    )
}
