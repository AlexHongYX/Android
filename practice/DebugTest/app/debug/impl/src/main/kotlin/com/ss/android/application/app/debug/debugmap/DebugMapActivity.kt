package com.ss.android.application.app.debug.debugmap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bytedance.common.utility.NetworkClient
import com.bytedance.i18n.business.debug.R
import com.bytedance.i18n.business.framework.legacy.service.setting.IDebugSettings
import com.bytedance.router.SmartRouter
import com.bytedance.router.annotation.RouteUri
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.ss.android.application.app.debug.data.BuzzCitiesProvider
import com.ss.android.article.ugc.trace.UgcTraceIdHelper
import com.ss.android.buzz.ArticleModel
import com.ss.android.buzz.PoiItem
import com.ss.android.buzz.base.BuzzAbsActivity
import com.ss.android.buzz.feed.dagger.CoreEngineParam
import com.ss.android.buzz.feed.dagger.IFeedDataEngine
import com.ss.android.buzz.feed.dagger.IFeedDataEngineProvider
import com.ss.android.buzz.feed.dagger.IFeedDataReceiver
import com.ss.android.buzz.feed.data.AbsCardDataModel
import com.ss.android.buzz.feed.data.MainFeedDataModel
import com.ss.android.buzz.location.ugc.IPoiManager
import com.ss.android.buzz.location.ugc.model.PoiListResp
import com.ss.android.network.threadpool.BuzzApiPool
import com.ss.android.network.threadpool.FastMain
import com.ss.android.uilib.base.contextJob
import com.ss.android.utils.loadFirst
import kotlinx.android.synthetic.main.activity_debug_map.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@RouteUri("//buzz/debug/gmap")
class DebugMapActivity : BuzzAbsActivity(), OnMapReadyCallback {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 2
    var poiManager: IPoiManager = IPoiManager::class.loadFirst()
    var mGoogleMap: GoogleMap? = null
    private var oldMarkers: MutableList<Marker> = mutableListOf()
    private var poiItems: MutableList<PoiItem> = mutableListOf()
    private var requestCount: Int = 0
    private var externalDirectory: File? = null
    private var isMoving = false
    private val adapters = ArrayList<ArrayAdapter<String>>()
    private val items = ArrayList<List<String>>()
    private val selectItems = arrayOfNulls<String>(4)
    private var chooseCityLatLng: LatLng? = null
    private var centerMarker: Marker? = null
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var dataEngine: IFeedDataEngine
    private var dataModel: MainFeedDataModel? = null
    private var feedPosition: MutableList<Marker> = mutableListOf()
    private var markerToModelList: MutableList<markerToModel> = mutableListOf()
    private var lastRequestLatitude: String? = null
    private var lastRequestLongitude: String? = null
    val setting = IDebugSettings::class.loadFirst()

    private val pullListener = object : IFeedDataReceiver {
        override var cancel: Boolean = false
        override fun onPermissionRejected(status: Int) {
        }

        override fun onResult(dataModel: MainFeedDataModel) {
            this@DebugMapActivity.dataModel = dataModel
            val cards = this@DebugMapActivity.dataModel?.aggregatedDataList
            var count = 0
            cards?.forEach {
                val latitude = it.getLatitude() ?: 0f
                val longitude = it.getLongitude() ?: 0f
                if (latitude > 0f && longitude > 0f) {
                    count++
                    if (mGoogleMap != null) {
                        val marker = mGoogleMap?.addMarker(MarkerOptions().
                                position(LatLng(latitude.toDouble(), longitude.toDouble())).
                                title(it.getDistance()).
                                snippet("Click to go to feed detail"))

                        marker?.let { markerNotNull ->
                            markerNotNull.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            feedPosition.add(markerNotNull)
                            markerToModelList.add(markerToModel(markerNotNull, it))

                        }
                    }
                }
            }
            if (cards == null) {
                Toast.makeText(applicationContext, "获取卡片失败", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "共获得" + cards.size.toString() + "条Feed，其中含有经纬度的有" + count.toString() + "条", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AbsCardDataModel
        setContentView(R.layout.activity_debug_map)
        initSpinner()
        dataEngine = IFeedDataEngineProvider::class.loadFirst().getStreamDataEngine()
        dataEngine.bind(this.contextJob, null, null, null)
        mapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.map_fragment, mapFragment)
            .commitAllowingStateLoss()
        mapFragment.getMapAsync(this)
        context?.let {
            externalDirectory = getPoiStorageDir(it)
            externalDirectory?.mkdir()
        }
        save_button.setOnClickListener {
            val formatter = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
            val date = Date(System.currentTimeMillis())
            val file = File(externalDirectory, formatter.format(date))
            file.let {
                it.writeBytes(stringGenerator(poiItems).toByteArray())
                Toast.makeText(applicationContext, "poi列表已保存至" + externalDirectory?.absolutePath, Toast.LENGTH_SHORT).show()
            }
        }
        feed_get_button.setOnClickListener {
            lastRequestLatitude = setting.getLongitude()
            lastRequestLongitude = setting.getLongitude()
            markerToModelList.removeAll { true }
            feedPosition.forEach {
                it.remove()
            }
            feedPosition.removeAll { true }

            pullListener.let {
                dataEngine.pull(CoreEngineParam(category = CoreEngineParam.CATEGORY_BUZZ_NEARBY,
                    openFirstQueryCache = true,
                    useTimeStampColdLaunch = true,
                    isNeedGpsInfo = true), dataModel, it
                )
            }
        }
        upload_button.setOnClickListener {
            val dataModel = dataModel ?: return@setOnClickListener
            if (lastRequestLatitude == null && lastRequestLongitude == null) {
                Toast.makeText(applicationContext, "没有数据可以上传", Toast.LENGTH_SHORT).show()
            } else {
                val pathUrl = "api/749/admin/data_upload"
                val jsonObject = JsonObject()
                val jsonArray = JsonArray()
                for (singleDataModel in dataModel.aggregatedDataList) {
                    val groupId = singleDataModel.getGroupId() ?: 0L
                    val itemId = singleDataModel.getItemId() ?: 0L
                    if (groupId > 0L && itemId > 0L ) {
                        val itemJsonObject = JsonObject()
                        itemJsonObject.addProperty("itemId", itemId)
                        itemJsonObject.addProperty("groupId", groupId)
                        jsonArray.add(itemJsonObject)
                    }
                }
                jsonObject.add("items", jsonArray)
                jsonObject.addProperty("requestLatitude", lastRequestLatitude)
                jsonObject.addProperty("requestLongitude", lastRequestLongitude)
                jsonObject.addProperty("time", System.currentTimeMillis())
                val data = jsonObject.toString().toByteArray()
                CoroutineScope(this.contextJob + FastMain).launch {
                    uploadFeedData(pathUrl, data)
                }
            }
        }
    }

    private fun uploadFeedData(pathUrl: String, data: ByteArray) = CoroutineScope(BuzzApiPool).async {
        val resultString = NetworkClient.getDefault().post(pathUrl, data, true, "application/json; charset=utf-8", false)
        if (resultString != null) {
            val json = JSONObject(resultString)
            val message = json.getString("message")
            if (message == "success") {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "上传成功", Toast.LENGTH_SHORT).show()
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "上传失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                        mGoogleMap?.isMyLocationEnabled = true
                    }
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        mGoogleMap = googleMap
        mGoogleMap?.let { googleMapNotNull ->
            val cameraPosition = googleMapNotNull.cameraPosition
            scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude)
            if (ContextCompat.checkSelfPermission(applicationContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMapNotNull.isMyLocationEnabled = true
            } else {
                ActivityCompat.requestPermissions(this@DebugMapActivity,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            with(googleMapNotNull.uiSettings) {
                isZoomControlsEnabled = true
                isCompassEnabled = true
                isMyLocationButtonEnabled = true
                isIndoorLevelPickerEnabled = false
                isMapToolbarEnabled = false
                isZoomGesturesEnabled = false
                isScrollGesturesEnabled = true
                isTiltGesturesEnabled = false
                isRotateGesturesEnabled = false
            }
            if (chooseCityLatLng != null) {
                googleMapNotNull.moveCamera(CameraUpdateFactory.newLatLngZoom(chooseCityLatLng, 15F))
            } else {
                val delhi = LatLng(28.644800, 77.216721)
                googleMapNotNull.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi, 15F))
            }
            centerMarker = googleMapNotNull.addMarker(MarkerOptions().position(googleMapNotNull.cameraPosition.target))
            centerMarker?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

            val centerLatLng = googleMapNotNull.cameraPosition?.target
            centerLatLng?.let {
                setting.setLongitude(it.longitude.toString())
                setting.setLatitude(it.latitude.toString())
                CoroutineScope(this.contextJob + FastMain).launch {
                    val requestCode = ++requestCount
                    val poiListResp: PoiListResp = poiManager.getPoiList(isDebug = true, traceId = UgcTraceIdHelper.newTraceId()).await()
                    if (requestCode == requestCount && !isMoving) {
                        poiListResp.poiList?.forEach {
                            poiItems.add(it)
                            oldMarkers.add(googleMapNotNull.addMarker(
                                    MarkerOptions().position(LatLng(it.latitude.toDouble(), it.longitude.toDouble())).title(it.poiName)
                            ))
                        }
                    }
                }
            }
            googleMapNotNull.setOnCameraMoveStartedListener {
                isMoving = true
            }
            googleMapNotNull.setOnCameraMoveListener {
                if (isMoving) {
                    centerMarker?.position = centerLatLng
                }
                scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude)
            }
            setOnCameraIdleListener(googleMapNotNull)
            googleMapNotNull.setOnCameraChangeListener {
                scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude)
            }
            googleMapNotNull.setOnMarkerClickListener {
                it.showInfoWindow()
                true
            }
            googleMapNotNull.setOnInfoWindowClickListener {
                for (i in markerToModelList) {
                    if (i.marker == it) {
                        SmartRouter.buildRoute(context, "//topbuzz/buzz/detail")
                                .withParam(ArticleModel.key_group_id, i.model.getGroupId())
                                .withParam(ArticleModel.key_item_id, i.model.getItemId())
                                .open()
                    }
                }
            }
        }
    }

    private fun setOnCameraIdleListener(googleMapNotNull: GoogleMap) {
        googleMapNotNull.setOnCameraIdleListener {
            val centerLatLng = googleMapNotNull.cameraPosition?.target
            if (isMoving) {
                isMoving = false
                oldMarkers.forEach {
                    it.remove()
                }
                oldMarkers.removeAll { true }
                poiItems.removeAll { true }
                centerLatLng?.let {
                    setting.setLongitude(it.longitude.toString())
                    setting.setLatitude(it.latitude.toString())
                    CoroutineScope(this.contextJob + FastMain).launch {
                        val requestCode = ++requestCount
                        val poiListResp: PoiListResp = poiManager.getPoiList(isDebug = true, traceId = UgcTraceIdHelper.newTraceId()).await()
                        if (requestCode == requestCount && !isMoving) {
                            poiListResp.poiList?.forEach {
                                poiItems.add(it)
                                oldMarkers.add(googleMapNotNull.addMarker(
                                        MarkerOptions().position(LatLng(it.latitude.toDouble(), it.longitude.toDouble())).title(it.poiName)
                                ))
                            }
                        }
                    }
                }
            }
            val cameraPosition = googleMapNotNull.cameraPosition
            scaleView.update(cameraPosition.zoom, cameraPosition.target.latitude)
        }
    }

    private fun initSpinner() {
        addItems(language_spinner, BuzzCitiesProvider.getLanguageList() ?: mutableListOf<String>(),
                object : OnItemSelectListener {
            var language: String? = null
            override fun onItemSelect(position: Int, selectItem: String) {
                if (position == 0 || TextUtils.equals(language, selectItem)) {
                    return
                }
                language = selectItem
                selectItems[0] = selectItem
                level_spinner.setSelection(0)
                state_spinner.setSelection(0)
                city_spinner.setSelection(0)
                updateItems(1, BuzzCitiesProvider.getTierList(selectItem))
                updateItems(2, BuzzCitiesProvider.getStateList(selectItem, ""))
                updateItems(3, BuzzCitiesProvider.getCityList(selectItem, "", ""))
            }
        })
        addItems(level_spinner, BuzzCitiesProvider.getTierList("") ?: mutableListOf<String>(),
                object : OnItemSelectListener {
            var level: String? = null
            override fun onItemSelect(position: Int, selectItem: String) {
                if (position == 0 || TextUtils.equals(level, selectItem)) {
                    return
                }
                level = selectItem
                selectItems[1] = selectItem
                state_spinner.setSelection(0)
                city_spinner.setSelection(0)
                updateItems(2, BuzzCitiesProvider.getStateList(selectItems[0], selectItems[1]))
                updateItems(3, BuzzCitiesProvider.getCityList(selectItems[0], selectItems[1], ""))
            }
        }
        )
        addItems(state_spinner, BuzzCitiesProvider.getStateList("") ?: mutableListOf<String>(),
                object : OnItemSelectListener {
            var state: String? = null
            override fun onItemSelect(position: Int, selectItem: String) {
                if (position == 0 || TextUtils.equals(state, selectItem)) {
                    return
                }
                state = selectItem
                selectItems[2] = selectItem
                city_spinner.setSelection(0)
                updateItems(3, BuzzCitiesProvider.getCityList(selectItems[0], selectItems[1], selectItems[2]))
            }
        })
        addItems(city_spinner, BuzzCitiesProvider.getCityList("") ?: mutableListOf<String>(),
                object : OnItemSelectListener {
            var city: String? = null
            override fun onItemSelect(position: Int, selectItem: String) {
                if (position == 0 || TextUtils.equals(city, selectItem)) {
                    return
                }
                city = selectItem
                val cityEntity = BuzzCitiesProvider.getCity(selectItem)
                if (cityEntity != null) {
                    if (mGoogleMap != null) {
                        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(cityEntity.latitude, cityEntity.longitude), 15F))
                        centerMarker?.position = mGoogleMap?.cameraPosition?.target
                    } else {
                        chooseCityLatLng = LatLng(cityEntity.latitude, cityEntity.longitude)
                    }
                }
            }
        })
    }

    private fun getPoiStorageDir(context: Context): File? {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            return File(context.getExternalFilesDir(null), "PoiList")
        }
        return null
    }

    private fun stringGenerator(poiList: MutableList<PoiItem>): String {
        var exportString = ""
        poiList.forEach {
            exportString = exportString + "placeName: " + it.poiName
            exportString = exportString + "latitude: " + it.latitude
            exportString = exportString + "longitude" + it.longitude + "\n"
        }
        return exportString
    }

    private fun addItems(spinner: Spinner, spinnerItems: List<String>, listener: OnItemSelectListener) {
        val adapter = ArrayAdapter<String>(spinner.context, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        adapters.add(adapter)
        items.add(spinnerItems)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (spinnerItems.size - 1 >= position && position >= 0) {
                    listener.onItemSelect(position, spinnerItems[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    fun updateItems(index: Int, items: List<String>?) {
        if (index < 0 ||
            index >= adapters.size ||
            items == null ||
            items.isEmpty()) {
            return
        }
        val adapter = adapters[index]
        adapter.clear()
        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }
}

interface OnItemSelectListener {
    fun onItemSelect(position: Int, selectItem: String)
}

//用于根据用户点击的Marker获取Model进入详情页
class markerToModel(val marker: Marker, val model: AbsCardDataModel)

