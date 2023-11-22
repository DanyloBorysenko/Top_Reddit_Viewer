package com.example.topredditviewer.fragments

import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.topredditviewer.databinding.FragmentSecondBinding
import com.example.topredditviewer.view_models.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.URL

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
        sharedViewModel.url.observe(viewLifecycleOwner) {url ->
            binding.publicationDetailsImageView.load(url)
        }
        binding.downloadButton.setOnClickListener {
            downloadImage()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun downloadImage() {
        CoroutineScope(Dispatchers.Default).launch {
            val byteArray = URL(sharedViewModel.url.value).readBytes()
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            }
            val resolver = requireContext().contentResolver
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let { uri ->
                var outputStream: OutputStream? = null
                try {
                    outputStream = resolver.openOutputStream(uri)
                    outputStream?.write(byteArray)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Image downloaded", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    outputStream?.close()

                }
            }
        }
    }
}