package com.example.topredditviewer.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.topredditviewer.model.Publication

class SharedViewModel : ViewModel() {
    private val _selectedPublication = MutableLiveData<Publication>()
    private val _url = MutableLiveData<String>()
    val url : LiveData<String>
        get() = _url
    fun saveSelectedPublication(publication: Publication) {
        _selectedPublication.value = publication
    }
    fun setImageUrl() {
        val imageUrl = if (_selectedPublication.value?.isGallery == true) {
            val galleryItemId: String =
                _selectedPublication.value?.galleryData?.items?.get(0)?.mediaId.orEmpty()
            _selectedPublication.value?.mediaMetaData?.get(galleryItemId)?.s?.u.orEmpty()
        } else {
            _selectedPublication.value?.preview?.images?.get(0)?.source?.url.orEmpty()
        }
        _url.value = imageUrl
    }
}