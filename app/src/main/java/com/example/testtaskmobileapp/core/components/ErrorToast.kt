package com.example.testtaskmobileapp.core.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme
import kotlinx.coroutines.delay

@Composable
fun ErrorToast(
    isError: Boolean = true,
    message: String,
    onDismiss: () -> Unit
) {
    val visible = remember { mutableStateOf(true) }
    val alpha = animateFloatAsState(
        targetValue = if (visible.value) 1f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val offsetY = animateDpAsState(
        targetValue = if (visible.value) 0.dp else (-40).dp,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    if (visible.value) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(y = offsetY.value)
                .alpha(alpha.value)
                .background(
                    color = if(isError)Color.Red.copy(alpha = 0.7f) else {
                        Color.Green.copy(alpha = 0.7f)
                    },
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = colorResource(R.color.primary_dark),
                style = MaterialTheme.typography.labelSmall
            )
        }
        LaunchedEffect(Unit) {
            delay(2000)
            visible.value = false
            onDismiss()
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ToastErrorPreview() {
    MainMaterialTheme {
        ErrorToast(
            message = "Error"
        ) {

        }
    }
}