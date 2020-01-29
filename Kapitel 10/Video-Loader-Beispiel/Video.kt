package com.dream.keymediation.repository.model

import com.squareup.moshi.FromJson

/**
 * Data class to contain video data
 */
data class Video (
    val thumbnails : String,
    val description : String,
    val title : String,
    val publishedAt : String,
    val channelId : String) {
}

/**
 * The Moshi-Adapter to create a List of Videos from the YoutubeSearc-Result page
 */
class VideoJsonAdapter() {

    @SuppressWarnings("unused")
    @FromJson fun videoFromYoutubeResult(youtubeResult: YoutubeResult) : MutableList<Video> {
        var result : MutableList<Video> = ArrayList()
        for (item in youtubeResult.items) {
            val highThumbnail = item.snippet.thumbnails.get("high")
            val snippet = item.snippet
            result.add(Video(highThumbnail?.url
                    ?: "", snippet.description, snippet.title, snippet.publishedAt, item.id.videoId))
        }
        return result
    }
}


//
// Plain data classes used to parse the Youtube-Result corretly
//
@SuppressWarnings("unused")
class YoutubeResult(val kind: String,
                    val etag: String,
                    val nextPageToken: String,
                    val regionCode: String,
                    val pageInfo: PageInfo,
                    val items:List<Item>)

@SuppressWarnings("unused")
class Item(val kind : String,
           val etag : String,
           val id : Id,
           val snippet: Snippet)

@SuppressWarnings("unused")
class Snippet(val publishedAt: String,
              val channelId: String,
              val title: String,
              val description: String,
              val thumbnails: Map<String, Thumbnail>,
              val channelTitle : String,
              val liveBroadCastContent: String)

class Id(val kind : String, val videoId : String)
class PageInfo(val totalResults : Int, val resultsPerPage : Int)
class Thumbnail(val url: String, val width : Int, val height : Int)


