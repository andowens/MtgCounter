package com.firerocks.mtgcounter.main

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.bluetooth.BluetoothFragment
import com.firerocks.mtgcounter.counter.players.TwoPlayerFragment
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_mtg_counter.*

class MtgCounterActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mtg_counter)

        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (savedInstanceState == null) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            replaceFragment(TwoPlayerFragment.newInstance())
        }

        bottom_nav_bar.setOnNavigationItemSelectedListener { menuItem ->
            var ret = false
            when (menuItem.itemId) {
                R.id.normal_counter -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    replaceFragment(TwoPlayerFragment.newInstance())
                    ret = true
                }
                R.id.bluetooth_menu_item -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    replaceFragment(BluetoothFragment.newInstance())
                    ret = true
                }
                R.id.menu_search -> {
                    ret = true
                }
            }
            return@setOnNavigationItemSelectedListener ret
        }
    }

    /**
     * Helper function that is used to replace the fragment
     */
    private fun replaceFragment(fragment: DaggerFragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commitAllowingStateLoss()
    }
}
