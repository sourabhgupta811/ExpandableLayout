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
    enum class State {
        EXPAND,
        COLLAPSE
    }
    lateinit var currentState:State
    private lateinit var expandableView: View
    private lateinit var headerView: View
    @Volatile private var animating:Boolean = false
    @Volatile private var expandableViewHeight: Int = 0
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

    fun initView(headerView: View, expandableView: View, initialState: State = State.COLLAPSE) {
        orientation = VERTICAL
        this.headerView = headerView
        this.expandableView = expandableView
        addView(headerView)
        addView(expandableView)
        setState(initialState)
    }

    private fun setState(state: State) {
        when (state) {
            State.EXPAND -> expand(0)
            State.COLLAPSE -> collapse(0)
        }
    }

    fun <H : ViewDataBinding, E : ViewDataBinding> initView(
        @LayoutRes headerView: Int,
        @LayoutRes expandableView: Int,
        initialState: State = State.COLLAPSE
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
        initialState: State = State.COLLAPSE
    ) {
        return initView(headerViewBinding.root, expandableViewBinding.root, initialState)
    }

    fun expand(duration: Long = 300) {
        animate(State.EXPAND, duration)
    }

    fun collapse(duration: Long = 300) {
        animate(State.COLLAPSE, duration)
    }

    fun toggle():Boolean {
        return if (currentState==State.EXPAND){
            collapse()
            false
        }else {
            expand()
            true
        }
    }

    private fun animate(state: State, duration: Long) {
        if(!animating) {
            expandableViewHeight = expandableView.getMeasurements(this).second
            if (duration == 0L) {
                if (state==State.EXPAND) {
                    expandableView.layoutParams.height = expandableViewHeight
                } else {
                    expandableView.layoutParams.height = 0
                }
                requestLayout()
            } else {
                heightAnimator.duration = duration
                heightAnimator.addListener(
                    onStart = { animating = true },
                    onEnd = {
                        this.currentState = state
                        animating = false
                    })
                if (state==State.EXPAND) heightAnimator.start() else heightAnimator.reverse()
            }
        }
    }
}