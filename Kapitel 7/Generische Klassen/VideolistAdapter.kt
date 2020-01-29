package com.dream.keymediation.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dream.keymediation.R
import com.dream.keymediation.repository.model.Video
import com.dream.keymediation.utils.Utility
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


internal class VideolistAdapter(context: Context, allIddescriptions: ArrayList<Video> = ArrayList()) :
        CommonListAdapter<Video>(context,allIddescriptions, R.layout.row_videolist) {

    override fun <VH> createViewHolder() = VideolistViewholder() as VH

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val (view, viewHolder) = retrieveView<VideolistViewholder>(convertView, parent)

        if (convertView == null) {
            viewHolder.text_Meditationen = view.findViewById<View>(R.id.text_Meditationen) as TextView
            viewHolder.row_image = view.findViewById<View>(R.id.row_image) as ImageView
            viewHolder.text_time = view.findViewById<View>(R.id.text_time) as TextView
        }

        ///2016-08-12T12:15:59.000Z
        val (url, _, title, dateadded) = items.elementAt(position)

        viewHolder.text_Meditationen!!.text = title
        loadImage(url, viewHolder.row_image!!, R.drawable.newicon)
        parseAndAddDate(dateadded, viewHolder)

        return view
    }

    private fun parseAndAddDate(dateadded: String, viewHolder: VideolistViewholder) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        try {
            val oldDate = simpleDateFormat.parse(dateadded)
            val timeAgo = Utility.getNewsFeeTimeAgo(oldDate.time)
            viewHolder.text_time!!.text = timeAgo

        } catch (ex: ParseException) {
            Timber.e(ex)
        }
    }

    private inner class VideolistViewholder {
        internal var text_Meditationen: TextView? = null
        internal var row_image: ImageView? = null
        internal var text_time: TextView? = null
    }
}

