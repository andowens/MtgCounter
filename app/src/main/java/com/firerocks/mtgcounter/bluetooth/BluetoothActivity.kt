package com.firerocks.mtgcounter.bluetooth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.root.App
import javax.inject.Inject

class BluetoothActivity: AppCompatActivity(), BluetoothMVP.View {

    companion object {
        // Message types sent from the BluetoothChatService Handler
        const val MESSAGE_STATE_CHANGE = 1
        const val MESSAGE_READ = 2
        const val MESSAGE_WRITE = 3
        const val MESSAGE_DEVICE_NAME = 4
        const val MESSAGE_TOAST = 5
        const val TOAST = 6
    }

    @Inject lateinit var mPresenter: BluetoothMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mPresenter.setView(this)
    }

    override fun getDefaultHealth(): Int {
        return resources.getInteger(R.integer.default_player_health)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.bluetooth_menu, menu)
        return true
    }
}