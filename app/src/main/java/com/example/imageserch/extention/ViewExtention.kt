package com.example.imageserch.extention

import android.view.View

fun View.fadeAnimation(isVisible: Boolean) {
    var isAnimating = false

    if (isVisible && !isAnimating && this.visibility != View.VISIBLE) {
        this.animate().alpha(1f).setDuration(500).start()
        isAnimating = true
        this.visibility = View.VISIBLE
    } else if (!isVisible && !isAnimating && this.visibility == View.VISIBLE) {
        this.animate().alpha(0f).setDuration(500).withEndAction {
            isAnimating = false
            this.visibility = View.GONE
        }.start()
        isAnimating = true
    }
}