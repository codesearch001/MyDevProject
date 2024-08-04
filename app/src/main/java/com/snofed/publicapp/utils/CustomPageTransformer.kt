package com.snofed.publicapp.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CustomPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageOffset = pageWidth * position

        page.translationX = -pageOffset
        page.alpha = 1 - Math.abs(position)
    }
}