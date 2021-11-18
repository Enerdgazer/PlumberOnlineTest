package com.example.plumberonlinetest.UI

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.plumberonlinetest.data.GeoData
import android.os.Bundle

import androidx.annotation.NonNull




class ViewModelMain(app: Application) : AndroidViewModel(app) {
    private val locationManager by lazy {
        app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    var dataPositionUser: MutableLiveData<GeoData>? = null
    fun getDataMapUser(): LiveData<GeoData?>? {
        if (dataPositionUser == null) {
            dataPositionUser = MutableLiveData()
            loadDataPositonUser()
        }
        return dataPositionUser
    }

    private val locationUserListener = LocationListener {
        dataPositionUser?.value = GeoData(it.latitude, it.longitude)
    }

    @SuppressLint("MissingPermission")
    private fun loadDataPositonUser() {
        val positionNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val positionGPS =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (positionNetwork == null && positionGPS == null) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationUserListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                locationUserListener
            )
        } else {
            val position = positionNetwork ?: positionGPS
            dataPositionUser?.value = GeoData(position!!.latitude, position.longitude)


        }


    }
    fun canelManager() {
        dataPositionUser = null
        locationManager.removeUpdates(locationUserListener)
    }

}