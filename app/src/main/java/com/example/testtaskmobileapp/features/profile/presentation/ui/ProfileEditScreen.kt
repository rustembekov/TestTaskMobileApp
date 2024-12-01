package com.example.testtaskmobileapp.features.profile.presentation.ui

import android.Manifest
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.core.api.ApiConst
import com.example.testtaskmobileapp.core.components.ErrorToast
import com.example.testtaskmobileapp.features.profile.domain.model.ProfileState
import com.example.testtaskmobileapp.features.profile.presentation.controller.ProfileEditController
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.Dimens
import java.util.*

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileEditScreen(
    navController: NavController,
    state: State<ProfileState>,
    controller: ProfileEditController,
) {
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { controller.onChangeAvatar(uri = it, contentResolver = context.contentResolver) }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            // Permission Denied: Do something
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(16.dp)
    ) {
        if (state.value.showToast) {
            ErrorToast(
                message = stringResource(state.value.errorMessage),
                isError = false
            ) {
                controller.updateShowToast()
            }
        }

        Text(
            text = stringResource(R.string.edit_profile),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = colorResource(R.color.primary_dark)
        )

        if (state.value.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AvatarSelector(
                        imageResource = if(state.value.avatarUri.isNullOrEmpty()) {
                            state.value.avatarUrl
                        } else {
                            state.value.avatarUri
                        },
                        onChangeAvatar = {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_MEDIA_IMAGES
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                imagePickerLauncher.launch("image/*")
                            } else {
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }
                        })
                }

                item {
                    ReadOnlyProfileField(
                        label = stringResource(R.string.username),
                        value = state.value.username
                    )
                }

                item {
                    ReadOnlyProfileField(
                        label = stringResource(R.string.phone_number),
                        value = state.value.phone
                    )
                }

                item {
                    EditableProfileField(
                        label = stringResource(R.string.city),
                        value = state.value.city ?: "",
                        onValueChange = { controller.onCityChange(it) }
                    )
                }

                item {
                    EditableDateField(
                        value = state.value.birthday ?: "",
                        onShowDatePicker = {
                            controller.updateShowDatePicker(true)
                        },
                    )
                }

                item {
                    EditableProfileField(
                        label = stringResource(R.string.zodiac_sign),
                        value = state.value.zodiacSign ?: "",
                        onValueChange = { controller.onZodiacSignChange(it) },
                    )
                }

                item {
                    EditableProfileField(
                        label = stringResource(R.string.about_me),
                        value = state.value.aboutMe ?: "",
                        onValueChange = { controller.onAboutMeChange(it) },
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { controller.onSaveClick(navController) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green.copy(alpha = 0.7f),
                                contentColor = colorResource(R.color.primary_dark)
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.save),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Button(
                            onClick = {
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Gray,
                                contentColor = colorResource(R.color.primary_dark)
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }


        }
    }

    LaunchedEffect(state.value.showDatePicker) {
        if (state.value.showDatePicker) {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$year-${month + 1}-$dayOfMonth"
                    controller.onBirthdayChange(selectedDate)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            ).apply {
                setOnDismissListener {
                    controller.updateShowDatePicker(false)
                }
            }.show()
        }
    }


}

@Composable
private fun AvatarSelector(
    imageResource: String?,
    onChangeAvatar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!imageResource.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageResource),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onChangeAvatar,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green.copy(
                    alpha = 0.7f
                )
            )
        ) {
            Text(
                text = stringResource(R.string.change_avatar),
                style = MaterialTheme.typography.labelSmall,
                color = colorResource(R.color.primary_dark)
            )
        }
    }
}


@Composable
private fun ReadOnlyProfileField(
    label: String,
    value: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(R.color.primary_dark),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.fixedHeightEditProfile),
            value = value,
            onValueChange = {},
            textStyle = MaterialTheme.typography.labelSmall,
            singleLine = true,
            enabled = false,
            colors = TextFieldDefaults.colors(
                disabledTextColor = colorResource(R.color.black),
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun EditableDateField(
    value: String,
    onShowDatePicker: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.date_of_birth),
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(R.color.primary_dark),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            modifier = Modifier
                .background(colorResource(R.color.gray))
                .fillMaxWidth()
                .height(Dimens.fixedHeightEditProfile),
            onClick = onShowDatePicker,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = colorResource(R.color.white)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun EditableProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(R.color.primary_dark),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.fixedHeightEditProfile),
            value = value,
            textStyle = MaterialTheme.typography.labelSmall,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = colorResource(R.color.black),
                unfocusedTextColor = colorResource(R.color.white),
                focusedContainerColor = colorResource(R.color.white),
                unfocusedContainerColor = colorResource(R.color.gray),
            )
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileEditScreenPreview_Dark() {
    val mockState: State<ProfileState> =
        remember { mutableStateOf(MockData.mockProfileState) }
    val mockNavController = rememberNavController()
    val mockController = object : ProfileEditController {
        override fun onChangeAvatar(uri: Uri, contentResolver: ContentResolver) {}
        override fun onSaveClick(navController: NavController) {}
        override fun onCancelClick(navController: NavController) {}
        override fun onUsernameChange(newUsername: String) {}
        override fun onPhoneChange(newPhone: String) {}
        override fun onCityChange(newCity: String) {}
        override fun onBirthdayChange(newBirthday: String) {}
        override fun onZodiacSignChange(newZodiacSign: String) {}
        override fun onAboutMeChange(newAboutMe: String) {}
        override fun updateShowToast() {}
        override fun updateShowDatePicker(showDate: Boolean) {}
    }

    ProfileEditScreen(
        navController = mockNavController,
        controller = mockController,
        state = mockState
    )

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
private fun ProfileEditScreenPreview_Light() {
    val mockState: State<ProfileState> =
        remember { mutableStateOf(MockData.mockProfileState) }
    val mockNavController = rememberNavController()
    val mockController = object : ProfileEditController {
        override fun onChangeAvatar(uri: Uri, contentResolver: ContentResolver) {}
        override fun onSaveClick(navController: NavController) {}
        override fun onCancelClick(navController: NavController) {}
        override fun onUsernameChange(newUsername: String) {}
        override fun onPhoneChange(newPhone: String) {}
        override fun onCityChange(newCity: String) {}
        override fun onBirthdayChange(newBirthday: String) {}
        override fun onZodiacSignChange(newZodiacSign: String) {}
        override fun onAboutMeChange(newAboutMe: String) {}
        override fun updateShowToast() {}
        override fun updateShowDatePicker(showDate: Boolean) {}
    }

    ProfileEditScreen(
        navController = mockNavController,
        controller = mockController,
        state = mockState
    )

}
