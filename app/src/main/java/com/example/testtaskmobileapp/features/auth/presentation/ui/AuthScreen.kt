package com.example.testtaskmobileapp.features.auth.presentation.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.features.auth.presentation.controller.AuthController
import com.example.testtaskmobileapp.features.auth.domain.model.AuthState
import com.example.testtaskmobileapp.core.components.CustomTextField
import com.example.testtaskmobileapp.core.components.ErrorToast
import com.example.testtaskmobileapp.core.components.LoadingIndicator
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme
import com.hbb20.CCPCountry.setDialogTitle
import com.hbb20.CountryCodePicker

@Composable
fun AuthScreen(
    navController: NavController,
    state: State<AuthState>,
    controller: AuthController
) {
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            controller.detectCountryNumberCode(context)
        } else {
            Log.e("AuthScreen", "Location permissions not granted")
        }
    }

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            controller.detectCountryNumberCode(context)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .background(color = colorResource(R.color.background))
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LoadingIndicator(
            selectedCount = 0,
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.TopCenter)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                AuthTitle()
                Spacer(modifier = Modifier.height(16.dp))
                if (state.value.isLoading) {
                    AuthForm(navController = navController, state = state, controller = controller)
                } else {
                    CircularProgressIndicator()
                }

            }
        }
    }
}


@Composable
private fun AuthTitle() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.sign_in),
            color = colorResource(R.color.primary_dark),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun AuthForm(
    navController: NavController,
    state: State<AuthState>,
    controller: AuthController
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountryCodePickerView(state = state, controller = controller)

        Spacer(modifier = Modifier.height(16.dp))

        PhoneNumberInput(state = state, controller = controller)

        Spacer(modifier = Modifier.height(16.dp))

        ContinueButton { controller.sendPhoneNumber(navController = navController) }

        if (state.value.showToast) {
            state.value.errorMessage?.let { stringResource(it.toInt()) }?.let {
                ErrorToast(
                    message = it,
                    onDismiss = { controller.updateShowToast() }
                )
            }
        }
    }
}

@Composable
private fun PhoneNumberInput(
    state: State<AuthState>,
    controller: AuthController
) {

    CustomTextField(
        value = state.value.selectedPhoneNumber,
        label = stringResource(R.string.phone_number),
        onValueChange = {
            controller.updatePhoneNumber(it)
        },
        placeholderLabel = stringResource(R.string.enter_phone_number),
        leadingLabel = "+" + state.value.selectedCountryNumberCode,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun AccountPrompt(onCreateAccountClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.account_prompt),
            color = colorResource(R.color.gray)
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextButton(onClick = onCreateAccountClick) {
            Text(
                text = stringResource(R.string.create_an_account),
                color = colorResource(R.color.cyan)

            )
        }
    }
}


@Composable
private fun ContinueButton(onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.cyan).copy(alpha = 0.8f)
        ),
        onClick = onClick
    ) {
        Text(text = stringResource(R.string.continue_word))
    }
}

@Composable
private fun CountryCodePickerView(
    state: State<AuthState>,
    controller: AuthController,

    ) {
    if (LocalInspectionMode.current) {
        // TODO: For Preview
        Text(
            text = "Country Code Picker (Preview)",
            color = Color.Gray,
            modifier = Modifier.padding(8.dp)
        )
    } else {
        AndroidView(
            factory = { context ->
                CountryCodePicker(context).apply {
                    setCountryForNameCode(state.value.detectedCountryCode)
                    setArrowColor(android.graphics.Color.BLUE)
                    setDialogBackgroundColor(android.graphics.Color.LTGRAY)
                    setDialogTextColor(android.graphics.Color.BLACK)
                    setContentColor(android.graphics.Color.BLUE)
                    setBackgroundColor(android.graphics.Color.CYAN)
                    setFlagBorderColor(android.graphics.Color.TRANSPARENT)
                    setDialogTitle(context.getString(R.string.ccp_dialog_title))
                    setOnCountryChangeListener {
                        controller.updateCountryCode(selectedCountryCode)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview_Light() {
    val mockState: State<AuthState> = remember { mutableStateOf(MockData.mockAuthState) }
    val mockNavController = rememberNavController()
    val mockController = object : AuthController {
        override fun sendPhoneNumber(navController: NavController) {}
        override fun updateCountryCode(selectedCountyCode: String) {}
        override fun updatePhoneNumber(selectedPhoneNumber: String) {}
        override fun updateOtpValueChange(
            value: String,
            navController: NavController,
            phone: String
        ) {
        }

        override fun updateShowToast() {}
        override fun detectCountryNumberCode(context: Context) {}
    }

    MainMaterialTheme {
        AuthScreen(
            navController = mockNavController,
            state = mockState,
            controller = mockController
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthScreenPreview_Dark() {
    val mockState: State<AuthState> = remember { mutableStateOf(MockData.mockAuthState) }
    val mockNavController = rememberNavController()
    val mockController = object : AuthController {
        override fun sendPhoneNumber(navController: NavController) {}
        override fun updateCountryCode(selectedCountyCode: String) {}
        override fun updatePhoneNumber(selectedPhoneNumber: String) {}
        override fun updateOtpValueChange(
            value: String,
            navController: NavController,
            phone: String
        ) {
        }

        override fun updateShowToast() {}
        override fun detectCountryNumberCode(context: Context) {}
    }

    MainMaterialTheme {
        AuthScreen(
            navController = mockNavController,
            state = mockState,
            controller = mockController
        )
    }
}
