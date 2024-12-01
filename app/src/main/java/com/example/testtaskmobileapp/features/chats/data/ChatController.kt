package com.example.testtaskmobileapp.features.chats.data

import androidx.navigation.NavController
import com.example.testtaskmobileapp.features.chats.model.UserChat

interface ChatController {
    fun onClickChat(navController: NavController, userChat: UserChat)
    fun updateChatMessage(newMessage: String)
}