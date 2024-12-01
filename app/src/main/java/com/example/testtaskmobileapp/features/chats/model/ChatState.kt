package com.example.testtaskmobileapp.features.chats.model

data class ChatState(
    val isLoading: Boolean = false,
    val sampleUserChats: List<UserChat> = listOf(
        UserChat("John Doe", "Hello! How are you?", "10:15 AM"),
        UserChat("Jane Smith", "See you tomorrow.", "Yesterday"),
        UserChat("Bob Johnson", "Thanks for the update!", "Monday"),
        UserChat("Alice Brown", "Can we meet later?", "10/11/2024")
    ),
    val sampleMessages: List<Message> = listOf(
        Message("Hi there!", isSentByUser = false),
        Message("Hello!", isSentByUser = true),
        Message("How are you?", isSentByUser = false)
    ),
    val newMessage: String = ""
)