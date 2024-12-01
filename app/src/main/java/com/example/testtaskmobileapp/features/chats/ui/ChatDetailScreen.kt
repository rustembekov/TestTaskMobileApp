package com.example.testtaskmobileapp.features.chats.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testtaskmobileapp.R
import com.example.testtaskmobileapp.features.chats.data.ChatController
import com.example.testtaskmobileapp.features.chats.model.UserChat
import com.example.testtaskmobileapp.features.chats.model.ChatState
import com.example.testtaskmobileapp.features.chats.model.Message
import com.example.testtaskmobileapp.mockData.MockData
import com.example.testtaskmobileapp.ui.theme.MainMaterialTheme

@Composable
fun ChatDetailScreen(
    navController: NavController,
    controller: ChatController,
    state: State<ChatState>,
    userChat: UserChat
) {
    Scaffold(
        topBar = {
            ChatTopBar(
                title = userChat.name,
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(colorResource(R.color.background))
                .padding(innerPadding)
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            MessageList(messages = state.value.sampleMessages)
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            ChatInputBar(
                currentMessage = state.value.newMessage,
                onMessageChange = controller::updateChatMessage,
                onSendClick = { /* TODO: Send message */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Column {

        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.primary_dark)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back Button",
                        tint = colorResource(R.color.primary_dark)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(R.color.background)
            )
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.LightGray
        )
    }
}



@Composable
private fun MessageList(messages: List<Message>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages) { message ->
                MessageItem(message = message)
            }
        }
    }
}

@Composable
fun ChatInputBar(
    currentMessage: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButtonWithIcon(
            iconRes = R.drawable.ic_emoji,
            contentDescription = "Emoji Panel",
            onClick = { /* Open emoji panel */ }
        )

        MessageTextField(currentMessage = currentMessage, onMessageChange = onMessageChange)

        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButtonWithIcon(
                iconRes = R.drawable.ic_tag,
                contentDescription = "Tag panl",
                size = 18,
                onClick = { /* Open tag panel */ }
            )

            IconButtonWithIcon(
                iconRes = R.drawable.ic_send_message,
                contentDescription = "Send Message",
                size = 18,
                onClick = onSendClick
            )
        }
    }
}

@Composable
private fun MessageTextField(currentMessage: String, onMessageChange: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .height(50.dp),
        value = currentMessage,
        onValueChange = onMessageChange,
        placeholder = {
            Text(
                text = stringResource(R.string.type_message),
                color = colorResource(R.color.gray),
                style = MaterialTheme.typography.titleSmall
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = colorResource(R.color.primary_dark),
            unfocusedTextColor = colorResource(R.color.primary_dark),
            disabledTextColor = colorResource(R.color.primary_dark),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun MessageItem(message: Message) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isSentByUser) Arrangement.End else Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (message.isSentByUser) Color.Blue else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (message.isSentByUser) Color.White else Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun IconButtonWithIcon(
    iconRes: Int,
    contentDescription: String,
    size: Int = 24,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            modifier = Modifier.size(size.dp),
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = colorResource(R.color.primary_dark)
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatDetailScreenPreview_Dark() {
    val mockState: State<ChatState> = remember { mutableStateOf(MockData.mockChatState) }
    val mockNavController = rememberNavController()
    val mockController = object : ChatController {
        override fun onClickChat(navController: NavController, userChat: UserChat) {}
        override fun updateChatMessage(newMessage: String) {}
    }
    val mockUserChat = ChatState().sampleUserChats[0]
    MainMaterialTheme {
        ChatDetailScreen(
            navController = mockNavController,
            controller = mockController,
            userChat = mockUserChat,
            state = mockState
        )
    }
}
@Composable
@Preview(showBackground = true)
private fun ChatDetailScreenPreview_Light() {
    val mockState: State<ChatState> = remember { mutableStateOf(MockData.mockChatState) }
    val mockNavController = rememberNavController()
    val mockController = object : ChatController {
        override fun onClickChat(navController: NavController, userChat: UserChat) {}
        override fun updateChatMessage(newMessage: String) {}
    }
    val mockUserChat = ChatState().sampleUserChats[0]
    MainMaterialTheme {
        ChatDetailScreen(
            navController = mockNavController,
            controller = mockController,
            userChat = mockUserChat,
            state = mockState
        )
    }
}