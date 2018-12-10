package com.example.milken.githubsearchapp.ui.details

import com.example.milken.githubsearchapp.data.models.User

interface DetailsContract {

    interface Presenter {
        fun setView (view: View)
        fun setUser (user: User)
    }

    interface View {
        fun configLoginText(login: String)
        fun configProfileImage(avatarUrl: String)
        fun configFollowersCountText(followersCount: String)
    }
}