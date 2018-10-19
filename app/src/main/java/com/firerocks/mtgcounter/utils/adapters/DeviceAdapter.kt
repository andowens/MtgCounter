package com.firerocks.mtgcounter.utils.adapters

import android.animation.AnimatorInflater
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import com.firerocks.mtgcounter.R
import androidx.recyclerview.widget.RecyclerView
import com.firerocks.mtgcounter.data.BTDevice
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.device_layout.view.*

class DeviceAdapter(private val devices: ArrayList<BluetoothDevice>, private val listener: (BluetoothDevice) -> Unit)
    : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    private var mLastPosition = -1
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_layout, parent, false)
        mContext = parent.context
        return DeviceViewHolder(view)
    }

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position], listener)

        setAnimation(holder.iv, position)
    }

    private fun setAnimation(iv: View, position: Int) {
        if (position > mLastPosition) {
            val animation = AnimatorInflater.loadAnimator(mContext, R.animator.up_from_bottom)

            animation.setTarget(iv)
            animation.start()
            mLastPosition = position
        }
    }

    class DeviceViewHolder(val iv: View) : RecyclerView.ViewHolder(iv) {

        fun bind(device: BluetoothDevice, listener: (BluetoothDevice) -> Unit) = with(iv) {
            val card : CardView = iv as CardView
            device_name.text = device.name
            device_address.text = device.address
            card.setOnClickListener { listener(device) }

        }
    }
}