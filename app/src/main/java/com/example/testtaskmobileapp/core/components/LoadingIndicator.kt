package com.example.testtaskmobileapp.core.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    barCount: Int = 2,
    barWidth: Dp = 60.dp,
    barHeight: Dp = 8.dp,
    barSpacing: Dp = 8.dp,
    animationDuration: Int = 1000,
    delayBetweenBars: Int = 200,
    selectedCount: Int,
    selectedColor: Color = colorResource(R.color.cyan),
    unselectedColor: Color = colorResource(R.color.gray_50_opacity)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(barSpacing)
    ) {
        repeat(barCount) { index ->
            val delay = index * delayBetweenBars

            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDuration,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse,
                    initialStartOffset = StartOffset(delay)
                ),
                label = "alpha_$index"
            )

            val isSelected = selectedCount == index
            val baseColor = if (isSelected) selectedColor else unselectedColor

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(barHeight)
                    .clip(RoundedCornerShape(barHeight / 2))
                    .background(
                        color = if (isSelected) {
                            baseColor.copy(alpha = alpha)
                        } else {
                            baseColor
                        }
                    )
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(selectedCount = 1)
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingScreen_Light() {
    MainMaterialTheme {
        LoadingScreen()
    }
}