package com.example.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nework.R
import com.example.nework.adapter.UsersListAdapter
import com.example.nework.adapter.UsersListInteractionListener
import com.example.nework.databinding.FragmentPostUsersListBinding
import com.example.nework.ui.ShowPhotoFragment.Companion.textArg
import com.example.nework.viewmodel.EventViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EventUsersListFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostUsersListBinding.inflate(inflater, container, false)

        (activity as AppActivity).supportActionBar?.title = getString(R.string.event_users)

        val viewModel: EventViewModel by activityViewModels()

        val adapter = UsersListAdapter(object : UsersListInteractionListener {
            override fun openUserProfile(id: Int) {
                val idAuthor = id.toString()
                findNavController().navigate(
                    R.id.userProfileFragment,
                    Bundle().apply { textArg = idAuthor })
            }
        })
        binding.list.adapter = adapter

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.loading) {
                Snackbar.make(binding.root, R.string.server_error_message, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.eventUsersData.observe(viewLifecycleOwner) {
            val newUser = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newUser) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        return binding.root
    }
}