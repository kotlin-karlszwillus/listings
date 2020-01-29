package com.dream.keymediation.repository.remote.api

import com.dream.keymediation.repository.model.Video
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("search")
    fun getChannelVideos(@Query("channelId") channelId: String = "<INSERT CHANNEL>",
                         @Query("order") order: String = "date",
                         @Query("part") part: String = "snippet",
                         @Query("key") key: String ="<INSERT KEY>",
                         @Query("type") type: String = "video")
            : Call<MutableList<Video>>
}