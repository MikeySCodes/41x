package com.example.ble101;

import android.os.Bundle;
import android.view.View;
import androidx.activity.ComponentActivity;
import androidx.activity.enableEdgeToEdge;
import androidx.activity.compose.setContent;
import androidx.compose.foundation.layout.fillMaxSize;
import androidx.compose.foundation.layout.padding;
import androidx.compose.material3.Scaffold;
import androidx.compose.material3.Text;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.tooling.preview.Preview;
import com.example.ble101.ui.theme.BLE101Theme;

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
    }

    fun startBLuetooth(view: View) {
        // TODO: Add Bluetooth start logic here
    }

    fun searchBLuetooth(view: View) {
        // TODO: Add Bluetooth search logic here
    }
}
