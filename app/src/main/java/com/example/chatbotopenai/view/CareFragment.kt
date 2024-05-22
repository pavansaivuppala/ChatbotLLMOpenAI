package com.example.chatbotopenai.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbotopenai.view.Adapter.ChatAdapter
import com.example.chatbotopenai.databinding.FragmentCareBinding
import com.example.chatbotopenai.model.OpenAIAPIService
import com.example.chatbotopenai.model.RetrofitBuilder
import com.example.chatbotopenai.model.data.Chat
import com.example.chatbotopenai.model.data.ChatBotInput
import com.example.chatbotopenai.model.data.MessageX
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CareFragment : Fragment() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chats: ArrayList<Chat>
    private lateinit var binding: FragmentCareBinding
    private lateinit var apiService: OpenAIAPIService
    private lateinit var sharedpref:SharedPreferences
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPref()
        initData()
        setUpRecyclerView()
        addMoreChats()
        apiService = RetrofitBuilder.getRetrofit().create(OpenAIAPIService::class.java)
    }

    private fun initPref() {
        sharedpref = requireActivity().getSharedPreferences("Register", Context.MODE_PRIVATE)
    }

    private fun addMoreChats() {
        binding.btnUser2.setOnClickListener {
            val message = binding.inputMessage.text.toString()
            chats.add(Chat(2, message))
            chatAdapter.notifyItemInserted(chats.size)
            initView(message)
            binding.inputMessage.text?.clear()
        }
    }

    private fun initData() {
        chats = ArrayList()
        chats.add(Chat(1, "Hey ${sharedpref.getString("name","Buddy").toString()}, this is Care AI, you're friend in support, if you're feeling low just message me, I'm just a text away! "))
        chatAdapter = ChatAdapter(chats)
    }

    private fun setUpRecyclerView() {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = chatAdapter
    }

    private fun initView(s: String) {
        val messages = ArrayList<MessageX>()
        messages.add(MessageX(role = "system", content = "You are my friend, the name of mine is ${sharedpref.getString("name","Pavan Sai Vuppala").toString()}"))
        messages.add(MessageX(role = "user", content = s))
        val input = ChatBotInput(model = "gpt-3.5-turbo", messages = messages)

        compositeDisposable.add(
            apiService.getCompletion(input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val content = response.choices[0].message.content
                    content?.let {
                        chats.add(Chat(1, it))
                        chatAdapter.notifyItemInserted(chats.size)
                    }
                    Log.e("Success", response.toString())
                }, { error ->
                    Log.e("errorPost", "${error.message}")
                    chats.add(Chat(1, "Server is not available"))
                    chatAdapter.notifyItemInserted(chats.size)
                })
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}
