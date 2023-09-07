package com.pluxbiosignals.beurer.android.ui.views

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pluxbiosignals.beurer.android.R
import com.pluxbiosignals.beurer.android.ui.views.screens.DeviceScreen
import com.pluxbiosignals.beurer.android.ui.views.screens.ScanScreen
import com.pluxbiosignals.beurer.api.enums.Devices


/**
 * Enum values that represent the screens in the app
 */
enum class BeurerAppScreen {
    Scan,
    Device
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeurerAppBar(
    currentScreenTitle: Int,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreenTitle)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeurerApp(
    bluetoothAdapter: BluetoothAdapter,
    navController: NavHostController = rememberNavController()
) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreenTitle =
        when (backStackEntry?.destination?.route ?: BeurerAppScreen.Scan.name) {
            BeurerAppScreen.Scan.name -> R.string.scan_devices
            BeurerAppScreen.Device.name -> R.string.device
            else -> R.string.scan_devices
        }

    Scaffold(
        topBar = {
            BeurerAppBar(
                currentScreenTitle = currentScreenTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        }

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BeurerAppScreen.Scan.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // builder parameter will be defined here as the graph

            composable(route = BeurerAppScreen.Scan.name) {
                ScanScreen(onDeviceSelected = { selectedDevice ->

                    navController.navigate(
                        BeurerAppScreen.Device.name +
                                "/${selectedDevice.macAddress}" +
                                "/${selectedDevice.name}" +
                                "/${selectedDevice.type}"
                    )
                }, bluetoothAdapter = bluetoothAdapter)

            }

            composable(
                route = "${BeurerAppScreen.Device.name}/{mac_address}/{device_name}/{device_type}",
                arguments = listOf(
                    navArgument("mac_address") { type = NavType.StringType },
                    navArgument("device_name") { type = NavType.StringType },
                    navArgument("device_type") { type = NavType.EnumType(Devices::class.java) }

                )
            ) { backStackEntry ->

                val selectedDevice = backStackEntry.arguments?.getString("mac_address")
                val name = backStackEntry.arguments?.getString("device_name")
                val deviceType = backStackEntry.arguments?.getSerializable("device_type")
                        as? Devices

                selectedDevice?.let {
                    deviceType?.let {
                        name?.let {
                            DeviceScreen(selectedDevice, name, deviceType)
                        }
                    }
                }
            }

        }
    }

}