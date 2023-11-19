package com.example.topredditviewer.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.network.RedditApi
import kotlinx.coroutines.launch

private const val TAG = "PublicationsViewModel"
class PublicationsViewModel : ViewModel() {
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
                Log.d(TAG, "Can not load top Publications")
            }
            _publications.value = topPublications
        }
    }
}