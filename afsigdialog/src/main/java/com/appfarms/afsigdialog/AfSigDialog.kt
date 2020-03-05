package com.appfarms.afsigdialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import kotlinx.android.synthetic.main.af_dialog.*
import nl.dionsegijn.konfetti.models.Shape.CIRCLE
import nl.dionsegijn.konfetti.models.Shape.RECT
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

    private var tipMax = 10
    private var tipCount = 0
    private var firstTipAt: Long? = null
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

        konfettiView.setOnClickListener {
            if (af_img.drawable is Animatable && !(af_img.drawable as Animatable).isRunning) {
                dismiss()
            }
        }

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
            konfettiView.build()
                    .addColors(
                        ContextCompat.getColor(context, R.color.af_green),
                        ContextCompat.getColor(context, R.color.af_blue)
                    )
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(
                        RECT,
                        CIRCLE
                    )
                    .addSizes(Size(12))
                    .setPosition(-50f, konfettiView.width + 50f, -50f, -50f)
                    .streamFor(300, 5000L)
        }
    }

    /**
     *  This Function sets a new Threshold for how often the user needs to tip on the
     *  [trigger][setTrigger]
     *
     *  default is 10
     *
     * @param count the new Threshold
     * @see [setTipTimeOut]
     * @see [setTrigger]
     */
    fun setTipCount(count: Int): AfSigDialog {
        tipMax = count
        return this
    }

    /**
     *  This Function sets a new Threshold for how long the user has time to tip on the
     *  [trigger][setTrigger]
     *
     *  default is 5 seconds
     *
     * @param time (im MS) the new Threshold
     * @see [setTipCount]
     * @see [setTrigger]
     *
     */
    fun setTipTimeOut(time: Int): AfSigDialog {
        maxTime = time
        return this
    }


    /**
     *  This Function sets a clickListener on the [target] and calls the logic for each tip
     *
     * @param target the view target
     * @see [setTipTimeOut]
     * @see [setTipCount]
     *
     */
    fun setTrigger(target: View): AfSigDialog {
        target.setOnClickListener {
            triggerCalled()
        }
        return this
    }

    /**
     *  This Function sets a clickListener on the [target] and calls the logic for each tip
     *
     * @param target the preference target
     * @see [setTipTimeOut]
     * @see [setTipCount]
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
     *  This Function holds the logic for each tip
     */
    private fun triggerCalled() {
        if (firstTipAt == null) {
            firstTipAt = System.currentTimeMillis()
            tipCount++
        } else {
            if (System.currentTimeMillis().minus(firstTipAt as Long) >= maxTime) {
                reset()
            } else {
                tipCount++
                if (tipCount >= tipMax) {
                    this.show()
                    reset()
                }
            }
        }
    }


    private fun reset() {
        firstTipAt = null
        tipCount = 0
    }
}