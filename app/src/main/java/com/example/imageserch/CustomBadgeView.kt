package com.example.imageserch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import com.google.android.material.badge.BadgeDrawable

class CustomBadgeView(context: Context): View(context) {
    private var badge: BadgeDrawable? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        badge?.bounds = Rect(0, 0, width, height)

    }

    fun setBadgeDrawable(badgeDrawable: BadgeDrawable) {
        this.badge = badgeDrawable
        invalidate()
    }

}