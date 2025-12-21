package com.example.oil_forecast.ui.Decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.example.oil_forecast.ui.Adapter.AqiAdapter

class CustomDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val divider: Drawable?

    init {
        val a = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        divider = a.getDrawable(0)
        a.recycle()
    }

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        if (parent.adapter == null || divider == null) {
            return
        }

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            val nextChild = parent.getChildAt(i + 1)

            val adapter = parent.adapter as? AqiAdapter ?: return

            val currentPos = parent.getChildAdapterPosition(child)
            val nextPos = parent.getChildAdapterPosition(nextChild)

            if (currentPos == RecyclerView.NO_POSITION || nextPos == RecyclerView.NO_POSITION) continue

            if (adapter.getItemViewType(currentPos) == AqiAdapter.VIEW_TYPE_ITEM &&
                adapter.getItemViewType(nextPos) == AqiAdapter.VIEW_TYPE_ITEM
            ) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }
}
