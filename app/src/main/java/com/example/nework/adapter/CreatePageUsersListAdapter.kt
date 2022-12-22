package com.example.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nework.databinding.CardUserPreviewBinding
import com.example.nework.dto.UserResponse
import com.example.nework.view.loadCircleCrop

interface CreatePageUsersListInteractionListener {
    fun openUserProfile(id: Int)
    fun deleteFromList(id:Int)
}

class CreatePageUsersListAdapter(private val onInteractionListener: CreatePageUsersListInteractionListener) :
    ListAdapter<UserResponse, CreatePageUsersListViewHolder>(UserResponseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatePageUsersListViewHolder {
        val binding = CardUserPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreatePageUsersListViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: CreatePageUsersListViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class CreatePageUsersListViewHolder(
    private val binding: CardUserPreviewBinding,
    private val listener: CreatePageUsersListInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserResponse) {
        binding.apply {
            avatar.loadCircleCrop(user.avatar)
            this.avatar.setOnClickListener {
                listener.openUserProfile(user.id)
            }
            author.text = user.name
            close.setOnClickListener {
                listener.deleteFromList(user.id)
            }
        }
    }
}