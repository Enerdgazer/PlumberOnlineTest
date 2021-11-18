package com.example.plumberonlinetest.UI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.plumberonlinetest.R

class MainActivity : AppCompatActivity() {
    private val locationManager by lazy {
        this.getSystemService(LOCATION_SERVICE) as LocationManager
    }
    private val mapViewModelUserLocation by lazy { ViewModelProvider(this)[ViewModelMain::class.java] }
    lateinit var buttonMap: Button
    private val PERMISSION_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonMap = findViewById(R.id.map_button)
        checkOnPremmision()
        buttonMap.setOnClickListener { goToMap() }


    }

    private fun goToMap() {
        if (checkOnGeoLocation()) {
            val locationData = mapViewModelUserLocation.getDataMapUser()
            locationData?.observe(this, Observer {
                it?.let {
                    val intent = Intent(this, MapActivity::class.java)
                    intent.putExtra("latUser", it.lat)
                    intent.putExtra("lonUser", it.lon)
                    startActivity(intent)
                }
            })
        } else {
            Toast.makeText(this, "Включите геопозицию", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkOnPremmision() {
        val coarseLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLocation =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (coarseLocation == PackageManager.PERMISSION_GRANTED &&
            fineLocation == PackageManager.PERMISSION_GRANTED
        ) {
            buttonMap.visibility = View.VISIBLE

        } else {
            onPremmision()

        }
    }

    private fun onPremmision() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_CODE

        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            checkOnPremmision()
        } else {
            checkOnPremmision()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkOnGeoLocation(): Boolean {
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    override fun onStop() {
        super.onStop()
        mapViewModelUserLocation.canelManager()
    }

}