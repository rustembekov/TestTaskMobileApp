package com.example.testtaskmobileapp.features.chats.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.features.chats.data.ChatController
import com.example.testtaskmobileapp.features.chats.model.UserChat
import com.example.testtaskmobileapp.features.chats.model.ChatState
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme

@Composable
fun ChatsScreen(
    navController: NavController,
    controller: ChatController,
    state: State<ChatState>
) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.background))) {
        items(state.value.sampleUserChats) { chat ->
            ChatItem(
                userChat = chat,
                onClick = {
                    controller.onClickChat(
                        navController = navController,
                        userChat = chat
                    )
                }
            )
        }
    }
}

@Composable
private fun ChatItem(userChat: UserChat, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(colorResource(R.color.gray), shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = userChat.name.first().toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.primary_dark)
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = userChat.name,
                style = MaterialTheme.typography.displayMedium,
                color = colorResource(R.color.primary_dark)

            )
            Text(
                text = userChat.lastMessage,
                style = MaterialTheme.typography.displaySmall,
                color = colorResource(R.color.gray)
            )
        }
        Text(
            text = userChat.timestamp,
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(R.color.gray)
        )
    }
    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatScreenPreview_Dark() {
    val mockState: State<ChatState> = remember { mutableStateOf(MockData.mockChatState) }
    val mockNavController = rememberNavController()
    val mockController = object : ChatController {
        override fun onClickChat(navController: NavController, userChat: UserChat) {}
        override fun updateChatMessage(newMessage: String) {}
    }
    MainMaterialTheme {
        ChatsScreen(
            navController = mockNavController,
            controller = mockController,
            state = mockState
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChatScreenPreview_Light() {
    val mockState: State<ChatState> = remember { mutableStateOf(MockData.mockChatState) }
    val mockNavController = rememberNavController()
    val mockController = object : ChatController {
        override fun onClickChat(navController: NavController, userChat: UserChat) {}
        override fun updateChatMessage(newMessage: String) {}
    }
    MainMaterialTheme {
        ChatsScreen(
            navController = mockNavController,
            controller = mockController,
            state = mockState
        )
    }
}
