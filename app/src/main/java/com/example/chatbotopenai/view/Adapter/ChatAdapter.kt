package com.example.chatbotopenai.view.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbotopenai.databinding.ReceiverViewBinding
import com.example.chatbotopenai.databinding.SenderViewBinding
import com.example.chatbotopenai.model.data.Chat

class ChatAdapter(private val chats:ArrayList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var bindingSender: SenderViewBinding
    private lateinit var bindingReceiver: ReceiverViewBinding

    override fun getItemCount()= chats.size

    override fun getItemViewType(position: Int): Int {
        return chats[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)

        return if (viewType == SENDER_VIEW){
            bindingSender= SenderViewBinding.inflate(layoutInflater, parent, false)
            SenderViewHolder(bindingSender.root)
        }
        else{
            bindingReceiver= ReceiverViewBinding.inflate(layoutInflater,parent,false)
            ReceiverViewHolder(bindingReceiver.root)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        if (chats[position].viewType== SENDER_VIEW){
            (holder as SenderViewHolder).send(position)

        } else{
            (holder as ReceiverViewHolder).send(position)
        }
    }

    inner class SenderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val txtSender: TextView = bindingSender.txtSender
        fun send(position: Int){
            txtSender.text = chats[position].text
        }
    }

    inner class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val txtReceiver: TextView = bindingReceiver.txtReceiver
        fun send(position: Int){
            txtReceiver.text = chats[position].text
        }
    }

    companion object{
        const val SENDER_VIEW=1
    }
}