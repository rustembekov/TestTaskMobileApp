package com.example.testtaskmobileapp.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme

@Composable
fun CustomTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    isError: Boolean = false,
    leadingLabel: String? = null,
    placeholderLabel: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelSmall,
        readOnly = readOnly,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        },
        placeholder = placeholderLabel?.let {
            {
                Text(
                    text = it,
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall

                )
            }
        },
        leadingIcon = leadingLabel?.let {
            {
                Text(
                    text = it,
                    color = colorResource(R.color.primary_dark),
                    style = MaterialTheme.typography.labelSmall

                )
            }
        },
        singleLine = true,
        isError = isError,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = colorResource(R.color.gray),
            focusedTextColor = colorResource(R.color.primary_dark),
            focusedBorderColor = colorResource(R.color.cyan),
            focusedLabelColor = colorResource(R.color.cyan),
            unfocusedLabelColor = colorResource(R.color.gray),
            errorBorderColor = Color.Red.copy(alpha = 0.7f),
            errorLabelColor = Color.Red.copy(alpha = 0.7f)
        )
    )
}


@Preview(showBackground = false)
@Composable
private fun CustomTextFieldPreview(){
    MainMaterialTheme {
        CustomTextField(
            value = "testing",
            label = "testing",
            onValueChange = {}
        )
    }
}