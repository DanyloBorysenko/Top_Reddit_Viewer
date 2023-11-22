package com.example.topredditviewer.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.model.RedditData
import com.example.topredditviewer.network.LIMIT
import com.example.topredditviewer.network.RedditApi
import kotlinx.coroutines.launch

class FirstFragmentViewModel : ViewModel() {
    private val _after = MutableLiveData<String?>()
    val after : LiveData<String?>
        get() = _after
    private val _before = MutableLiveData<String?>()
    val before : MutableLiveData<String?>
        get() = _before
    private val _publications = MutableLiveData<List<Publication>>()
    val publications : LiveData<List<Publication>>
        get() = _publications
    private var fetchedPublicationCount : Int = 0

    init {
        getPublications()
    }
    fun getPublications(showNextPage : Boolean = false, showPreviousPage : Boolean = false) {
        viewModelScope.launch {
            val redditData : RedditData?
            val topPublications = mutableListOf<Publication>()
            var afterParamFromResponse : String? = null
            var beforeParamFromResponse : String? = null
            try {
                if (showNextPage) {
                    fetchedPublicationCount += LIMIT
                    redditData = RedditApi.retrofitService.loadTopPublication(after = _after.value, count = fetchedPublicationCount)
                } else if (showPreviousPage) {
                    fetchedPublicationCount -= LIMIT
                    redditData = RedditApi.retrofitService.loadTopPublication(before = _before.value, count = fetchedPublicationCount)
                } else {
                    redditData = RedditApi.retrofitService.loadTopPublication()
                    fetchedPublicationCount += LIMIT
                }
                redditData.data.children.forEach{
                    topPublications.add(it.data)
                }
                afterParamFromResponse = redditData.data.after
                beforeParamFromResponse = redditData.data.before
            } catch (e : Exception) {
                e.printStackTrace()
            }
            _publications.value = topPublications
            _before.value = beforeParamFromResponse
            _after.value = afterParamFromResponse
        }
    }
}