package com.example.milken.githubsearchapp.ui.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.DataType
import com.example.milken.githubsearchapp.ui.search.viewholders.BaseViewHolder
import com.example.milken.githubsearchapp.ui.search.viewholders.RepoViewHolder
import com.example.milken.githubsearchapp.ui.search.viewholders.UserViewHolder
import java.lang.RuntimeException

class SearchAdapter(
    private val context: Context,
    private val listener: (BaseItem) -> Unit
) : RecyclerView.Adapter<BaseViewHolder>() {

    private var itemList: List<BaseItem> = mutableListOf()

    fun setData(items: List<BaseItem>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            REPO_ITEM -> RepoViewHolder(
                LayoutInflater.from(context).inflate(R.layout.repo_item_view, parent, false)
            )
            USER_ITEM -> UserViewHolder(
                LayoutInflater.from(context).inflate(R.layout.user_item_view, parent, false)
            )
            else -> {
                throw RuntimeException("Unknown viewholder type")
            }
        }
    }

    override fun onBindViewHolder(viewHolder: BaseViewHolder, position: Int) {
        viewHolder.configure(itemList[position], listener)
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("myTag", "viewType at $position = ${itemList[position].id}")
        return when (itemList[position].dataType) {
            DataType.REPO -> REPO_ITEM
            DataType.USER -> USER_ITEM
        }
    }

    override fun getItemCount(): Int = itemList.size

    companion object {
        private const val USER_ITEM = 0
        private const val REPO_ITEM = 1
    }
}