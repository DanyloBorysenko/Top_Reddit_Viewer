package com.example.topredditviewer

import android.content.Context
import com.example.topredditviewer.adapter.PublicationAdapter
import com.example.topredditviewer.model.Publication
import org.mockito.Mockito.mock
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    private val data = listOf(
        Publication(
            title = "Title",
            author = null,
            created = null,
            galleryData = null,
            isGallery = null,
            mediaMetaData = null,
            numComments = null,
            preview = null,
            thumbnail = null,
            url = null)
    )
    private val adapter : PublicationAdapter = PublicationAdapter {}
    @Test
    fun adapter_updateData_Ok() {
        adapter.updateData(data)
        val actual = adapter.itemCount
        val expected = data.size
        assertEquals(expected, actual)
    }
}