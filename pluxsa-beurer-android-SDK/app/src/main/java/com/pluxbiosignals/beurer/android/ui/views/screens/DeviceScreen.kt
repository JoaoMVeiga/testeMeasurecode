package com.pluxbiosignals.beurer.android.ui.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.ui.views.devices.DeviceDefaultView
import com.pluxbiosignals.beurer.android.ui.views.devices.GlucometerView
import com.pluxbiosignals.beurer.android.ui.views.devices.OximeterView
import com.pluxbiosignals.beurer.android.ui.views.devices.ScaleView
import com.pluxbiosignals.beurer.android.viewmodels.GlucometerViewModel
import com.pluxbiosignals.beurer.android.viewmodels.OximeterViewModel
import com.pluxbiosignals.beurer.android.viewmodels.ScaleViewModel
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModel
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModelFactory
import com.pluxbiosignals.beurer.api.States
import com.pluxbiosignals.beurer.api.enums.Devices


@Composable
fun DeviceScreen(
    macAddress: String,
    name: String,
    deviceType: Devices
) {

    val selectedDevice = Device(macAddress, name, deviceType)
    val viewModel: DeviceViewModel = viewModel(
        key = deviceType.toString(),
        factory = DeviceViewModelFactory(selectedDevice, LocalContext.current)
    )

    val connection by remember { viewModel.state }
    val data by remember { viewModel.data }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row() {
                    Text(text = "Name:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = name)
                }

                Row() {
                    Text(text = "MAC Address:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = macAddress)
                }

                Row() {
                    Text(text = "Connection state:")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = connection.name)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { viewModel.connectDevice() }) {
                        Text(text = "Connect")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(onClick = { viewModel.disconnectDevice() }) {
                        Text(text = "Disconnect")
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(15.dp))

        val replyScrollState = rememberScrollState()

        // Scrollable Text Column
        Column(
            modifier = Modifier
                .weight(1f) // Use weight to take remaining height
                .padding(10.dp)
                .verticalScroll(replyScrollState)
        ) {
            Text(text = data)
        }

        if (connection == States.CONNECTED) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                DeviceView(type = deviceType, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DeviceView(type: Devices, viewModel: DeviceViewModel) {

    return when (type) {

        Devices.GLUCOMETER -> GlucometerView(viewModel as GlucometerViewModel)

        Devices.OXIMETER -> OximeterView(viewModel as OximeterViewModel)

        Devices.BPM, Devices.TEMP, Devices.TEMP_BASAL -> DeviceDefaultView(viewModel)

        Devices.SCALE -> ScaleView(viewModel as ScaleViewModel)
        else -> {
            TODO()
        }
    }
}





