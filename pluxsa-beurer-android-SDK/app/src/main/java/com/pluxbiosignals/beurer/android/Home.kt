package com.pluxbiosignals.beurer.android

import android.bluetooth.BluetoothClass
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.pluxbiosignals.beurer.android.databinding.ActivityHomeBinding
import com.pluxbiosignals.beurer.android.model.Device
import com.pluxbiosignals.beurer.android.viewmodels.OximeterViewModel
import com.pluxbiosignals.beurer.android.viewmodels.ScaleViewModel
import com.pluxbiosignals.beurer.android.viewmodels.ScanViewModel
import com.pluxbiosignals.beurer.android.viewmodels.ScanViewModelFactory
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModel
import com.pluxbiosignals.beurer.android.viewmodels.base.DeviceViewModelFactory
import com.pluxbiosignals.beurer.api.DeviceScan
import com.pluxbiosignals.beurer.api.enums.Devices

class Home : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var deviceViewModel: DeviceViewModel // Declare uma variável para a ViewModel
    private lateinit var oximeterViewModel: OximeterViewModel // Declare uma variável para a ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        binding.textConnect.setOnClickListener {
            val selectedDevice = Device("D8:3A:C3:1C:2F:00", "PO60", Devices.OXIMETER)
            // Inicialize a ViewModel usando um ViewModelProvider

            deviceViewModel = ViewModelProvider(
                this,
                DeviceViewModelFactory(selectedDevice, applicationContext)
            ).get(DeviceViewModel::class.java)

            // Agora você pode acessar os métodos e propriedades da ViewModel
            deviceViewModel.connectDevice() // Exemplo de chamada de método

        }

        binding.textdesConnect.setOnClickListener {
            val selectedDevice = Device("D8:3A:C3:1C:2F:00", "PO60", Devices.OXIMETER)
            // Inicialize a ViewModel usando um ViewModelProvider

            deviceViewModel = ViewModelProvider(
                this,
                DeviceViewModelFactory(selectedDevice, applicationContext)
            ).get(DeviceViewModel::class.java)

            // Agora você pode acessar os métodos e propriedades da ViewModel
            deviceViewModel.disconnectDevice() // Exemplo de chamada de método

        }

        binding.getinfo.setOnClickListener {
            // Substitua 'selectedDevice' com uma instância válida de 'Device' ou obtenha-a da forma apropriada.
            val selectedDevice = Device("D8:3A:C3:1C:2F:00", "PO60", Devices.OXIMETER)

            // Inicialize a ViewModel usando um ViewModelProvider
            oximeterViewModel = ViewModelProvider(this, DeviceViewModelFactory(selectedDevice, applicationContext)).get(OximeterViewModel::class.java)

            // Agora você pode acessar os métodos e propriedades da ViewModel
            oximeterViewModel.getData() // Exemplo de chamada de método
        }

    }
}