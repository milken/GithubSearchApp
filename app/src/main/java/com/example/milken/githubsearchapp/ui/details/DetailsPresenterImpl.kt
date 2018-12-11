package com.example.milken.githubsearchapp.ui.details

import android.annotation.SuppressLint
import android.util.Log
import com.example.milken.githubsearchapp.data.apis.GithubUserDetailsApi
import com.example.milken.githubsearchapp.data.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsPresenterImpl(
    val githubUserDetailsApi: GithubUserDetailsApi
) :
    DetailsContract.Presenter {

    private lateinit var view: DetailsContract.View
    private lateinit var user: User

    private var requestDisposable: Disposable? = null

    override fun setView(view: DetailsContract.View) {
        this.view = view
    }

    override fun setUser(user: User) {
        this.user = user
    }

    override fun viewSetUp() {
        updateUI()
        if (!user.hasDetails) {
            fetchUserDetails()
        }
    }


    @SuppressLint("CheckResult")
    private fun fetchUserDetails() {
        requestDisposable = githubUserDetailsApi.getUserDetails(user.login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                user = it
                updateUI()
            }, { err -> Log.d("myTag","err = ${err.localizedMessage}") })
    }

    private fun updateUI() {
        view.configLoginText(user.login)
        view.configProfileImage(user.avatarUrl)
        user.followersCount?.let {
            view.configFollowersCountText(it)
        }
    }

    override fun viewDestroyed() {
        requestDisposable?.dispose()
    }
}