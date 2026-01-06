package com.livestreaming.tv.ui.fragments.common.languages

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CenterLayoutManager(context: Context) : LinearLayoutManager(context, VERTICAL, false) {

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleChildren()
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val scrolled = super.scrollVerticallyBy(dy, recycler, state)
        scaleChildren()
        return scrolled
    }

    private fun scaleChildren() {
        if (childCount == 0 || height == 0) return

        val midpoint = height / 2f
        val d1 = 0.9f * midpoint

        if (d1 == 0f) return

        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue

            val childMidpoint =
                (getDecoratedTop(child) + getDecoratedBottom(child)) / 2f

            val d = minOf(d1, abs(midpoint - childMidpoint))
            val scale = 1f - 0.4f * (d / d1)

            child.scaleX = scale.coerceIn(0.6f, 1f)
            child.scaleY = scale.coerceIn(0.6f, 1f)

            child.alpha = (0.5f + 0.5f * (1f - d / d1)).coerceIn(0.5f, 1f)
        }
    }
}
