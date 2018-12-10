package com.example.milken.githubsearchapp.search

import android.view.View
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.User
import kotlinx.android.synthetic.main.user_item_view.view.*

class UserViewHolder(view: View) : BaseViewHolder(view) {

    override fun configure(
        baseItem: BaseItem,
        listener: (BaseItem) -> Unit
    ) {
        val user = baseItem as User
        itemView.idTextView.text = user.id.toString()
        itemView.nameTextView.text = user.login

        itemView.setOnClickListener { listener(user) }

    }
}