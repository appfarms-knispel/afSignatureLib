package com.appfarms.appfarmslibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appfarms.afsigdialog.AfSigDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AfSigDialog(this).setTipTimeOut(10000).setTrigger(click_me)

    }
}
