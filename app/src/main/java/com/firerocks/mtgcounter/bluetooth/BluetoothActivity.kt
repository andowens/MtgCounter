package com.firerocks.mtgcounter.bluetooth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firerocks.mtgcounter.R
import com.firerocks.mtgcounter.helpers.changeNameDialog
import com.firerocks.mtgcounter.root.App
import kotlinx.android.synthetic.main.bluetooth_view.*
import java.util.*
import javax.inject.Inject

class BluetoothActivity: AppCompatActivity(), BluetoothMVP.View {


    @Inject lateinit var mPresenter: BluetoothMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bluetooth_view)

        (application as App).appComponent.inject(this)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mPresenter.setView(this) { name, health ->
            player_health.text = health.toString()
            player_name.text = name
        }
    }

    override fun getDefaultHealth(): Int {
        return resources.getInteger(R.integer.default_player_health)
    }

    override fun getRandomPlayerName(): String {
        val nameList = listOf<String>(getString(R.string.player_four_default_name),
                getString(R.string.player_three_default_name),
                getString(R.string.player_two_default_name),
                getString(R.string.player_one_default_name))

        val nameListSize = nameList.size - 1

        return nameList[Random().nextInt(nameListSize)]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.bluetooth_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        when (item?.itemId) {
            R.id.menu_new_game -> {
                mPresenter.menuNewGame {
                }
                return true
            }
            R.id.menu_bluetooth -> {
                mPresenter.discoverBluetooth()
                return true
            }
        }

        return false
    }

    fun upClicked(view: View) {
        mPresenter.upClicked()
    }

    fun downClicked(view: View) {
        mPresenter.downClicked()
    }

    fun nameClicked(view: View) {

        changeNameDialog(this) { name ->
            mPresenter.nameClicked(name) {
                player_name.text = name
            }
        }
    }
}