package com.example.topredditviewer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.topredditviewer.R
import com.example.topredditviewer.adapter.PublicationAdapter
import com.example.topredditviewer.databinding.PublicationsTopBinding
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.view_models.AppStatus
import com.example.topredditviewer.view_models.TopPublicationsViewModel
import com.example.topredditviewer.view_models.SharedViewModel

class TopPublications : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: TopPublicationsViewModel by viewModels()
    private val adapter: PublicationAdapter by lazy {
        PublicationAdapter { publication ->
            navigateToSecondFragmentAndSetPublication(publication)
        }
    }

    private var _binding: PublicationsTopBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PublicationsTopBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.before.value != null) {
                        viewModel.getPublications(showPreviousPage = true)
                    }
                }
            }
        )
        setupObservers()
        binding.publicationsRecyclerView.adapter = adapter
        binding.publicationsRecyclerView.setHasFixedSize(true)
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        viewModel.apply {
            status.observe(viewLifecycleOwner) { status ->
                setLoadingImage(status)
            }
            publications.observe(viewLifecycleOwner) { publications ->
                adapter.updateData(newData = publications)
            }
            before.observe(viewLifecycleOwner) {
                binding.previousPublicationTextView.setVisibility(it)
            }
            after.observe(viewLifecycleOwner) {
                binding.nextPublicationTextView.setVisibility(it)
            }
        }
    }
    private fun setupClickListeners() {
        binding.apply {
            previousPublicationTextView.setOnClickListener {
                viewModel.getPublications(showPreviousPage = true)
            }
            nextPublicationTextView.setOnClickListener {
                viewModel.getPublications(showNextPage = true)
            }
        }
    }

    private fun navigateToSecondFragmentAndSetPublication(publication: Publication) {
        sharedViewModel.apply {
            saveSelectedPublication(publication)
            setImageUrl()
        }
        findNavController().navigate(R.id.action_TopPublicationsFragment_to_ThumbnailDetailsFragment)
    }

    private fun View.setVisibility(parameter: String?) {
        if (parameter == null) {
            this.visibility = View.GONE
        } else {
            this.visibility = View.VISIBLE
        }
    }

    private fun setLoadingImage(status: AppStatus) {
        when (status) {
            AppStatus.LOADING -> {
                binding.apply {
                    loadingImageView.visibility = View.VISIBLE
                    loadingImageView.bringToFront()
                    loadingImageView.setImageResource(R.drawable.loading_animation)
                }
            }

            AppStatus.ERROR -> {
                binding.loadingImageView.visibility = View.VISIBLE
                binding.appStatus.visibility = View.VISIBLE
                binding.loadingImageView.setImageResource(R.drawable.connection_error_ic)
            }
            else -> binding.loadingImageView.visibility = View.GONE
        }
    }
}