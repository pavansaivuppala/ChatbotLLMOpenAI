package com.example.chatbotopenai.model.data

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("content") val content: String,
    @SerializedName("role") val role: String
)