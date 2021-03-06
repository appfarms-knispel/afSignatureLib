package com.appfarms.afsigdialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import kotlinx.android.synthetic.main.af_dialog.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

/**
 *  This class holds all the logic for displaying the appfarms hidden dialog
 *
 *  @param firstLine the first line to be displayed on the dialog
 *  @param secondLine the second line to be displayed on the dialog
 *  @param thirdLine the third line to be displayed on the dialog
 */

class AfSigDialog(
    context: Context,
    private val firstLine: String = "",
    private val secondLine: String = "appfarms GmbH & Co. KG",
    private val thirdLine: String = "Karl-Ferdinand-Braun-Straße 7\nD-28359 Bremen\nGermany"
) : Dialog(context, R.style.AfDialog) {

    private var maxTap = 10
    private var tapCount = 0
    private var firstTapAt: Long? = null
    private var maxTime = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.af_dialog)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        setCancelable(false)

        tv1.text = firstLine

        tv2.text = secondLine

        tv3.text = thirdLine

        setOnDismissListener {
            konfettiView.apply {
                reset()
                clearAnimation()
            }
        }

        setOnShowListener {
            if (af_img.drawable is Animatable) {
                (af_img.drawable as Animatable).start()
            }
            konfettiView?.apply {
                build()
                    .addColors(
                        ContextCompat.getColor(context, R.color.af_green),
                        ContextCompat.getColor(context, R.color.af_blue)
                    )
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(
                        Shape.Square,
                        Shape.Circle
                    )
                    .addSizes(Size(12))
                    .setPosition(-50f, konfettiView.width + 50f, -50f, -50f)
                    .streamFor(300, 5000L)

                setOnClickListener {
                    if (af_img.drawable is Animatable && !(af_img.drawable as Animatable).isRunning) {
                        dismiss()
                    }
                }
            }
        }
    }

    /**
     *  This Function sets a new Threshold for how often the user needs to tap on the
     *  [trigger][setTrigger]
     *
     *  default is 10
     *
     * @param count the new Threshold
     * @see [setTapTimeOut]
     * @see [setTrigger]
     */
    fun setTapCount(count: Int): AfSigDialog {
        maxTap = count
        return this
    }

    /**
     *  This Function sets a new Threshold for how long the user has time to tap on the
     *  [trigger][setTrigger]
     *
     *  default is 5 seconds
     *
     * @param time (im MS) the new Threshold
     * @see [setTapCount]
     * @see [setTrigger]
     *
     */
    fun setTapTimeOut(time: Int): AfSigDialog {
        maxTime = time
        return this
    }

    /**
     *  This Function sets a clickListener on the [target] and calls the logic for each tap
     *
     * @param target the view target
     * @see [setTapTimeOut]
     * @see [setTapCount]
     *
     */
    fun setTrigger(target: View): AfSigDialog {
        target.setOnClickListener {
            triggerCalled()
        }
        return this
    }

    /**
     *  This Function sets a clickListener on the [target] and calls the logic for each tap
     *
     * @param target the preference target
     * @see [setTapTimeOut]
     * @see [setTapCount]
     *
     */
    fun setTrigger(target: Preference): AfSigDialog {
        target.setOnPreferenceClickListener {
            triggerCalled()
            true
        }
        return this
    }

    /**
     *  This Function holds the logic for each tap
     */
    private fun triggerCalled() {
        if (firstTapAt == null) {
            firstTapAt = System.currentTimeMillis()
            tapCount++
        } else {
            if (System.currentTimeMillis().minus(firstTapAt as Long) >= maxTime) {
                reset()
            } else {
                tapCount++
                if (tapCount >= maxTap) {
                    this.show()
                    reset()
                }
            }
        }
    }

    private fun reset() {
        firstTapAt = null
        tapCount = 0
    }
}