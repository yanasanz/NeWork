package com.example.nework.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nework.R
import com.example.nework.adapter.*
import com.example.nework.databinding.FragmentUserProfileBinding
import com.example.nework.dto.Job
import com.example.nework.dto.PostResponse
import com.example.nework.enumeration.AttachmentType
import com.example.nework.ui.PostFeedFragment.Companion.intArg
import com.example.nework.utils.StringArg
import com.example.nework.view.loadCircleCrop
import com.example.nework.viewmodel.AuthViewModel
import com.example.nework.viewmodel.PostViewModel
import com.example.nework.viewmodel.UserProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    val userProfileViewModel: UserProfileViewModel by activityViewModels()
    val authViewModel: AuthViewModel by activityViewModels()
    val postViewModel: PostViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!authViewModel.authenticated && arguments == null)
            findNavController().navigate(R.id.signInFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserProfileBinding.inflate(inflater, container, false)

        authViewModel.data.observeForever {
            if (!authViewModel.authenticated || arguments != null) {
                binding.addJob.visibility = View.GONE
                binding.addPost.visibility = View.GONE
                arguments?.textArg?.let {
                    val userId = it.toInt()
                    userProfileViewModel.getUserById(userId)
                    userProfileViewModel.getUserJobs(userId)
                    userProfileViewModel.getUserPosts(userId)
                }
            } else if (authViewModel.authenticated && arguments == null) {
                binding.addJob.visibility = View.VISIBLE
                binding.addPost.visibility = View.VISIBLE
                val myId = userProfileViewModel.myId.toInt()
                userProfileViewModel.getUserById(myId)
                userProfileViewModel.getMyJobs()
                userProfileViewModel.getUserPosts(myId)

            }
        }

        val jobAdapter = JobAdapter(object : JobInteractionListener {
            override fun onLinkClick(url: String) {
                CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
                    .launchUrl(requireContext(), Uri.parse(url))
            }

            override fun onRemoveJob(job: Job) {
                userProfileViewModel.removeJobById(job.id)
            }
        })

        binding.jobList.adapter = jobAdapter

        userProfileViewModel.jobData.observe(viewLifecycleOwner) {
            if (authViewModel.authenticated && arguments == null) {
                it.forEach { job ->
                    job.ownedByMe = true
                }
            }
            if (it.isEmpty()) {
                binding.jobList.visibility = View.GONE
            } else {
                jobAdapter.submitList(it)
                binding.jobList.visibility = View.VISIBLE
            }
        }

        userProfileViewModel.userData.observe(viewLifecycleOwner) {
            (activity as AppActivity?)?.supportActionBar?.title = it.name
            binding.name.text = it.name
            binding.avatar.loadCircleCrop(it.avatar)
        }

        val postAdapter = PostAdapter(object : PostInteractionListener {
            override fun onLike(post: PostResponse) {
                if (authViewModel.authenticated) {
                    if (!post.likedByMe) postViewModel.likePostById(post.id) else postViewModel.dislikePostById(post.id)
                } else {
                    Snackbar.make(binding.root, R.string.log_in_to_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.signInFragment)
                }
            }

            override fun onEdit(post: PostResponse) {
                findNavController().navigate(
                    R.id.newPostFragment,
                    Bundle().apply { intArg = post.id })
            }

            override fun onRemove(post: PostResponse) {
                postViewModel.removePostById(post.id)
            }

            override fun onShare(post: PostResponse) {
                if (authViewModel.authenticated) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.share_description))
                    startActivity(shareIntent)
                } else {
                    Snackbar.make(binding.root, R.string.log_in_to_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.signInFragment)
                }
            }

            override fun loadLikedAndMentionedUsersList(post: PostResponse) {
                if (authViewModel.authenticated) {
                    if (post.users.values.isEmpty()) {
                        return
                    } else {
                        postViewModel.getLikedAndMentionedUsersList(post)
                        findNavController().navigate(R.id.postUsersListFragment)
                    }
                } else {
                    Snackbar.make(binding.root, R.string.log_in_to_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.signInFragment)
                }
            }

            override fun onShowPhoto(post: PostResponse) {
                if (post.attachment?.url != "") {
                    when (post.attachment?.type) {
                        AttachmentType.IMAGE -> {
                            findNavController().navigate(R.id.showPhotoFragment,
                                Bundle().apply { textArg = post.attachment.url })
                        }
                        else -> return
                    }
                }
            }
        })

        binding.postList.adapter = postAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
                    postAdapter.retry()
                }
            }),
            footer = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
                    postAdapter.retry()
                }
            }),
        )

        lifecycleScope.launchWhenCreated {
            println(userProfileViewModel.postData.toString())
            userProfileViewModel.postData.collectLatest(postAdapter::submitData)
        }

        binding.addJob.setOnClickListener {
            findNavController().navigate(R.id.newJobFragment)
        }

        binding.addPost.setOnClickListener {
            findNavController().navigate(R.id.newPostFragment)
        }

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}