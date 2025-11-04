package com.creativedrewy.mozartexamples.wallpapers

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import com.creativedrewy.mozart.MozartWallpaperService
import kotlin.math.floor

class SimpleComposeWallpaper: MozartWallpaperService() {

    override val wallpaperContents: @Composable ((OffsetValues) -> Unit)
        get() = { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFC107)),
                contentAlignment = Alignment.Center
            ) {
                val gridState = rememberLazyGridState()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    modifier = Modifier
                        .wrapContentSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(4) { index ->
                        AnimatedColorSquare(index = index)
                    }
                }
            }
        }
}

@Composable
private fun AnimatedColorSquare(index: Int) {
    val colors = listOf(
        Color(0xFFE91E63), // Pink
        Color(0xFF3F51B5), // Indigo
        Color(0xFF4CAF50), // Green
        Color(0xFFFFC107), // Amber
        Color(0xFF00BCD4), // Cyan
        Color(0xFFFF5722)  // Deep Orange
    )

    val durationPerColorMillis = 1500
    val totalDurationMillis = durationPerColorMillis * colors.size

    val infiniteTransition = rememberInfiniteTransition(label = "colorCycle")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = colors.size.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = totalDurationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(offsetMillis = index * 250)
        ),
        label = "colorProgress"
    )

    val baseIndex = floor(progress).toInt() % colors.size
    val nextIndex = (baseIndex + 1) % colors.size
    val localT = progress - floor(progress)
    val animatedColor = lerp(colors[baseIndex], colors[nextIndex], localT)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .dropShadow(
                RoundedCornerShape(20.dp),
                shadow = Shadow(
                    16.dp,
                    color = Color.Black,
                    spread = 2.dp,
                    alpha = 0.25f
                )
            )
            .background(
                color = animatedColor,
                shape = RoundedCornerShape(8.dp),
            )
    )
}