package com.livestreaming.tv.ui.fragments.common.userselection

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CenterHorizontalLayoutManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false) {

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleChildren()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
        scaleChildren()
        return scrolled
    }

    private fun scaleChildren() {
        val midpoint = width / 2f
        val d1 = 0.9f * midpoint

        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            val childMidpoint = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2f
            val d = kotlin.math.min(d1, abs(midpoint - childMidpoint))
            val scale = 1f - 0.3f * (d / d1) // Scale from 1.0 to 0.7

            child.scaleX = scale
            child.scaleY = scale
            // Adjust alpha for non-center items
            child.alpha = 0.6f + 0.4f * (1f - d / d1)
        }
    }
}