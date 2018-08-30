package com.firerocks.mtgcounter.utils.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firerocks.mtgcounter.R
import kotlinx.android.synthetic.main.device_name.view.*

class DeviceAdapter(private val devices: ArrayList<String>)
    : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device_name, parent, false)
        return DeviceViewHolder(view)
    }

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.iv.device_text.text = devices[position]
        Log.i("TAG", "bound: ${devices[position]}")
    }

    class DeviceViewHolder(val iv: View) : RecyclerView.ViewHolder(iv)
}