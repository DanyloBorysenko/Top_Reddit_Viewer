package com.example.topredditviewer.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topredditviewer.model.Publication
import com.example.topredditviewer.network.RedditApi
import kotlinx.coroutines.launch

private const val TAG = "FirstFragmentViewModel"
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

    init {
        getPublications()
    }
    fun getPublications(useAfterParameter : Boolean = false, useBeforeParameter : Boolean = false) {
        viewModelScope.launch {
            val topPublications = mutableListOf<Publication>()
            var afterParamFromResponse : String? = null
            var beforeParamFromResponse : String? = null
            try {

                val redditData = if (useAfterParameter) {
                    RedditApi.retrofitService.loadTopPublication(after = _after.value, count = 10)
                } else if (useBeforeParameter) {
                    RedditApi.retrofitService.loadTopPublication(before = _before.value, count = 10)
                } else {
                    RedditApi.retrofitService.loadTopPublication()
                }
                redditData.data.children.forEach{
                    topPublications.add(it.data)
                }
                afterParamFromResponse = redditData.data.after
                beforeParamFromResponse = redditData.data.before
            } catch (e : Exception) {
                Log.d(TAG, "After parameter is ${after.value}, Before parameter is ${before.value}")
                e.printStackTrace()
            }
            _publications.value = topPublications
            _before.value = beforeParamFromResponse
            _after.value = afterParamFromResponse
        }
    }

//    fun getPublications(useAfterParameter : Boolean = false, useBeforeParameter : Boolean = false) {
//        viewModelScope.launch {
//            val topPublications = mutableListOf<Publication>()
//            var afterParamFromResponse : String? = null
//            var beforeParamFromResponse : String? = null
//            try {
//
//                val redditData = if (useAfterParameter) {
//                    RedditApi.retrofitService.loadTopPublication(after = _after.value, count = 10)
//                } else if (useBeforeParameter) {
//                    RedditApi.retrofitService.loadTopPublication(before = _before.value, count = 10)
//                } else {
//                    RedditApi.retrofitService.loadTopPublication()
//                }
//                redditData.data.children.forEach{
//                    topPublications.add(it.data)
//                }
//                afterParamFromResponse = redditData.data.after
//                beforeParamFromResponse = redditData.data.before
//            } catch (e : Exception) {
//                Log.d(TAG, "After parameter is ${after.value}, Before parameter is ${before.value}")
//                e.printStackTrace()
//            }
//            _publications.value = topPublications
//            _before.value = beforeParamFromResponse
//            _after.value = afterParamFromResponse
//        }
//    }
}