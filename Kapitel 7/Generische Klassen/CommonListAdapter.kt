package com.dream.keymediation.views

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.dream.keymediation.utils.NetInfo
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.util.*

internal abstract class CommonListAdapter<T>(context: Context, var items: ArrayList<T>, val layoutResource : Int) : ArrayAdapter<T>(context, layoutResource, items) {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): T {
        return items.get(position)
    }

    fun exchangeItems(newItems: ArrayList<T>) {
        items = newItems
        notifyDataSetChanged()
    }

    abstract fun <VH> createViewHolder() : VH

    protected fun <VH> retrieveView(convertView : View?, parentView : ViewGroup) : ViewResult<VH> {
        if (convertView == null) {
            val li = LayoutInflater.from(context)
            val viewHolder = createViewHolder<VH>()
            val view = li.inflate(layoutResource, parentView, false)
            view.tag = viewHolder
            return ViewResult(view, viewHolder)
        }
        else {
            if (convertView.tag is Int) return  ViewResult(convertView, createViewHolder())
            else return ViewResult(convertView, convertView.tag as VH)
        }
    }

    protected fun loadImage(url: String, targetView: ImageView, @DrawableRes placeholderRes : Int) {

        val imageUri = Uri.parse(url)
        val safeUri = if (imageUri.scheme == "http") imageUri.buildUpon().scheme("https").build() else imageUri
        val creator = Picasso.with(context)
                .load(safeUri)
                .placeholder(placeholderRes)
                .error(placeholderRes)
                .fit()
                .centerCrop()

        if (!NetInfo.isOnline(context)) {
            creator.networkPolicy(NetworkPolicy.OFFLINE)
        }
        creator.into(targetView)
    }

    data class ViewResult<out VH>(val view : View, val viewHolder : VH)
}
