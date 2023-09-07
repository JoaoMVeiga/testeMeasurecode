package com.pluxbiosignals.beurer.android.ui.views.devices

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModel

@Composable
fun DeviceDefaultView(
    viewModel: DeviceViewModel
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(10.dp)
            .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical
            )
    ) {


    }
}