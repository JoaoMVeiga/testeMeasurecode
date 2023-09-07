package com.pluxbiosignals.beurer.android.ui.views.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluxbiosignals.beurer.android.model.Device

@Composable
fun DevicesList(
    list: List<Device>,
    onDeviceSelected: (Device) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(list) { device ->
            DeviceRow(
                device = device,
                onClick = onDeviceSelected
            )
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceRow(device: Device, onClick: (Device) -> Unit) {

    Card() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable {
                    onClick.invoke(device)
                }
        ) {

            Row(Modifier.padding()) {
                Text(text = "Nome:")
                Text(text = device.name)
            }
            Row(Modifier.padding()) {
                Text(text = "MAC Address:")
                Text(text = device.macAddress)
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DeviceRowPreview() {

    Card() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {

            Row(Modifier.padding()) {
                Text(text = "Nome:")
                Text(text = "unknown")
            }
            Row(Modifier.padding()) {
                Text(text = "MAC Address:")
                Text(text = "device.macAddress")
            }
        }
    }
}