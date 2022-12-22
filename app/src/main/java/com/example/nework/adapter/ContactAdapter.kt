package com.example.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nework.databinding.CardContactBinding
import com.example.nework.dto.UserResponse
import com.example.nework.view.loadCircleCrop

interface ContactInteractionListener {
    fun openUserProfile(id: Int)
}

class ContactAdapter(private val onInteractionListener: ContactInteractionListener) :
    ListAdapter<UserResponse, ContactViewHolder>(UserResponseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = CardContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class ContactViewHolder(
    private val binding: CardContactBinding,
    private val listener: ContactInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserResponse) {
        binding.apply {
            avatar.loadCircleCrop(user.avatar)
            avatar.setOnClickListener {
                listener.openUserProfile(user.id)
            }
            author.text = user.name
            author.setOnClickListener {
                listener.openUserProfile(user.id)
            }
        }
    }
}
