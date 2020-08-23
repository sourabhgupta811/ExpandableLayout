package com.samnetworks.expandable_layout

import android.view.View

/**
 * Measures the view and returns the width and height.
 */
fun View.getMeasurements(parent: View): Pair<Int,Int> {
    measure(
        View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED))
    return Pair(measuredWidth,measuredHeight)
}
