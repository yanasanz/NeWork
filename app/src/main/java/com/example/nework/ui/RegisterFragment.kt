package com.example.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.nework.auth.AppAuth
import com.example.nework.databinding.FragmentRegisterBinding
import com.example.nework.repository.AuthRepository
import com.example.nework.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.navigation.fragment.findNavController
import com.example.nework.R
import com.example.nework.dto.MediaUpload
import com.example.nework.utils.Utils

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth
    @Inject
    lateinit var repository: AuthRepository

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)

        (activity as AppActivity).supportActionBar?.title = getString(R.string.sign_up)

        binding.addAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_addAvatarFragment)
        }

        binding.signUpButton.setOnClickListener {
            val login = binding.login.text.toString()
            val pass = binding.password.text.toString()
            val name = binding.userName.text.toString()

            when {
                binding.login.text.isNullOrBlank() || binding.password.text.isNullOrBlank() -> {
                    Toast.makeText(
                        activity,
                        getString(R.string.error_filling_forms),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                binding.confirmPassword.text.toString() != pass -> {
                    Toast.makeText(
                        activity,
                        getString(R.string.password_doesnt_match),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                viewModel.avatar.value?.file == null -> {
                    viewModel.register(login, pass, name)
                    Utils.hideKeyboard(requireView())
                    findNavController().navigateUp()
                }
                else -> {
                    val file = viewModel.avatar.value?.file?.let { MediaUpload(it) }
                    file?.let { viewModel.registerWithPhoto(login, pass, name, it) }
                    Utils.hideKeyboard(requireView())
                    findNavController().navigateUp()
                }
            }
        }
        return binding.root
    }
}