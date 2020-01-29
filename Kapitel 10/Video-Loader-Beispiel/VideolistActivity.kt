package com.dream.keymediation.usecases.information.video

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.dream.keymediation.R
import com.dream.keymediation.extensions.addBack
import com.dream.keymediation.repository.remote.api.network
import com.dream.keymediation.views.VideolistAdapter
import kotlinx.android.synthetic.main.activity_itemlist.*

/**
 * Activity to display the results from a Youtube-Channel ID
 */
class VideolistActivity : AppCompatActivity() {

    private lateinit var videolistAdapter: VideolistAdapter
    private val youtubeInteractor = network().youTubeInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itemlist)
        initUi()
    }

    private fun initUi() {
        addBack()
        text_Meditationen.text = resources.getString(R.string.Videos)

        videolistAdapter = VideolistAdapter(this)
        listview.adapter = videolistAdapter
        listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val (_, _, _, _, channelId) = videolistAdapter.getItem(position)
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=$channelId")))
        }
        youtubeInteractor.getChannelVideos { videoList -> videolistAdapter.exchangeItems(videoList) }
    }
}
