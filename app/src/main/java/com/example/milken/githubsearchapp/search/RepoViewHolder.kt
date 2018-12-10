package com.example.milken.githubsearchapp.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.Repo
import kotlinx.android.synthetic.main.repo_item_view.view.*

class RepoViewHolder(override val view: View) : BaseViewHolder(view) {

    override fun configure(baseItem: BaseItem) {
        val repo = baseItem as Repo
        view.idTextView.text = repo.id.toString()
        view.nameTextView.text = repo.name

        view.descTextView.visibility = if (repo.description == null) View.GONE else View.VISIBLE

        repo.description?.let {
            view.descTextView.text = it
        }
    }


}