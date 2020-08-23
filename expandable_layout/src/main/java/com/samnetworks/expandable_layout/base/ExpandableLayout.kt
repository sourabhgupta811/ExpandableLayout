package com.samnetworks.expandable_layout.base

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.core.animation.addListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.samnetworks.expandable_layout.getMeasurements

class ExpandableLayout : LinearLayout {
    enum class STATE {
        EXPAND,
        COLLAPSE
    }

    private lateinit var expandableView: View
    private lateinit var headerView: View
    @Volatile private var animating:Boolean = false
    @Volatile private var expandableViewHeight: Int = 0
    var expanded: Boolean = true
    private val heightAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            if (::expandableView.isInitialized) {
                expandableView.layoutParams.height =
                    (it.animatedFraction * expandableViewHeight).toInt()
                requestLayout()
            }
        }
    }

    constructor(context: Context) : super(context)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attributeSet, defStyleAttr, defStyleRes)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    fun initView(headerView: View, expandableView: View, initialState: STATE = STATE.COLLAPSE) {
        orientation = VERTICAL
        this.headerView = headerView
        this.expandableView = expandableView
        addView(headerView)
        addView(expandableView)
        setState(initialState)
    }

    private fun setState(state: STATE) {
        when (state) {
            STATE.EXPAND -> expand(0)
            STATE.COLLAPSE -> collapse(0)
        }
    }

    fun <H : ViewDataBinding, E : ViewDataBinding> initView(
        @LayoutRes headerView: Int,
        @LayoutRes expandableView: Int,
        initialState: STATE = STATE.COLLAPSE
    ): Pair<H, E> {
        val inflater = LayoutInflater.from(context)
        val headerBinding = DataBindingUtil.inflate<H>(inflater, headerView, this, false)
        val expandableViewBinding =
            DataBindingUtil.inflate<E>(inflater, expandableView, this, false)
        initView(headerBinding, expandableViewBinding, initialState)
        return Pair(headerBinding, expandableViewBinding)
    }

    fun initView(
        headerViewBinding: ViewDataBinding,
        expandableViewBinding: ViewDataBinding,
        initialState: STATE = STATE.COLLAPSE
    ) {
        return initView(headerViewBinding.root, expandableViewBinding.root, initialState)
    }

    fun expand(duration: Long = 300) {
        animate(true, duration)
    }

    fun collapse(duration: Long = 300) {
        animate(false, duration)
    }

    fun toggle() {
        if (expanded) collapse() else expand()
    }

    private fun animate(expand: Boolean, duration: Long) {
        if(!animating) {
            expandableViewHeight = expandableView.getMeasurements(this).second
            if (duration == 0L) {
                if (expand) {
                    expandableView.layoutParams.height = expandableViewHeight
                } else {
                    expandableView.layoutParams.height = 0
                }
            } else {
                heightAnimator.duration = duration
                heightAnimator.addListener(
                    onStart = { animating = true },
                    onEnd = {
                        this.expanded = expand
                        animating = false
                    })
                if (expand) heightAnimator.start() else heightAnimator.reverse()
            }
        }
    }
}