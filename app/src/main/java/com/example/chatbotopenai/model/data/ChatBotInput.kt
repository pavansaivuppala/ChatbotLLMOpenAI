package com.example.chatbotopenai.model.data

import com.google.gson.annotations.SerializedName

data class ChatBotInput(
   @SerializedName("messages") val messages: List<MessageX>,
   @SerializedName("model")val model: String
)