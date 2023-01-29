package com.example.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nework.R
import com.example.nework.adapter.ChooseUsersAdapter
import com.example.nework.adapter.ChooseUsersInteractionListener
import com.example.nework.databinding.FragmentChoosePostUsersBinding
import com.example.nework.viewmodel.PostViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChoosePostUsersFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentChoosePostUsersBinding.inflate(inflater, container, false)

        val postViewModel: PostViewModel by activityViewModels()

        postViewModel.getUsers()

        val adapter = ChooseUsersAdapter(object : ChooseUsersInteractionListener {
            override fun check(id: Int) {
                postViewModel.check(id)
            }
            override fun unCheck(id: Int) {
                postViewModel.unCheck(id)
            }
        })
        binding.list.adapter = adapter

        postViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.loading){
                Snackbar.make(binding.root, R.string.server_error_message, Snackbar.LENGTH_SHORT).show()
            }
        }

        postViewModel.usersList.observe(viewLifecycleOwner) {
            val newUser = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newUser) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.add.setOnClickListener {
            postViewModel.addMentionIds()
            findNavController().navigateUp()
        }
        return binding.root
    }
}