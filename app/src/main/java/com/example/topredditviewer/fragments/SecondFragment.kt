package com.example.topredditviewer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.topredditviewer.databinding.FragmentSecondBinding
import com.example.topredditviewer.view_models.SharedViewModel

class SecondFragment : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.publicationDetailsImageView.load(sharedViewModel.getPublicationUri())
        binding.downloadButton.setOnClickListener {
            downloadImage(sharedViewModel.url.value.orEmpty())
        }
    }

    private fun downloadImage(imageUrl: String) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}