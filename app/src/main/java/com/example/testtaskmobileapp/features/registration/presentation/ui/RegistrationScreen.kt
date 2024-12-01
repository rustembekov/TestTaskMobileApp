package com.example.testtaskmobileapp.features.registration.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.core.components.CustomTextField
import com.example.testtaskmobileapp.core.components.ErrorToast
import com.example.testtaskmobileapp.features.registration.presentation.controller.RegistrationController
import com.example.testtaskmobileapp.features.registration.domain.model.RegistrationState
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme
import com.example.testtaskmobileapp.features.registration.presentation.controller.RegistrationController as RegistrationController1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navController: NavController,
    phoneNumber: String,
    controller: RegistrationController1,
    state: State<RegistrationState>
) {
    Column(
        modifier = Modifier
            .background(colorResource(R.color.background))
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.registration),
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(R.color.primary_dark)
                    )
                }
                    },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Arrow Back",
                        tint = colorResource(R.color.primary_dark)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.background),
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = phoneNumber,
                label = stringResource(R.string.phone_number),
                onValueChange = {},
                readOnly = true
            )

            CustomTextField(
                value = state.value.name,
                label = stringResource(R.string.name),
                onValueChange = { controller.updateName(it) }
            )

            CustomTextField(
                value = state.value.surname,
                label = stringResource(R.string.surname),
                onValueChange = { controller.updateSurname(it) }
            )

            CustomTextField(
                value = state.value.username,
                label = stringResource(R.string.username),
                onValueChange = { controller.updateUsername(it) },
                isError = state.value.showToast,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
            )

            if (state.value.showToast) {
                ErrorToast(
                    message = stringResource(R.string.invalid_username),
                    onDismiss = { controller.updateShowToast() }
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { controller.saveUser(
                    phoneNumber = phoneNumber,
                    navController = navController
                ) },
                enabled = !state.value.showToast && state.value.username.isNotEmpty() && state.value.name.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(R.color.cyan),
                    containerColor = colorResource(R.color.cyan).copy(
                        alpha = 0.7f
                    )
                )
            ) {
                Text(
                    text = stringResource(R.string.register),
                    color = colorResource(R.color.primary_dark),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}



@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun RegistrationScreenPreview_Dark() {
    val mockState: State<RegistrationState> =
        remember { mutableStateOf(MockData.mockRegistrationState) }
    val mockNavController = rememberNavController()
    val mockController = object : RegistrationController {
        override fun updateName(name: String) {}
        override fun updateSurname(surname: String) {}
        override fun updateUsername(username: String) {}
        override fun updateShowToast() {}
        override fun saveUser(phoneNumber: String, navController: NavController) = Unit
    }

    MainMaterialTheme {
        RegistrationScreen(
            navController = mockNavController,
            phoneNumber = "+79389003434",
            controller = mockController,
            state = mockState
        )
    }
}
@Composable
@Preview(showBackground = true)
private fun RegistrationScreenPreview_Light() {
    val mockState: State<RegistrationState> =
        remember { mutableStateOf(MockData.mockRegistrationState) }
    val mockNavController = rememberNavController()
    val mockController = object : RegistrationController {
        override fun updateName(name: String) {}
        override fun updateSurname(surname: String) {}
        override fun updateUsername(username: String) {}
        override fun updateShowToast() {}
        override fun saveUser(phoneNumber: String, navController: NavController) = Unit
    }

    MainMaterialTheme {
        RegistrationScreen(
            navController = mockNavController,
            phoneNumber = "+79389003434",
            controller = mockController,
            state = mockState
        )
    }
}
