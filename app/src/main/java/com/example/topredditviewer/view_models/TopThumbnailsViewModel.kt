package com.example.topredditviewer.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topredditviewer.network.RedditApi
import kotlinx.coroutines.launch

class TopThumbnailsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>("Hello Json")
    val text : LiveData<String>
        get() = _text

    init {
        getThumbnails()
    }

    private fun getThumbnails() {
        viewModelScope.launch {
            val json = RedditApi.retrofitService.getTopThumbnails()
            _text.value = json
        }
    }
}