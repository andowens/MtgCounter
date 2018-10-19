package com.firerocks.mtgcounter.data

import java.util.*

class BTDevice (var deviceName: String, var deviceAddress: String, var paired: Boolean) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as BTDevice
        if (other.deviceAddress != deviceAddress) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {

        return Arrays.hashCode(deviceAddress.toByteArray())
    }
}