package com.pluxbiosignals.beurer.android.ui.views.screens

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.ui.views.components.DevicesList
import com.pluxbiosignals.beurer.android.viewmodels.ScanViewModel
import com.pluxbiosignals.beurer.android.viewmodels.ScanViewModelFactory
import com.pluxbiosignals.beurer.api.DeviceScan


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    scan: DeviceScan = DeviceScan(),
    bluetoothAdapter: BluetoothAdapter,
    scanViewModel: ScanViewModel = viewModel(factory = ScanViewModelFactory(scan)),
    onDeviceSelected: (Device) -> Unit,
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            scanViewModel.startScan()
        }
    }

    val neededPermissions: MutableList<String> = mutableListOf()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        neededPermissions.add(BLUETOOTH_CONNECT)
        neededPermissions.add(BLUETOOTH_SCAN)
    } else {
        neededPermissions.add(ACCESS_FINE_LOCATION)
    }

    // scan permission state
    val scanPermissions = rememberMultiplePermissionsState(neededPermissions)

    if (scanPermissions.revokedPermissions.isNotEmpty()) {
        PermissionsScreen(scanPermissions)
        return
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row {
            Button(onClick = {
                if (bluetoothAdapter.isEnabled) {
                    scanViewModel.startScan()
                } else {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    launcher.launch(intent)
                }

            }) {
                Text(text = "Scan")
            }
            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = { scanViewModel.stopScan() }) {
                Text(text = "Stop Scan")
            }

            Spacer(modifier = Modifier.width(10.dp))

            val scanning by remember {
                scanViewModel.isScanning
            }
            if (scanning) {
                CircularProgressIndicator(Modifier.padding(2.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        DevicesList(
            list = scanViewModel.devices,
            onDeviceSelected = {
                scanViewModel.stopScan()
                onDeviceSelected(it)
                scanViewModel.clearDevices()
            }
        )
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsScreen(scanPermissions: MultiplePermissionsState) {//TODO not closing if permission denied
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        val textToShow = if (scanPermissions.shouldShowRationale) {
            // If the user has denied the permission but the rationale can be shown,
            // then gently explain why the app requires this permission
            "Some permission is important to enable scan. Please grant the permission."
        } else {
            // If it's the first time the user lands on this feature, or the user
            // doesn't want to be asked again for this permission, explain that the
            // permission is required
            "Some permission is required to scan for devices. " +
                    "Please grant necessary permissions"
        }
        Text(textToShow)
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { scanPermissions.launchMultiplePermissionRequest() }) {
            Text("OK")
        }

    }
}
