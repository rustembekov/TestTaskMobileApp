package com.example.testtaskmobileapp

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.testtaskmobileapp.features.auth.presentation.viewModel.AuthViewModel
import com.example.testtaskmobileapp.features.auth.presentation.ui.AuthScreen
import com.example.testtaskmobileapp.features.auth.presentation.ui.PhoneVerificationScreen
import com.example.testtaskmobileapp.features.chats.data.ChatViewModel
import com.example.testtaskmobileapp.features.chats.model.UserChat
import com.example.testtaskmobileapp.features.chats.ui.ChatDetailScreen
import com.example.testtaskmobileapp.features.chats.ui.ChatsScreen
import com.example.testtaskmobileapp.core.components.StatusScreen
import com.example.testtaskmobileapp.features.profile.presentation.ui.ProfileEditScreen
import com.example.testtaskmobileapp.features.profile.presentation.viewModel.ProfileViewModel
import com.example.testtaskmobileapp.features.profile.presentation.ui.ProfileScreen
import com.example.testtaskmobileapp.features.profile.presentation.viewModel.ProfileEditViewModel
import com.example.testtaskmobileapp.features.registration.presentation.viewModel.RegistrationViewModel
import com.example.testtaskmobileapp.features.registration.presentation.ui.RegistrationScreen
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class NavigationItem(
    val route: String,
    val icon: ImageVector? = null,
    val label: String
) {
    data object Auth : NavigationItem("auth", label = "auth")
    data object PhoneVerification :
        NavigationItem("phone_verification?phone={phone}", label = "phone_verification") {
            fun createRoute(phone: String):String {
                return "phone_verification?phone=$phone"
            }
        }

    data object Registration :
        NavigationItem("registration?phone={phone}", label = "Registration") {
        fun createRoute(phone: String): String {
            return "registration?phone=$phone"
        }
    }

    data object StatusScreen :
        NavigationItem("success?messageId={messageId}&isSuccess={isSuccess}&message={message}&phoneNumber={phoneNumber}", label = "Profile") {

        fun createRoute(messageId: String? = null, isSuccess: Boolean, message: String? = null, phoneNumber: String): String {
            val encodedMessage = message?.let { Uri.encode(it) } ?: ""
            val encodedMessageId = messageId ?: ""

            return "success?messageId=$encodedMessageId&isSuccess=$isSuccess&message=$encodedMessage&phoneNumber=$phoneNumber"
        }
    }


    data object Profile : NavigationItem("profile", Icons.Default.AccountCircle, "Profile")
    data object ProfileEdit : NavigationItem("profile_edit", label =  "Profile")
    data object Chats : NavigationItem("chats", Icons.Default.Email, "Chats")
    data object ChatItem : NavigationItem("chat_item?chat={chat}", label = "chat_item") {
        fun createRoute(userChat: UserChat): String {
            val gson = Gson()
            val json = gson.toJson(userChat)
            val encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
            return "chat_item?chat=$encodedJson"
        }
    }

    companion object {
        val screens = listOf(Profile, Chats)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun TaskNavigation(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in NavigationItem.screens.map { it.route }) {
                MainNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Auth.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationItem.Auth.route) {
                val viewModel: AuthViewModel = hiltViewModel()

                AuthScreen(
                    navController = navController,
                    controller = viewModel,
                    state = viewModel.state.collectAsState()
                )
            }
            composable(NavigationItem.PhoneVerification.route) {navBackStackEntry ->
                val phoneNumber = navBackStackEntry.arguments?.getString("phone")
                val viewModel: AuthViewModel = hiltViewModel()

                if (phoneNumber != null) {
                    PhoneVerificationScreen(
                        navController = navController,
                        controller = viewModel,
                        state = viewModel.state.collectAsState(),
                        phoneNumber = phoneNumber
                    )
                }
            }
            composable(NavigationItem.Registration.route) { navBackStackEntry ->
                val phone = navBackStackEntry.arguments?.getString("phone")
                val viewModel: RegistrationViewModel = hiltViewModel()

                if (phone != null) {
                    RegistrationScreen(
                        navController = navController,
                        phoneNumber = phone,
                        controller = viewModel,
                        state = viewModel.state.collectAsState()
                    )
                }
            }
            composable(NavigationItem.StatusScreen.route) { navBackStackEntry ->
                val messageId = navBackStackEntry.arguments?.getString("messageId")
                val messageSender = navBackStackEntry.arguments?.getString("message")
                val isSuccess = navBackStackEntry.arguments?.getString("isSuccess")?.toBoolean() // Convert to Boolean
                val phoneNumber = navBackStackEntry.arguments?.getString("phoneNumber")
                if (isSuccess != null && phoneNumber != null) {
                    StatusScreen(
                        navController = navController,
                        messageId = messageId,
                        messageSender = messageSender,
                        isSuccess = isSuccess,
                        phoneNumber = phoneNumber
                    )
                }
            }

            composable(NavigationItem.Chats.route) {
                val viewModel: ChatViewModel = hiltViewModel()

                ChatsScreen(
                    navController = navController,
                    controller = viewModel,
                    state = viewModel.state.collectAsState()
                )
            }
            composable(NavigationItem.ChatItem.route) { navBackStackEntry ->
                val gson = Gson()
                val encodedJson = navBackStackEntry.arguments?.getString("chat")
                val json = encodedJson?.let {
                    URLDecoder.decode(
                        it,
                        StandardCharsets.UTF_8.toString()
                    )
                }
                val userChat = json?.let {
                    gson.fromJson(
                        it,
                        UserChat::class.java
                    )
                }
                val viewModel: ChatViewModel = hiltViewModel()

                if (userChat != null) {
                    ChatDetailScreen(
                        navController = navController,
                        userChat = userChat,
                        controller = viewModel,
                        state = viewModel.state.collectAsState()
                    )
                }
            }

            composable(NavigationItem.Profile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()

                ProfileScreen(
                    navController = navController,
                    controller = viewModel,
                    state = viewModel.state.collectAsState()
                )
            }

            composable(NavigationItem.ProfileEdit.route) {
                val viewModel: ProfileEditViewModel = hiltViewModel()

                ProfileEditScreen(
                    navController = navController,
                    controller = viewModel,
                    state = viewModel.state.collectAsState()
                )
            }
        }
    }
}

