package com.android.photogallery.api

import com.android.photogallery.json.FlickrResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {
    /*
    * 常见的HTTP请求类型有@GET、 @POST、 @PUT、 @DELETE和@HEAD。
    *
    * @GET("/")注解的作用是把fetchContents()函数返回的Call配置成一个GET请求。 字符
    * 串"/"表示一个相对路径URL——针对Flickr API端点基URL来说的相对路径。 大多数HTTP请求方法注解包
    * 括相对路径。 这里， "/" 相对路径是指请求会发往你稍后就会提供的基URL。
    * */

//    @GET("/")
//    fun fetchContents(): Call<String>

    /*
    * key：2aa0eb4afa55e900c12deb7af82a1878
    * 密鑰：5ca57ed4d1351288
    *
    * */
    @GET("services/rest?method=flickr.interestingness.getList")
    fun fetchPhotos(): Call<FlickrResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    //搜索
    @GET("services/rest?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>
}