package com.pluxbiosignals.beurer.android
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pluxbiosignals.beurer.android.databinding.ActivityHomeBinding
import com.pluxbiosignals.beurer.android.ui.theme.BeurerAndroidTheme
import com.pluxbiosignals.beurer.android.ui.views.BeurerApp

class MainActivity : ComponentActivity() {

    private val TAG = this.javaClass.simpleName

    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val bluetoothService = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = bluetoothService.adapter

        val intent = Intent(this, Home::class.java)
        startActivity(intent)

        setContent {

            BeurerAndroidTheme() {
                BeurerApp(bluetoothAdapter = adapter)
            }

        }
    }
}