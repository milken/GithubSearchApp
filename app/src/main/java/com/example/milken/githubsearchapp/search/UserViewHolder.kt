package com.example.milken.githubsearchapp.search

import android.view.View
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.User
import kotlinx.android.synthetic.main.user_item_view.view.*

class UserViewHolder(override val view: View) : BaseViewHolder(view) {

    override fun configure(baseItem: BaseItem) {
        val user = baseItem as User
        view.idTextView.text = user.id.toString()
        view.nameTextView.text = user.login
    }
}