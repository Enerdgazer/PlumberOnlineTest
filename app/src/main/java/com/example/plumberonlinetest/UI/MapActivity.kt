package com.example.plumberonlinetest.UI

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.plumberonlinetest.R
import com.example.plumberonlinetest.data.GeoData
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

private const val KEYMAP = "d20c0071-9903-493a-8961-9bbaae9de501"

class MapActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    private val mapViewModel by lazy { ViewModelProvider(this)[ViewModelMap::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(KEYMAP)
        setContentView(R.layout.activity_map)
        initMap()
        setDefaultMapPosition()
        loadDataMap()

    }

    private fun initMap() {
        MapKitFactory.initialize(this)
        mapView = findViewById(R.id.mapview)
        mapView.map.addInputListener(mapViewModel.mapListener)

    }

    private fun setDefaultMapPosition() {
        var lat=intent.getDoubleExtra("latUser", 0.0)
        var lon=intent.getDoubleExtra("lonUser", 0.0)
        mapView.map.move(
            CameraPosition(
                Point(lat, lon),
                11.0f, 0.0f, 0.0f
            ),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
    }

    private fun loadDataMap() {
        val loadData = mapViewModel.getDataMap()
        loadData?.observe(this, Observer {
            it?.let {
                mapView.map.mapObjects.clear()
                mapView.map.mapObjects.addPlacemark(Point(it.lat, it.lon))
                cameraPositionMove(it.lat, it.lon)
                mapViewModel.clearSearch()
                dialogShow(it)
            }
        })

    }

    private fun cameraPositionMove(lat: Double, lon: Double) {
        mapView.map.move(
            CameraPosition(
                Point(lat, lon),
                16.0f, 0.0f, 0.0f
            ),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

    }

    private fun dialogShow(geoData: GeoData) {
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("Пожалуйста, подтвердите адресс")  // заголовок
        dialog.setMessage(geoData.address)
        dialog.setPositiveButton("Да", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                val intent = Intent(this@MapActivity, PinInfoActivity::class.java)
                intent.putExtra("address", geoData.address)
                intent.putExtra("lat", geoData.lat)
                intent.putExtra("lon", geoData.lon)
                startActivity(intent)
            }
        })
        dialog.setNegativeButton("Нет", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                dialog.setCancelable(true)
            }
        })
        dialog.show()

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }






}