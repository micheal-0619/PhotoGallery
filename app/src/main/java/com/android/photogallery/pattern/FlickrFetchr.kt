package com.android.photogallery.pattern

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.photogallery.api.FlickrApi
import com.android.photogallery.api.PhotoInterceptor
import com.android.photogallery.data.GalleryItem
import com.android.photogallery.json.FlickrResponse
import com.android.photogallery.json.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


private const val TAG = "FlickrFetchr"

class FlickrFetchr {
    private val flickrApi: FlickrApi

    init {
        /*添加拦截器*/
        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()
        /*构建Retrofit对象并创建API实例*/
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")//https://api.flickr.com/    https://www.vcg.com/
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): MutableLiveData<List<GalleryItem>> {
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()
        //创建一个Call请求
        val flickrHomePageRequest: Call<FlickrResponse> = flickrApi.fetchPhotos()
        //异步执行网络请求
        flickrHomePageRequest.enqueue(object : Callback<FlickrResponse> {
            override fun onResponse(
                call: Call<FlickrResponse>,
                response: Response<FlickrResponse>
            ) {
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                var galleryItems: List<GalleryItem> = photoResponse?.galleryItems
                    ?: mutableListOf()
                /*
                 * 注意， 并不是所有图片都有对应的url_s链接。 因此，
                 * 以下代码要使用filterNot{...}过滤那些带空url_s值的图片
                 * */
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos ", t)
            }

        })
        return responseLiveData
    }

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }
}