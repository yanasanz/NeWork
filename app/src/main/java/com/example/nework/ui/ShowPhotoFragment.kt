package com.example.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nework.R
import com.example.nework.databinding.FragmentShowPhotoBinding
import com.example.nework.utils.StringArg
import com.example.nework.view.load

class ShowPhotoFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentShowPhotoBinding.inflate(
            inflater,
            container,
            false
        )
        val url = arguments?.textArg
        binding.imageView.load(url)

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}