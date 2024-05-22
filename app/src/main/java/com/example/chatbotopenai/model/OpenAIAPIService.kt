package com.example.chatbotopenai.model


import com.example.chatbotopenai.model.ConstantChat.END_POINT
import com.example.chatbotopenai.model.data.ChatBotInput
import com.example.chatbotopenai.model.data.ChatBotResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIAPIService {
    @POST(END_POINT)
    fun getCompletion(
        @Body chatBotInput: ChatBotInput
    ): Single<ChatBotResponse>
}