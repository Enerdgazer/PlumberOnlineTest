package com.example.plumberonlinetest.UI

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.plumberonlinetest.R

class PinInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_info)
        initPinInfo()

    }

    fun initPinInfo() {
        val dataAddress: TextView = findViewById(R.id.data_address)
        val dataLat: TextView = findViewById(R.id.data_datalat)
        val dataLon: TextView = findViewById(R.id.data_lon)
        val lonIntent = intent.getDoubleExtra("lon", 0.0)
        val latIntent = intent.getDoubleExtra("lat", 0.0)
        val addressIntent = intent.getStringExtra("address")
        dataLat.text = latIntent.toString()
        dataLon.text = lonIntent.toString()
        dataAddress.text = addressIntent
        val buttonChange: Button = findViewById(R.id.changeData)
        buttonChange.setOnClickListener {
            finish()
        }

    }
}