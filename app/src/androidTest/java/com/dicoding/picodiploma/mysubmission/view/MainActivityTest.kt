package com.dicoding.picodiploma.mysubmission.view

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.dicoding.picodiploma.mysubmission.R
import com.dicoding.picodiploma.mysubmission.util.EspressoIdlingResource
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun checkItemMovies() {
        onView(withId(R.id.tabs)).perform(click()).check(matches(isDisplayed()))
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))

        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(R.id.rv_list)
        val itemCount = recyclerView.adapter!!.itemCount

        for (i in 0 until itemCount - 1) {
            onView(allOf(isDisplayed(), withId(R.id.rv_list)))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<ListMovieAdapter.ListViewHolder>(
                        i,
                        click()
                    )
                )
            onView(withId(R.id.detail_movie)).check(matches(isDisplayed()))
            onView(withId(R.id.iv_movie_backdrop)).check(matches(isDisplayed()))
            onView(withId(R.id.iv_movie_poster)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_title)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_desc)).check(matches(isDisplayed()))
            onView(withId(R.id.detail_movie)).perform(swipeUp())
            onView(withId(R.id.layout_detail_mov)).perform(click())
            onView(withId(R.id.layout_detail_mov)).perform(click())
            onView(withId(R.id.tv_movie_rating)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_release)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_time)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_genre)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_directors)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_writers)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_studio)).check(matches(isDisplayed()))
            Espresso.pressBack()
        }

        onView(allOf(withText(R.string.tab_title_2), isDescendantOfA(withId(R.id.tabs))))
            .perform(click())
            .check(matches(isDisplayed()))

        for (i in 0 until itemCount - 1) {
            onView(allOf(isDisplayed(), withId(R.id.rv_list)))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<ListMovieAdapter.ListViewHolder>(
                        i,
                        click()
                    )
                )
            onView(withId(R.id.detail_movie)).check(matches(isDisplayed()))
            onView(withId(R.id.iv_movie_backdrop)).check(matches(isDisplayed()))
            onView(withId(R.id.iv_movie_poster)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_title)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_desc)).check(matches(isDisplayed()))
            onView(withId(R.id.detail_movie)).perform(swipeUp())
            onView(withId(R.id.layout_detail_mov)).perform(click())
            onView(withId(R.id.layout_detail_mov)).perform(click())
            onView(withId(R.id.tv_movie_rating)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_release)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_time)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_genre)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_directors)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_writers)).check(matches(isDisplayed()))
            onView(withId(R.id.tv_movie_studio)).check(matches(isDisplayed()))
            Espresso.pressBack()
        }
    }
}