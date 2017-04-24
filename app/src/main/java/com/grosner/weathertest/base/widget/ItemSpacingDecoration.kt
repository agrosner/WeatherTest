package com.grosner.weathertest.base.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Description: Simple decoration for equal spacing between all items.
 */
class ItemSpacingDecoration @JvmOverloads constructor(
        private val horizontalSpace: Int,
        private val verticalSpace: Int = horizontalSpace) : RecyclerView.ItemDecoration() {

    var drawVerticalSpaces = true
    var drawHorizontalSpaces = true

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view)
        if (drawVerticalSpaces) {
            outRect.left = verticalSpace
            outRect.right = verticalSpace
        }

        if (drawHorizontalSpaces) {
            outRect.bottom = horizontalSpace
            outRect.top = horizontalSpace
        }
    }
}
