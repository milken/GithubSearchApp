package com.example.milken.githubsearchapp.ui.details

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.data.models.SearchDataParcel
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.di.DetailsModule
import com.example.milken.githubsearchapp.ui.MyApp
import com.example.milken.githubsearchapp.ui.search.SearchActivity
import com.example.milken.githubsearchapp.utils.GlideApp
import kotlinx.android.synthetic.main.details_activity.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), DetailsContract.View {

    @Inject
    lateinit var presenter: DetailsContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        initDagger()

        val user: User? = tryGetUserData(savedInstanceState)

        presenter.setView(this)
        presenter.trySetValidUserData(user)
    }

    private fun tryGetUserData(savedInstanceState: Bundle?): User? {
        savedInstanceState?.let {
            it.getParcelable<User>(DetailsActivity.SAVE_INSTANCE_USER_KEY)?.let { return it }
        }

        if (intent.hasExtra(INTENT_USER_KEY)){
            return intent.getParcelableExtra(INTENT_USER_KEY)
        }

        return null
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(SAVE_INSTANCE_USER_KEY, presenter.getData())
    }

    override fun continueViewSetUp() {
        presenter.viewSetUp()
    }

    override fun finishWithError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    override fun configLoginText(login: String) {
        loginTextView.text = login
    }

    override fun configProfileImage(avatarUrl: String) {
        GlideApp
            .with(this)
            .load(avatarUrl)
            .into(profileImageView)
    }

    override fun configFollowersCountText(followersCount: Int) {
        followersCountTextView.text = String.format(resources.getString(R.string.followers_count_text), followersCount)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun initDagger() {
        (application as MyApp)
            .appComponent
            .getDetailsSubComponent(DetailsModule())
            .inject(this)
    }

    companion object {
        private const val INTENT_USER_KEY = "USER"
        private const val SAVE_INSTANCE_USER_KEY = "SAVE_INSTANCE_USER_KEY"

        fun newIntent(context: Context, user: User): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(INTENT_USER_KEY, user)
            return intent
        }
    }
}
