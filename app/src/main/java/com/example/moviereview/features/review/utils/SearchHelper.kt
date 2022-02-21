package com.example.moviereview.features.review.utils

import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup


/**
 * Подручный поиска
 */
object SearchHelper {
    /**
     * Анимиция вызова поиска
     */
     fun stateSearch(show: Boolean, searchImage:View, target : View, root: ViewGroup) {
        if (show) {
            val transition = Slide(Gravity.TOP)
            transition.duration = 300
            transition.addTarget(target)
            TransitionManager.beginDelayedTransition(root, transition)
            searchImage.visibility = View.GONE
            target.visibility = View.VISIBLE
        } else {
            searchImage.visibility = View.VISIBLE
            target.visibility = View.GONE
        }
    }
}