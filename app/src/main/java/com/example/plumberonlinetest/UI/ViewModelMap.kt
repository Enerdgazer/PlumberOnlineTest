package com.example.plumberonlinetest.UI


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plumberonlinetest.data.GeoData
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error


class ViewModelMap : ViewModel() {
    lateinit var searchListener: Session.SearchListener
    var data: MutableLiveData<GeoData>? = null
    private val searchManager: SearchManager by lazy {
        SearchFactory.getInstance().createSearchManager(
            SearchManagerType.ONLINE
        )
    }

    fun getDataMap(): LiveData<GeoData?>? {
        if (data == null) {
            data = MutableLiveData()
            loadDataMap()
        }
        return data
    }

    private fun loadDataMap() {
        searchListener = object : Session.SearchListener {
            override fun onSearchResponse(p0: Response) {
                val obj = p0.collection.children.firstOrNull()?.obj!!
                val toponim = obj.metadataContainer.getItem(ToponymObjectMetadata::class.java)
                val address = toponim.address
                val point = toponim.balloonPoint
                data?.value = GeoData(point.latitude, point.longitude, address.formattedAddress)
            }

            override fun onSearchError(p0: Error) {
                Log.e("ОШИБКА",p0.toString())
            }

        }
    }

    fun startSearch(point: Point) {
        searchManager.submit(point, 16, SearchOptions(), searchListener)
    }

    fun clearSearch() {
        data?.value = null
    }

    val mapListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: com.yandex.mapkit.geometry.Point) {
            startSearch(p1)
        }

        override fun onMapLongTap(p0: Map, p1: com.yandex.mapkit.geometry.Point) {

        }

    }
}
