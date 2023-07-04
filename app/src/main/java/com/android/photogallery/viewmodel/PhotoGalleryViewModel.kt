package com.android.photogallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android.photogallery.data.GalleryItem
import com.android.photogallery.pattern.FlickrFetchr

class PhotoGalleryViewModel : ViewModel() {
    val galleryItemLiveData: LiveData<List<GalleryItem>>

    init {
        galleryItemLiveData = FlickrFetchr().searchPhotos("planets")
    }
}