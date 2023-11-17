package com.example.topredditviewer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.topredditviewer.R
import com.example.topredditviewer.databinding.FragmentFirstBinding
import com.example.topredditviewer.view_models.TopThumbnailsViewModel

class FirstFragment : Fragment() {
    private val viewModel : TopThumbnailsViewModel by viewModels()

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.text.observe(viewLifecycleOwner) {
            binding.textview.text = viewModel.text.value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}