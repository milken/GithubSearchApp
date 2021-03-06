package com.example.milken.githubsearchapp.ui.search

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class MarginItemDecorator(val marginValue: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = marginValue
            }
            left = 0
            right = 0
            bottom = marginValue
        }

    }
}
