package com.firerocks.mtgcounter.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firerocks.mtgcounter.R
import kotlinx.android.synthetic.main.device_name.view.*
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(private val devices: ArrayList<String>, private val listener: (String) -> Unit)
    : RecyclerView.ViewHolder<DeviceAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_layout, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position], listener)
    }

    class DeviceViewHolder(val iv: View) : RecyclerView.ViewHolder(iv) {

        fun bind(device: String, listener: (String) -> Unit) = with(iv) {
            device_text.text = device
            setOnClickListener { listener(device) }
        }
    }
}