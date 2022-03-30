package com.mhmdawad.superest.util.extention

import android.view.View
import android.view.animation.AnimationUtils

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
fun View.showWithAnimate(anim: Int){
    show()
    animation = AnimationUtils.loadAnimation(context, anim).apply {
        start()
    }
}