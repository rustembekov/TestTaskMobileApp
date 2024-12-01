package com.example.testtaskmobileapp.features.auth.presentation.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.core.components.LoadingIndicator
import com.example.testtaskmobileapp.features.auth.domain.model.AuthState
import com.example.testtaskmobileapp.features.auth.presentation.controller.AuthController
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme

@Composable
fun PhoneVerificationScreen(
    state: State<AuthState>,
    controller: AuthController,
    navController: NavController,
    phoneNumber: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.background)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OtpVerificationTopBar(onBackClick = navController::navigateUp)
        Column {
            OtpScreen(
                state = state.value,
                onValueChange = {controller.updateOtpValueChange(it, navController = navController, phone = phoneNumber)}
            )
            ResendCodeButton()
        }
    }
}

@Composable
private fun OtpVerificationTopBar(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BackButton(onBackClick)
            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TitleText(text = stringResource(R.string.phone_verification))
                LoadingIndicator(selectedCount = 1)
            }

            Spacer(modifier = Modifier.weight(1f))
        }
        SubtitleText(text = stringResource(R.string.phone_verification_content))
    }
}


@Composable
private fun BackButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = colorResource(R.color.background_arrow_back))
            .size(32.dp),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = "Back",
            tint = colorResource(R.color.primary_dark)
        )
    }
}

@Composable
private fun TitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.displayLarge,
        color = colorResource(R.color.primary_dark)
    )
}

@Composable
private fun SubtitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = colorResource(R.color.primary_dark),
        textAlign = TextAlign.Left
    )
}

@Composable
private fun ResendCodeButton() {
    TextButton(
        onClick = {
            // TODO: Resend Code
        }
    ) {
        Text(
            text = stringResource(R.string.resend_code),
            color = colorResource(R.color.cyan),
            style = MaterialTheme.typography.displayLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPhoneVerification_Light() {
    val mockState: State<AuthState> = remember { mutableStateOf(MockData.mockAuthState) }
    val mockNavController = rememberNavController()
    val mockController = object : AuthController {
        override fun sendPhoneNumber(navController: NavController) {}
        override fun updateCountryCode(selectedCountyCode: String) {}
        override fun updatePhoneNumber(selectedPhoneNumber: String) {}
        override fun updateOtpValueChange(value: String, navController: NavController, phone: String) {}
        override fun updateShowToast() {}
        override fun detectCountryNumberCode(context: Context) {}
    }
    MainMaterialTheme {
        PhoneVerificationScreen(
            state = mockState,
            controller = mockController,
            navController = mockNavController,
            phoneNumber = ""
        )
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPhoneVerification_Night() {
    val mockState: State<AuthState> = remember { mutableStateOf(MockData.mockAuthState) }
    val mockNavController = rememberNavController()
    val mockController = object : AuthController {
        override fun sendPhoneNumber(navController: NavController) {}
        override fun updateCountryCode(selectedCountyCode: String) {}
        override fun updatePhoneNumber(selectedPhoneNumber: String) {}
        override fun updateOtpValueChange(value: String, navController: NavController, phone: String) {}
        override fun updateShowToast() {}
        override fun detectCountryNumberCode(context: Context) {}
    }
    MainMaterialTheme {
        PhoneVerificationScreen(
            state = mockState,
            controller = mockController,
            navController = mockNavController,
            phoneNumber = ""
        )
    }
}

