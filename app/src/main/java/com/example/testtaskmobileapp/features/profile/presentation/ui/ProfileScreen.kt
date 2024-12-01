package com.example.testtaskmobileapp.features.profile.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.core.api.ApiConst
import com.example.testtaskmobileapp.features.profile.presentation.controller.ProfileController
import com.example.testtaskmobileapp.features.profile.domain.model.ProfileState
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme

@Composable
fun ProfileScreen(
    navController: NavController,
    state: State<ProfileState>,
    controller: ProfileController
) {
    if (state.value.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(100.dp))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.profile),
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.primary_dark),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            ImageField(imageResource = state.value.avatarUrl)


            Text(
                text = state.value.username,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = colorResource(R.color.primary_dark)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileField(
                label = stringResource(R.string.city),
                value = state.value.city ?: "Nothing"
            )
            ProfileField(
                label = stringResource(R.string.birthday),
                value = state.value.birthday ?: "Nothing"
            )
            ProfileField(
                label = stringResource(R.string.zodiac_sign),
                value = state.value.zodiacSign ?: "Nothing"
            )
            ProfileField(
                label = stringResource(R.string.about_me),
                value = state.value.aboutMe ?: "Nothing"
            )
            ProfileField(label = stringResource(R.string.phone_number), value = state.value.phone)

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { controller.onExitClick(navController) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = colorResource(R.color.primary_dark)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.exit),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Button(onClick = { controller.onEditClick(navController = navController) }) {
                    Text(
                        text = stringResource(R.string.edit),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }
        }
    }
}

@Composable
private fun ImageField(
    imageResource: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        if(!imageResource.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageResource),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = stringResource(R.string.no_image_data),
                style = MaterialTheme.typography.displayMedium,
                color = colorResource(R.color.primary_dark)
            )
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(R.color.gray)
        )
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = value,
            style = MaterialTheme.typography.displaySmall,
            color = colorResource(R.color.primary_dark)
        )
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenPreview_Dark() {
    val mockState: State<ProfileState> =
        remember { mutableStateOf(MockData.mockProfileState) }
    val mockNavController = rememberNavController()
    val mockController = object : ProfileController {
        override fun onEditClick(navController: NavController) {}
        override fun onExitClick(navController: NavController) {}
    }

    MainMaterialTheme {
        ProfileScreen(
            navController = mockNavController,
            state = mockState,
            controller = mockController
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview_Light() {
    val mockState: State<ProfileState> =
        remember { mutableStateOf(MockData.mockProfileState) }
    val mockNavController = rememberNavController()
    val mockController = object : ProfileController {
        override fun onEditClick(navController: NavController) {}
        override fun onExitClick(navController: NavController) {}
    }

    MainMaterialTheme {
        ProfileScreen(
            navController = mockNavController,
            state = mockState,
            controller = mockController
        )
    }
}