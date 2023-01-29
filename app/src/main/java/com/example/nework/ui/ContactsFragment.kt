package com.example.nework.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.nework.R
import com.example.nework.adapter.ContactAdapter
import com.example.nework.adapter.ContactInteractionListener
import com.example.nework.databinding.FragmentContactsBinding
import com.example.nework.ui.ShowPhotoFragment.Companion.textArg
import com.example.nework.viewmodel.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var binding: FragmentContactsBinding? = null

    val userViewModel: UserProfileViewModel by activityViewModels()
    lateinit var adapter: ContactAdapter
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val search = menu.findItem(R.id.menu_search)
        searchView = search?.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    userViewModel.data.observe(viewLifecycleOwner) {
                        val searchText = newText.lowercase(Locale.getDefault())
                        val newArray = it.filter {
                            it.name.lowercase(Locale.getDefault()).contains(searchText)
                        }
                        adapter.submitList(newArray)
                    }
                } else {
                    userViewModel.data.observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }
                }
                return true
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentContactsBinding.inflate(inflater, container, false)

        adapter = ContactAdapter(object : ContactInteractionListener {
            override fun openUserProfile(id: Int) {
                val idAuthor = id.toString()
                findNavController().navigate(
                    R.id.userProfileFragment,
                    Bundle().apply { textArg = idAuthor })
            }
        })
        binding.list.adapter = adapter

        userViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.loading) {
                Snackbar.make(binding.root, R.string.server_error_message, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

        userViewModel.getAllUsers()

        userViewModel.data.observe(viewLifecycleOwner) {
            println(it.toString())
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}