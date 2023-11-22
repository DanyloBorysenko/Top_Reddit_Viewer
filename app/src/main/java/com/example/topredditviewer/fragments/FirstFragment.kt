package com.example.topredditviewer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.topredditviewer.R
import com.example.topredditviewer.adapter.PublicationAdapter
import com.example.topredditviewer.databinding.FragmentFirstBinding
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.view_models.AppStatus
import com.example.topredditviewer.view_models.FirstFragmentViewModel
import com.example.topredditviewer.view_models.SharedViewModel

class FirstFragment : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val viewModel : FirstFragmentViewModel by viewModels()
    private val adapter: PublicationAdapter by lazy {
        PublicationAdapter {
                publication -> navigateToSecondFragmentAndSetPublication(publication)
        }
    }

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
        viewModel.status.observe(viewLifecycleOwner) {status ->
            setLoadingImage(status)
        }
        binding.publicationsRecyclerView.adapter = adapter
        viewModel.publications.observe(viewLifecycleOwner) {publications->
            adapter.updateData(newData = publications)
        }
        viewModel.before.observe(viewLifecycleOwner) {
            binding.previousPublicationTextView.setVisibility(it)
        }
        viewModel.after.observe(viewLifecycleOwner) {
            binding.nextPublicationTextView.setVisibility(it)
        }
        binding.previousPublicationTextView.setOnClickListener {
            viewModel.getPublications(showPreviousPage = true)
        }
        binding.nextPublicationTextView.setOnClickListener {
            viewModel.getPublications(showNextPage = true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun navigateToSecondFragmentAndSetPublication(publication : Publication) {
        sharedViewModel.apply {
            saveSelectedPublication(publication)
            setImageUrl()
        }
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    private fun View.setVisibility(parameter : String?) {
        if (parameter == null) {
            this.visibility = View.GONE
        } else {
            this.visibility = View.VISIBLE
        }
    }
    private fun setLoadingImage(status: AppStatus) {
        when(status) {
            AppStatus.LOADING -> {
                binding.loadingImageView.visibility = View.VISIBLE
                binding.loadingImageView.bringToFront()
                binding.loadingImageView.setImageResource(R.drawable.loading_animation)}
            AppStatus.ERROR -> {
                binding.loadingImageView.visibility = View.VISIBLE
                binding.loadingImageView.setImageResource(R.drawable.broken_image_ic)
            }
            else -> binding.loadingImageView.visibility = View.GONE
        }
    }
}