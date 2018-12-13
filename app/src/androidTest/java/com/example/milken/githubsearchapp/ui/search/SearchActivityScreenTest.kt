package com.example.milken.githubsearchapp.ui.search

import android.support.test.runner.AndroidJUnit4
import android.support.test.runner.AndroidJUnitRunner
import org.junit.runner.RunWith
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import com.example.milken.githubsearchapp.R
import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.models.Repo
import com.example.milken.githubsearchapp.data.models.ReposResponse
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.data.models.UsersResponse
import com.example.milken.githubsearchapp.ui.MyApp
import io.reactivex.Observable

import org.junit.Rule
import org.junit.Test
import android.support.test.espresso.UiController
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.ViewAction
import android.view.View
import com.example.milken.githubsearchapp.di.*
import com.example.milken.githubsearchapp.ui.search.RecyclerViewMatcher.Companion.withRecyclerView
import it.cosenonjaviste.daggermock.DaggerMock
import org.mockito.Mock
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class SearchActivityScreenTest {

    @JvmField
    @Rule
    val main: ActivityTestRule<SearchActivity> =
        ActivityTestRule(SearchActivity::class.java, false, false)

    @JvmField
    @Rule
    val dagger = DaggerMock.rule<AppComponent>(NetModule()){
       set {
           val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as MyApp
           app.appComponent = it
       }
    }

    @Mock
    private lateinit var githubSearchApi: GithubSearchApi

    private val userId1 = User(id = 1, login = "Adam", avatarUrl = "avatar_url", detailsUrl = "url")
    private val userId10 = User(id = 10, login = "Alicja", avatarUrl = "avatar_url", detailsUrl = "url")
    private val repoId2 = Repo(id = 2, name = "RxKotlin", description = "Rx version of kotlin")
    private val repoId3 = Repo(id = 3, name = "Testable", description = null)

    @Test
    fun testGetSearches_showsListInCorrectOrder() {
        val userList = listOf(userId1, userId10)
        val repoList = listOf(repoId2, repoId3)

        val resultList = listOf(userId1, repoId2, repoId3, userId10)

        val userResponse = UsersResponse(userList)
        val reposResponse = ReposResponse(repoList)

        Mockito.`when`(githubSearchApi.getUserList("a")).thenReturn(Observable.just(userResponse))
        Mockito.`when`(githubSearchApi.getRepoList("a")).thenReturn(Observable.just(reposResponse))

        main.launchActivity(null)

        onView(withId(R.id.searchEditText)).perform(click()).perform(typeText("a")).perform(typeText("a"))

        for((index, value) in resultList.iterator().withIndex()) {
            onView(withRecyclerView(R.id.recyclerView).atPosition(index))
                .check(matches(hasDescendant(withText(value.id.toString()))))
        }
    }

    @Test
    fun testClickUser_showsDetailsActivity() {
        val userList = listOf(userId1)

        val userResponse = UsersResponse(userList)
        val reposResponse = ReposResponse(emptyList())

        Mockito.`when`(githubSearchApi.getUserList("a")).thenReturn(Observable.just(userResponse))
        Mockito.`when`(githubSearchApi.getRepoList("a")).thenReturn(Observable.just(reposResponse))

        main.launchActivity(null)

        onView(withId(R.id.searchEditText)).perform(click()).perform(typeText("a")).perform(typeText("a"))

        onView(withRecyclerView(R.id.recyclerView).atPosition(0)).perform(click())

        onView(withText(userId1.login)).check(matches(isDisplayed()))
    }

}

