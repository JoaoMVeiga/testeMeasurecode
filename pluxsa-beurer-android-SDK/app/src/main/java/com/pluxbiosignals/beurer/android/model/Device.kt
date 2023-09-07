package com.pluxbiosignals.beurer.android.model

import com.pluxbiosignals.beurer.api.enums.Devices

data class Device(
    val macAddress: String,
    val name: String,
    val type: Devices
)