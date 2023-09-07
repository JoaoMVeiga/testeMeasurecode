/*
 *
 * Copyright (c) PLUX S.A., All Rights Reserved.
 * (www.plux.info)
 *
 * This software is the proprietary information of PLUX S.A.
 * Use is subject to license terms.
 *
 */
package com.pluxbiosignals.beurer.android.ui.views.devices

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pluxbiosignals.beurer.android.R
import com.pluxbiosignals.beurer.android.viewmodels.ScaleViewModel
import com.pluxbiosignals.beurer.api.enums.scale.Gender
import com.pluxbiosignals.beurer.api.objects.ScaleUser

@Composable
fun ScaleView(
    viewModel: ScaleViewModel
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
            onClick = { viewModel.setTime() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.set_time))
        }

        Button(
            onClick = { viewModel.listUsers() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.list_users))
        }

        Button(
            onClick = { viewModel.takeMeasurement() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.take_measure))
        }

        var consentCode by rememberSaveable { mutableStateOf(1234) }
        var userIndex by rememberSaveable { mutableStateOf(1) }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
//            TextField(
//                modifier = Modifier.weight(0.5f).padding(5.dp),
//                value = consentCode.toString(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                onValueChange = {
//                    consentCode = it.toInt()
//                },
//                label = { Text("Consent code") }
//            )
//
//            TextField(
//                modifier = Modifier.weight(0.5f).padding(5.dp),
//                value = userIndex.toString(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                onValueChange = {
//                    userIndex = it.toInt()
//                },
//                label = { Text("User index") }
//            )
        }

        Button(
            onClick = {
                viewModel.createUser(
                    user = ScaleUser(
                        793370044000L,
                        162,
                        Gender.FEMALE,
                        2
                    ), consentCode = consentCode
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.create_user))
        }

        Button(
            onClick = { viewModel.consent(userIndex, consentCode) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.consent))
        }

        Button(
            onClick = { viewModel.deleteUserData() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.delete_user_data))
        }

    }
}