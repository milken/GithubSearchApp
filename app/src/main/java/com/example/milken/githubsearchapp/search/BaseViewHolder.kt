package com.example.milken.githubsearchapp.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.milken.githubsearchapp.data.models.BaseItem

abstract class BaseViewHolder(open val view: View) : RecyclerView.ViewHolder(view) {

    abstract fun configure(baseItem: BaseItem)
}