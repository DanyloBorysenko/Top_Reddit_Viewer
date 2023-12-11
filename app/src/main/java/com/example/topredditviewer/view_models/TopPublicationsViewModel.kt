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

enum class AppStatus {
    LOADING, DONE, ERROR
}

class TopPublicationsViewModel : ViewModel() {
    private val _after = MutableLiveData<String?>()
    val after: LiveData<String?>
        get() = _after
    private val _before = MutableLiveData<String?>()
    val before: MutableLiveData<String?>
        get() = _before
    private val _publications = MutableLiveData<List<Publication>>()
    val publications: LiveData<List<Publication>>
        get() = _publications
    private var fetchedPublicationCount: Int = 0
    private val _status = MutableLiveData<AppStatus>()
    val status: LiveData<AppStatus>
        get() = _status

    init {
        getPublications()
    }
    private fun updateParameters(redditData: RedditData, topPublications: MutableList<Publication>) {
        _after.value = redditData.data.after
        _before.value = redditData.data.before
        _publications.value = topPublications
        _status.value = AppStatus.DONE
    }
    private suspend fun initialLoading(): RedditData {
        fetchedPublicationCount += LIMIT
        return RedditApi.retrofitApi.loadTopPublication()
    }

    fun getPublications(showNextPage: Boolean = false, showPreviousPage: Boolean = false) {
        viewModelScope.launch {
            _status.value = AppStatus.LOADING
            val redditData: RedditData?
            val topPublications = mutableListOf<Publication>()
            try {
                if (showNextPage) {
                    fetchedPublicationCount += LIMIT
                    redditData = RedditApi.retrofitApi.loadTopPublication(
                        after = _after.value,
                        count = fetchedPublicationCount
                    )
                } else if (showPreviousPage) {
                    fetchedPublicationCount -= LIMIT
                    redditData = RedditApi.retrofitApi.loadTopPublication(
                        before = _before.value,
                        count = fetchedPublicationCount
                    )
                } else {
                    redditData = initialLoading()
                }
                redditData.data.children.forEach {
                    topPublications.add(it.data)
                }
                updateParameters(redditData, topPublications)
            } catch (e: Exception) {
                _status.value = AppStatus.ERROR
                e.printStackTrace()
            }
        }
    }
}