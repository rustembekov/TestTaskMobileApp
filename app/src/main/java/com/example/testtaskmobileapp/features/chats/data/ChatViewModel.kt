package com.example.testtaskmobileapp.features.chats.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.testtaskmobileapp.NavigationItem
import com.example.testtaskmobileapp.features.chats.model.UserChat
import com.example.testtaskmobileapp.features.chats.model.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
) : ViewModel(), ChatController {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    override fun onClickChat(
        navController: NavController,
        userChat: UserChat
    ) {
        navController.navigate(NavigationItem.ChatItem.createRoute(userChat = userChat))
    }

    override fun updateChatMessage(newMessage: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    newMessage = newMessage
                )
            }
        }
    }
}