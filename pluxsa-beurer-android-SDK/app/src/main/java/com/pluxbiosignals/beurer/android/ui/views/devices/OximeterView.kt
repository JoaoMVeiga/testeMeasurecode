package com.pluxbiosignals.beurer.android.ui.views.devices

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pluxbiosignals.beurer.android.R
import com.pluxbiosignals.beurer.android.viewmodels.OximeterViewModel

@Composable
fun OximeterView(
    viewModel: OximeterViewModel
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

        Button(
            onClick = { viewModel.getVersion() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.get_version))
        }

        Button(
            onClick = { viewModel.setTimeSynchronization() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.set_time))
        }

        Button(
            onClick = { viewModel.getDeviceTime() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.get_time))
        }

        Button(
            onClick = { viewModel.getDataStorageInformation() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.get_storage_info))
        }

        Button(
            onClick = { viewModel.getData() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.get_data))
        }
    }
}