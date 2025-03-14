package com.example.ble101

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
    }

    fun startBluetooth(view: View) {
        // TODO: Add Bluetooth start logic here
    }

    fun searchBluetooth(view: View) {
        // TODO: Add Bluetooth search logic here
    }

    fun connectBluetooth(view: View) {
        // TODO: Add Bluetooth connect logic here
    }

    fun discoverServices(view: View) {
        // TODO: Add service discovery logic here
    }

    fun Disconnect(view: View) {
        // TODO: Add disconnect logic here
    }
}