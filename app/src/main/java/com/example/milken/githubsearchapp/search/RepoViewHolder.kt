package com.example.milken.githubsearchapp.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.Repo
import kotlinx.android.synthetic.main.repo_item_view.view.*

class RepoViewHolder(view: View) : BaseViewHolder(view) {

    override fun configure(baseItem: BaseItem) {
        val repo = baseItem as Repo
        itemView.idTextView.text = repo.id.toString()
        itemView.nameTextView.text = repo.name

        itemView.descTextView.visibility = if (repo.description == null) View.GONE else View.VISIBLE

        repo.description?.let {
            itemView.descTextView.text = it
        }
    }


}