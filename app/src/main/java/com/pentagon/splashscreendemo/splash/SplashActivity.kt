package com.pentagon.splashscreendemo.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.window.SplashScreenView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.os.BuildCompat
import com.pentagon.splashscreendemo.MainActivity
import com.pentagon.splashscreendemo.R
import com.pentagon.splashscreendemo.databinding.ActivitySplashBinding
import java.time.Instant


class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val viewModel: SplashViewModel by viewModels()
    private val jumpRunnable = { goToMainScreen() }

    @RequiresApi(31)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        customizeSplashScreenExit()
        keepSplashScreenLonger()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    private fun goToMainScreenDelayed() {
        handler.postDelayed(jumpRunnable, 1500)
    }

    private fun goToMainScreen() {
        Intent(this, MainActivity::class.java
        ).also { startActivity(it) }
        finish()
    }

    private fun keepSplashScreenLonger() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isDataReady()) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        goToMainScreenDelayed()
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    @RequiresApi(31)
    private fun customizeSplashScreenExit() {
        if (!BuildCompat.isAtLeastS()) {
            return
        }

        splashScreen.setOnExitAnimationListener { splashScreenView ->

            /** Exit immediately **/
//            splashScreenView.remove()

            /**  Standard exit animator**/
//            sleep(1000)
//            splashScreenView.remove()

            /**  Customize exit animator **/
            showSplashExitAnimator(splashScreenView)
//            showSplashIconExitAnimator(splashScreenView)
        }
    }

    /**
      * Show exit animator for splash screen view.
     * */
    @RequiresApi(31)
    private fun showSplashExitAnimator(splashScreenView: SplashScreenView) {
        // Single slide up animator.
//        val slideUp = ObjectAnimator.ofFloat(
//            splashScreenView,
//            View.TRANSLATION_Y,
//            0f,
//            -splashScreenView.height.toFloat()
//        )
//            slideUp.interpolator = AnticipateInterpolator()
//            slideUp.duration = 1000L
//            // Call SplashScreenView.remove at the end of your custom animation.
//            slideUp.doOnEnd {
//                Log.d("Splash", "SplashScreen#onSplashScreenExit onEnd remove")
//                splashScreenView.remove()
//            }
//            // Run your animation.
//            slideUp.start()

        // Create your custom animation set.
        val slideUp = ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_Y,
            0f,
            -splashScreenView.height.toFloat()
        )
        val slideLeft = ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_X,
            0f,
            -splashScreenView.width.toFloat()
        )

        val scaleXOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            1.0f,
            0f
        )

        val path = Path()
        path.moveTo(1.0f, 1.0f)
        path.lineTo(0f, 0f)
        val scaleOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.SCALE_X,
            View.SCALE_Y,
            path
        )

        val alphaOut = ObjectAnimator.ofFloat(
            splashScreenView,
            View.ALPHA,
            1f,
            0f
        )

        val animatorSet = AnimatorSet()
        animatorSet.duration = resources.getInteger(R.integer.splash_exit_total_duration).toLong()
        animatorSet.interpolator = AnticipateInterpolator()

        animatorSet.playTogether(scaleOut)
//        animatorSet.playTogether(slideUp)
//        animatorSet.playTogether(slideUp, scaleXOut)
//        animatorSet.playTogether(slideUp, scaleOut)
//        animatorSet.playTogether(slideUp, slideLeft)
//        animatorSet.playTogether(slideUp, slideLeft, scaleOut)
//        animatorSet.playTogether(slideUp, slideLeft, scaleOut, alphaOut)

        animatorSet.doOnEnd {
            Log.d("Splash", "SplashScreen#remove when animator done")
            // splashScreenView.setBackgroundColor(android.graphics.Color.BLUE)
            splashScreenView.remove()
        }
        animatorSet.start()
    }

    /**
     * Show exit animator for splash icon.
     */
    @RequiresApi(31)
    private fun showSplashIconExitAnimator(splashScreenView: SplashScreenView) {
        val iconView = splashScreenView.iconView ?: return

        val slideUp = ObjectAnimator.ofFloat(
            splashScreenView.iconView,
            View.TRANSLATION_Y,
            0f,
            -iconView.height * 2.toFloat()
//            -iconView.height.toFloat()
        )
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.duration = resources.getInteger(R.integer.splash_exit_icon_duration).toLong()
        // slideUp.duration = getRemainingDuration(splashScreenView)
        // getRemainingDuration(splashScreenView)
        Log.d("Splash", "SplashScreen#showSplashIconExitAnimator() duration:${slideUp.duration}")

        slideUp.doOnEnd {
            Log.d("Splash", "SplashScreen#showSplashIconExitAnimator() onEnd remove")
            splashScreenView.remove()
        }
        slideUp.start()
    }

}