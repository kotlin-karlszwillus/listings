package com.dream.keymediation.usecases.information.video

import com.dream.keymediation.repository.model.Video
import com.dream.keymediation.repository.remote.api.YoutubeApi
import com.dream.keymediation.repository.remote.api.network
import kotlinx.coroutines.*

class YoutubeInteractor(private val youtubeApi: YoutubeApi = network().youTubeApi()) {

    val coroutineContext = Job() + Dispatchers.IO

    fun getChannelVideos(withData: (ArrayList<Video>) -> Unit) {
        CoroutineScope(coroutineContext).launch {
            val deferredIo = async(Dispatchers.IO) {
                val call = youtubeApi.getChannelVideos().execute()
                call.body()?.toCollection(ArrayList()) ?: ArrayList<Video>()
            }
            withContext(Dispatchers.Main) {
                withData(deferredIo.await())
            }
        }
    }
}
