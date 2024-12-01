package com.example.testtaskmobileapp.features.auth.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.features.auth.domain.model.AuthState
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme

@Composable
fun OtpScreen(
    state: AuthState,
    onValueChange: (String) -> Unit
) {
    val focusRequesters = List(6) { FocusRequester() }
    val digits = remember { state.digits.toMutableStateList() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.background)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            for (i in digits.indices) {
                OtpTextField(
                    value = digits[i],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            digits[i] = newValue
                            onValueChange(digits.joinToString(""))
                            if (newValue.isNotEmpty() && i < digits.lastIndex) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    },
                    onBackspace = {
                        if (digits[i].isNotEmpty()) {
                            digits[i] = ""
                            onValueChange(digits.joinToString(""))
                        } else if (i > 0) {
                            focusRequesters[i - 1].requestFocus()
                        }
                    },
                    focusRequester = focusRequesters[i],
                    imeAction = if (i == digits.lastIndex) ImeAction.Done else ImeAction.Next
                )
            }
        }
    }
}


@Composable
private fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= 1) {
                onValueChange(newValue)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        modifier = modifier
            .width(54.dp)
            .height(54.dp)
            .focusRequester(focusRequester)
            .onPreviewKeyEvent { event ->
                if (event.key == Key.Backspace && event.type == KeyEventType.KeyDown) {
                    onBackspace()
                    true
                } else {
                    false
                }
            },
        singleLine = true,
        textStyle = TextStyle(textAlign = TextAlign.Center),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(R.color.background_otp_20_focused),
            unfocusedBorderColor = colorResource(R.color.primary_dark),
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = colorResource(R.color.primary_dark),
            unfocusedTextColor = colorResource(R.color.primary_dark),

        )
    )
}

@Preview(showBackground = true)
@Composable
private fun OtpScreen_Light() {
    val mockState = MockData.mockAuthState
    MainMaterialTheme {
        OtpScreen(
            state = mockState,
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable()
private fun OtpScreen_Dark() {
    val mockState = MockData.mockAuthState
    MainMaterialTheme {
        OtpScreen(
            state = mockState,
            onValueChange = {}
        )
    }
}