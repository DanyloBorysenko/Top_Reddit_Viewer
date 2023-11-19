package com.example.topredditviewer.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topredditviewer.R
import com.example.topredditviewer.adapter.PublicationAdapter
import com.example.topredditviewer.databinding.FragmentFirstBinding
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.view_models.FirstFragmentViewModel
import com.example.topredditviewer.view_models.SharedViewModel

class FirstFragment : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val viewModel : FirstFragmentViewModel by viewModels()

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
        viewModel.publications.observe(viewLifecycleOwner) { newPublicationsList->
            binding.publicationsRecyclerView.adapter = PublicationAdapter(newPublicationsList) {publication ->
                navigateToSecondFragment(publication)
            }
        }
        binding.publicationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun navigateToSecondFragment(publication : Publication) {
        sharedViewModel.apply {
            saveSelectedPublication(publication)
            setImageUrl()
        }
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}