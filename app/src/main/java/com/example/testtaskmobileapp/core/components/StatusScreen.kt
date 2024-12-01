package com.example.testtaskmobileapp.core.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testtaskmobileapp.NavigationItem
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.core.domain.models.StatusEnum
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme
import kotlinx.coroutines.delay

@Composable
fun StatusScreen(
    navController: NavController,
    phoneNumber: String,
    messageId: String? = null ,
    messageSender: String? = null,
    isSuccess: Boolean
) {
    val message = when {
        !messageId.isNullOrEmpty() -> {
            stringResource(id = messageId.toInt())
        }
        !messageSender.isNullOrEmpty() -> {
            messageSender
        }
        else -> {
            "Unknown message"
        }
    }
    val iconScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing), label = ""
    )

    val messageAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500, delayMillis = 300, easing = FastOutSlowInEasing),
        label = ""
    )

    LaunchedEffect(message) {
        delay(3000)
        val route = when (messageId) {
            StatusEnum.REGISTERED.messageResId.toString(), StatusEnum.FAILURE_LOGIN.messageResId.toString() -> {
                NavigationItem.Auth.route
            }
            StatusEnum.LOGIN.messageResId.toString() -> {
                NavigationItem.Profile.route
            }
            else -> {
                NavigationItem.Registration.createRoute(phone = phoneNumber)
            }
        }
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = colorResource(R.color.background)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = if (isSuccess) "Success" else "Failure",
            tint = if (isSuccess) Color.Green else Color.Red,
            modifier = Modifier
                .scale(iconScale)
                .size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = messageAlpha > 0f) {
            Text(
                text = message,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.primary_dark),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(messageAlpha)
                    .scale(messageAlpha)
            )
        }
    }
}

@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StatusScreenPreview() {
    val mockNavController = rememberNavController()
    MainMaterialTheme {
        StatusScreen(
            navController = mockNavController,
            messageId = R.string.successfully_registered.toString(),
            isSuccess = true,
            phoneNumber = ""
        )
    }
}
