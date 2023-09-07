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
import com.pluxbiosignals.beurer.android.viewmodels.GlucometerViewModel

@Composable
fun GlucometerView(
    viewModel: GlucometerViewModel
) {

    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
    ) {

        Button(
            onClick = { viewModel.getAllRecords() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.all_records))
        }

        Button(
            onClick = { viewModel.getFirstRecord() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.first_record))
        }

        Button(
            onClick = { viewModel.getLastRecord() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.last_record))
        }

        Button(
            onClick = { viewModel.getLowerThan() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.lower_than))
        }

        Button(
            onClick = { viewModel.getHigherThan() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.higher_than))
        }

        Button(
            onClick = { viewModel.getWithin() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.in_range))
        }

        Button(
            onClick = { viewModel.getTotalRecords() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.total_records))
        }
    }
}