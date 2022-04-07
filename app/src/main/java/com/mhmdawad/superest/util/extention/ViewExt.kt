package com.mhmdawad.superest.util.extention

import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*


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

fun ImageView.loadImage(link: String){
    Glide.with(context).load(link).into(this)
}

fun EditText.searchListener(func: ()-> Unit){
    setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            func()
            return@OnEditorActionListener true
        }
        false
    })
}

fun MotionLayout.transitionBottomNavigationBar(transitionChange:(progress: Float)-> Unit, transitionComplete:()-> Unit){
    addTransitionListener(object : MotionLayout.TransitionListener{
        override fun onTransitionStarted(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int
        ) {}

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
            transitionChange(1 -progress)
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            transitionComplete()
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {}
    })
}

fun ImageView.loadGif(gifImage: Int){
    Glide.with(this.context).asGif().load(gifImage).into(this)
}
fun ImageView.loadTimerGif(gifImage: Int){
    Glide.with(this.context).asGif().load(gifImage).listener(object:RequestListener<GifDrawable>{
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<GifDrawable>?,
            isFirstResource: Boolean
        ): Boolean = false

        override fun onResourceReady(
            resource: GifDrawable?,
            model: Any?,
            target: Target<GifDrawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            resource!!.setLoopCount(1)
            resource.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    if(!resource.isRunning)
                        this@loadTimerGif.setImageResource(gifImage)
                }
            })
            return false
        }
    }).into(this)
}