package com.example.topredditviewer.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.network.RedditApi
import kotlinx.coroutines.launch

class FirstFragmentViewModel : ViewModel() {
    private val _publications = MutableLiveData<List<Publication>>()
    val publications : LiveData<List<Publication>>
        get() = _publications

    init {
        getPublications()
    }

    private fun getPublications() {
        viewModelScope.launch {
            val topPublications = mutableListOf<Publication>()
            try {
                val redditData = RedditApi.retrofitService.getTopThumbnails()
                redditData.data.children.forEach{
                    topPublications.add(it.data)
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
            _publications.value = topPublications
        }
    }
}