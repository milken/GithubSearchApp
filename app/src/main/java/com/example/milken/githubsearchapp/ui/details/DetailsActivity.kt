package com.example.milken.githubsearchapp.ui.details

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.di.DetailsModule
import com.example.milken.githubsearchapp.di.SearchModule
import com.example.milken.githubsearchapp.ui.MyApp
import com.example.milken.githubsearchapp.utils.GlideApp
import kotlinx.android.synthetic.main.details_activity.*
import javax.inject.Inject
import kotlin.math.log

class DetailsActivity : AppCompatActivity(), DetailsContract.View {

    @Inject
    lateinit var presenter: DetailsContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        if (dataInvalid()) {
            finishWithError()
            return
        }

        initDagger()

        presenter.setView(this)

        val user = intent.getParcelableExtra<User>(INTENT_USER_KEY)
        presenter.setUser(user)

        presenter.viewSetUp()
    }

    private fun initDagger() {
        (application as MyApp)
            .appComponent
            .getDetailsSubComponent(DetailsModule())
            .inject(this)
    }

    private fun dataInvalid(): Boolean = !intent.hasExtra(INTENT_USER_KEY)

    private fun finishWithError() {
        Toast.makeText(this, "No data passed", Toast.LENGTH_LONG).show()
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

    companion object {
        private const val INTENT_USER_KEY = "USER"

        fun newIntent(context: Context, user: User): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(INTENT_USER_KEY, user)
            return intent
        }
    }
}
