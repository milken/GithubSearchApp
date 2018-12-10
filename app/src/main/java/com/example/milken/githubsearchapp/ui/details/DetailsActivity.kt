package com.example.milken.githubsearchapp.ui.details

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.data.models.User

class DetailsActivity : AppCompatActivity() {

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        if (dataInvalid()) {
            finishWithError()
            return
        }

        user = intent.getParcelableExtra(INTENT_USER_KEY)
    }

    private fun dataInvalid(): Boolean = !intent.hasExtra(INTENT_USER_KEY)


    private fun finishWithError() {
        Toast.makeText(this, "No data passed", Toast.LENGTH_LONG).show()
        finish()
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
