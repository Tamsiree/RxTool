package com.tamsiree.rxarcgiskit.tool

import android.content.Context
import android.location.Location
import android.widget.TextView
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.data.TileCache
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.layers.ArcGISTiledLayer
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer
import com.esri.arcgisruntime.loadable.LoadStatus
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.MobileMapPackage
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask
import com.tamsiree.rxarcgiskit.model.MapPreview
import com.tamsiree.rxarcgiskit.view.RxMapScaleView
import com.tamsiree.rxkit.RxDataTool.Companion.changeDistance
import com.tamsiree.rxkit.RxDataTool.Companion.isNullString
import com.tamsiree.rxkit.RxDataTool.Companion.stringToDouble
import com.tamsiree.rxkit.RxImageTool.dp2px
import com.tamsiree.rxkit.RxLocationTool.GPS84ToGCJ02
import com.tamsiree.rxkit.RxMapTool.screenPixelToMetre
import com.tamsiree.rxkit.RxSPTool.getContent
import com.tamsiree.rxkit.TLog
import com.tamsiree.rxkit.model.Gps
import com.tamsiree.rxkit.view.RxToast
import java.io.File
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * Created by Tamsiree on 2017/8/9.
 */
object RxArcGisMapTool {
    /**
     * initialize ArcGis MapView
     *
     * @param mMapView 地图容器
     */
    fun initAcrMap(mContext: Context?, mMapView: MapView, mTvPlottingScale: TextView) {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud5954166547,none,2K0RJAY3FLGP9KB10136")
        mMapView.addMapScaleChangedListener { mapScaleChangedEvent ->
            val s: Double = screenPixelToMetre(dp2px(30f) * 1.5, mapScaleChangedEvent.source.mapScale, mContext!!)
            mTvPlottingScale.text = changeDistance(s, false)
        }
    }

    fun initAcrMap(mContext: Context?, mMapView: MapView, mScaleView: RxMapScaleView) {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud5954166547,none,2K0RJAY3FLGP9KB10136")
        mScaleView.setMapView(mMapView)
        mMapView.addMapScaleChangedListener { mScaleView.refreshScaleView() }
    }

    fun mapScaleIn(mMapView: MapView) {
        val mScale = mMapView.mapScale
        mMapView.setViewpointScaleAsync(mScale * 0.5)
    }

    fun mapScaleOut(mMapView: MapView) {
        val mScale1 = mMapView.mapScale
        mMapView.setViewpointScaleAsync(mScale1 * 2)
    }

    /**
     * 获取 保存在本地的 离线地图切片 TPK
     *
     * @param mContext 实体
     * @param unitCode 填报单位代码
     * @return
     */
    fun getTpkPath(mContext: Context?, unitCode: String): String {
        val tpkPath = getContent(mContext!!, "TPK")
        return File(tpkPath).parent + File.separator + unitCode + ".zip"
    }

    /**
     * 首次定位成功后的 地图 平移 与 缩放
     *
     * @param mMapView 地图工具
     * @param mapPoint 定位成功的点
     */
    fun locationFirst(mMapView: MapView, mapPoint: Point?) {
        mMapView.setViewpointCenterAsync(mapPoint, 600.0)
    }

    /**
     * 地图 镜头平移到  当前 定位
     *
     * @param mMapView 地图工具
     * @param mapPoint 定位成功的点
     */
    fun locationSelf(mMapView: MapView, mapPoint: Point?) {
        if (mapPoint == null) {
            RxToast.normal("GPS正在定位中")
        } else {
            mMapView.setViewpointCenterAsync(mapPoint, 600.0)
        }
    }

    /**
     * 单次定位 平移镜头
     *
     * @param mMapView      地图控件
     * @param location      定位信息
     * @param gLayerPos     图层
     * @param mMarkerSymbol 定位标记
     * @param isGPS         是否为GPS坐标系
     * @param isCenter      是否将定位成功的点平移到镜头正中央
     * @return
     */
    fun markLocationSingle(mMapView: MapView, location: Location, gLayerPos: GraphicsOverlay, mMarkerSymbol: PictureMarkerSymbol?, isGPS: Boolean, isCenter: Boolean): Point {
        gLayerPos.graphics.clear()
        return markLocation(mMapView, location, gLayerPos, mMarkerSymbol, isGPS, isCenter)
    }

    fun markLocationSingle(mMapView: MapView, longitude: Double, latitude: Double, gLayerPos: GraphicsOverlay, mMarkerSymbol: PictureMarkerSymbol?, isGPS: Boolean, isCenter: Boolean): Point {
        gLayerPos.graphics.clear()
        return markLocation(mMapView, longitude, latitude, gLayerPos, mMarkerSymbol, isGPS, isCenter)
    }

    fun getGps(longitude: Double, latitude: Double, isGps: Boolean): Gps? {
        val gps: Gps?
        gps = if (isGps) {
            Gps(longitude, latitude)
        } else {
            GPS84ToGCJ02(longitude, latitude)
        }
        return gps
    }

    fun markLocation(mMapView: MapView, location: Location, gLayerPos: GraphicsOverlay, mMarkerSymbol: PictureMarkerSymbol?, isGPS: Boolean, isCenter: Boolean): Point {
        val gps = getGps(location.longitude, location.latitude, isGPS)
        return markLocation(mMapView, gps, gLayerPos, mMarkerSymbol, isCenter)
    }

    fun markLocation(mMapView: MapView, longitude: Double, latitude: Double, gLayerPos: GraphicsOverlay, mMarkerSymbol: PictureMarkerSymbol?, isGPS: Boolean, isCenter: Boolean): Point {
        val gps = getGps(longitude, latitude, isGPS)
        return markLocation(mMapView, gps, gLayerPos, mMarkerSymbol, isCenter)
    }

    fun markLocation(mMapView: MapView, gps: Gps?, gLayerPos: GraphicsOverlay, mMarkerSymbol: PictureMarkerSymbol?, isCenter: Boolean): Point {
        //经度
        val locx = gps!!.longitude
        //纬度
        val locy = gps.latitude
        //定位到所在位置
        val mapPoint = Point(locx, locy, SpatialReference.create(4326))
        val graphic = Graphic(mapPoint, mMarkerSymbol)
        gLayerPos.graphics.add(graphic)
        if (isCenter) {
            mMapView.setViewpointCenterAsync(mapPoint, 600.0)
        }
        return mapPoint
    }

    fun addMapLocalTPK(mMapView: MapView, path: String?) {
        if (File(path).exists()) {
            val mainTileCache = TileCache(path)
            val layer = ArcGISTiledLayer(mainTileCache)
            val arcGISMap = mMapView.map
            arcGISMap.basemap.referenceLayers.add(layer)
            //将离线地图加载到MapView中
            mMapView.map = arcGISMap
        }
    }

    fun addMapLocalVTPK(mMapView: MapView, vtpk: String?) {
        if (File(vtpk).exists()) {
            val mVectorTiledLayer = ArcGISVectorTiledLayer(vtpk)
            var arcGISMap = mMapView.map
            if (arcGISMap == null) {
                arcGISMap = ArcGISMap(Basemap(mVectorTiledLayer))
            } else {
                val layers = arcGISMap.basemap.baseLayers
                layers.add(layers.size, mVectorTiledLayer)
            }
            val vp = Viewpoint(27.6699, 111.8236, 8000000.00)
            arcGISMap.initialViewpoint = vp
            //将离线地图加载到MapView中
            mMapView.map = arcGISMap
        }
    }

    fun addMapLocalMMPK(mMapView: MapView, mmpk: String?) {
        val mainMobileMapPackage = MobileMapPackage(mmpk)
        mainMobileMapPackage.loadAsync()
        mainMobileMapPackage.addDoneLoadingListener {
            val mainLoadStatus = mainMobileMapPackage.loadStatus
            if (mainLoadStatus == LoadStatus.LOADED) {
                val mainArcGISMapL = mainMobileMapPackage.maps
                val mainArcGISMap = mainArcGISMapL[0]
                mMapView.map = mainArcGISMap
            }
        }
    }

    fun addMapLocalMMPK(mContext: Context?, mMapView: MapView, mmpk: String?) {
        val mMobileMapPackage = MobileMapPackage(mmpk)
        val mMapPreviews = ArrayList<MapPreview>()
        mMobileMapPackage.loadAsync()
        mMobileMapPackage.addDoneLoadingListener {
            if (mMobileMapPackage.loadStatus == LoadStatus.LOADED &&
                    mMobileMapPackage.maps.size > 0) {
                val mLocatorTask = mMobileMapPackage.locatorTask
                //default to display of first map in package
                val map = mMobileMapPackage.maps[0]
                val mRouteTask: RouteTask?
                val mRouteParameters: RouteParameters
                //if map contains transport network setup route task
                if (map.transportationNetworks.size > 0) {
                    mRouteTask = RouteTask(mContext, map.transportationNetworks[0])
                    try {
                        mRouteParameters = mRouteTask.createDefaultParametersAsync().get()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    //only allow routing on map with transport networks
                    mRouteTask = null
                }
                mMapView.map = map
                val mMMPkTitle = mMobileMapPackage.item.title
                //for each map in the mobile map package, pull out relevant thumbnail information
                for (i in mMobileMapPackage.maps.indices) {
                    val currMap = mMobileMapPackage.maps[i]
                    val mapPreview = MapPreview()
                    //set map number
                    mapPreview.mapNum = i
                    //set map title. If null use the index of the list of maps to name each map Map #
                    if (currMap.item != null && currMap.item.title != null) {
                        mapPreview.title = currMap.item.title
                    } else {
                        mapPreview.title = "Map $i"
                    }
                    //set map description. If null use package description instead
                    if (currMap.item != null && currMap.item.description != null) {
                        mapPreview.desc = currMap.item.description
                    } else {
                        mapPreview.desc = mMobileMapPackage.item.description
                    }
                    //check if map has transport data
                    if (currMap.transportationNetworks.size > 0) {
                        mapPreview.setTransportNetwork(true)
                    }
                    //check if map has geocoding data
                    if (mMobileMapPackage.locatorTask != null) {
                        mapPreview.setGeocoding(true)
                    }
                    //set map preview thumbnail
                    val thumbnailAsync: ListenableFuture<ByteArray>
                    thumbnailAsync = if (currMap.item != null && currMap.item.fetchThumbnailAsync() != null) {
                        currMap.item.fetchThumbnailAsync()
                    } else {
                        mMobileMapPackage.item.fetchThumbnailAsync()
                    }
                    thumbnailAsync.addDoneListener {
                        if (thumbnailAsync.isDone) {
                            try {
                                mapPreview.thumbnailByteStream = thumbnailAsync.get()
                                mMapPreviews.add(mapPreview)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            } catch (e: ExecutionException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            } else {
                //log an issue if the mobile map package fails to load
                TLog.e(mMobileMapPackage.loadError.message!!)
            }
        }
    }

    fun getPointLine(shapes: List<String>?, mMapView: MapView, gLayerRoadHistory: GraphicsOverlay, gLayerRoadHistoryLine: GraphicsOverlay, mMarkerSymbolHistory: PictureMarkerSymbol?, isGPS: Boolean) {
        gLayerRoadHistory.graphics.clear()
        gLayerRoadHistoryLine.graphics.clear()
        if (shapes != null) {
            for (ldModel in shapes) {
                if (!isNullString(ldModel)) {
                    val shape = ldModel.substring(11, ldModel.length - 1)
                    if (!isNullString(shape)) {
                        val arrayShape = shape.split(",").toTypedArray()
                        val lineGeometry = PolylineBuilder(SpatialReference.create(4326) /*SpatialReferences.getWebMercator()*/)
                        for (i in arrayShape.indices) {
                            val str = arrayShape[i]
                            if (!isNullString(str)) {
                                val arrayPoint = str.split(" ").toTypedArray()
                                val gps = getGps(stringToDouble(arrayPoint[0]), stringToDouble(arrayPoint[1]), isGPS)
                                if (gps != null) {
                                    //经度
                                    val locx = gps.longitude
                                    //纬度
                                    val locy = gps.latitude
                                    val mapPoint = Point(locx, locy /*, SpatialReference.create(4326)*/)
                                    //定位到所在位置
                                    if (i == 0) {
                                        markLocation(mMapView, stringToDouble(arrayPoint[0]), stringToDouble(arrayPoint[1]), gLayerRoadHistory, mMarkerSymbolHistory, isGPS, false)
                                        lineGeometry.addPoint(mapPoint)
                                    } else {
                                        if (i == arrayShape.size - 1) {
                                            lineGeometry.addPoint(mapPoint)
                                            val lineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, -0x33ad4569, 3f)
                                            val lineGraphic = Graphic(lineGeometry.toGeometry())
                                            val lineRenderer = SimpleRenderer(lineSymbol)
                                            gLayerRoadHistoryLine.renderer = lineRenderer
                                            gLayerRoadHistoryLine.graphics.add(lineGraphic)
                                            markLocation(mMapView, stringToDouble(arrayPoint[0]), stringToDouble(arrayPoint[1]), gLayerRoadHistory, mMarkerSymbolHistory, isGPS, false)
                                        } else {
                                            lineGeometry.addPoint(mapPoint)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算线要素长度
     * 长度单位和线要素的空间参考的单位一致
     *
     * @param pline
     */
    fun lengthPolyline(pline: Polyline?): Double {
        return GeometryEngine.length(pline)
    }

    fun initPointLine(shapeStr: String, gpsList: List<Gps>?, gLayerPos: GraphicsOverlay, orginLocationSymbol: PictureMarkerSymbol?, finalLocationSymbol: PictureMarkerSymbol?, locationSymbol: PictureMarkerSymbol?, gLayerPosLine: GraphicsOverlay, mMapView: MapView) {
        gLayerPosLine.graphics.clear()
        gLayerPos.graphics.clear()
        if (!isNullString(shapeStr)) {
            var shape = shapeStr
            if (shapeStr.contains("LINESTRING")) {
                shape = shapeStr.substring(12, shapeStr.length - 1)
            }
            if (!isNullString(shape)) {
                val arrayShape = shape.split(", ").toTypedArray()
                val originShape = arrayShape[0].split(" ").toTypedArray()
                val originGps = getGps(stringToDouble(originShape[0]), stringToDouble(originShape[1]), true)
                //定位到所在位置
                val mapOriginPoint = Point(originGps!!.longitude, originGps.latitude, SpatialReference.create(4326))
                val graphic = Graphic(mapOriginPoint, orginLocationSymbol)
                gLayerPos.graphics.add(graphic)
                val finalShape = arrayShape[arrayShape.size - 1].split(" ").toTypedArray()
                val finalGps = getGps(stringToDouble(finalShape[0]), stringToDouble(finalShape[1]), true)
                //定位到所在位置
                val mapFinalPoint = Point(finalGps!!.longitude, finalGps.latitude, SpatialReference.create(4326))
                val graphicFinal = Graphic(mapFinalPoint, finalLocationSymbol)
                gLayerPos.graphics.add(graphicFinal)
                val shapes = arrayShape[arrayShape.size / 2]
                val currentPoint = shapes.split(" ").toTypedArray()
                val currentGps = getGps(stringToDouble(currentPoint[0]), stringToDouble(currentPoint[1]), true)
                val currentMapPoint = Point(currentGps!!.longitude, currentGps.latitude, SpatialReference.create(4326))
                mMapView.setViewpointCenterAsync(currentMapPoint, 60000.0)
                val lineGeometry = PolylineBuilder(SpatialReference.create(4326))
                for (i in arrayShape.indices) {
                    val str = arrayShape[i]
                    if (!isNullString(str)) {
                        val arrayPoint = str.split(" ").toTypedArray()
                        val gps = getGps(stringToDouble(arrayPoint[0]), stringToDouble(arrayPoint[1]), true)
                        if (gps != null) {
                            //经度
                            val locx = gps.longitude
                            //纬度
                            val locy = gps.latitude
                            val mapPoint = Point(locx, locy)
                            if (i == 0) {
                                lineGeometry.addPoint(mapPoint)
                            } else {
                                if (i == arrayShape.size - 1) {
                                    lineGeometry.addPoint(mapPoint)
                                    val lineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, -0x33de690d, 5f)
                                    val lineGraphic = Graphic(lineGeometry.toGeometry())
                                    val lineRenderer = SimpleRenderer(lineSymbol)
                                    gLayerPosLine.renderer = lineRenderer
                                    gLayerPosLine.graphics.add(lineGraphic)
                                } else {
                                    lineGeometry.addPoint(mapPoint)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (gpsList != null) {
            for (gps in gpsList) {
                //定位到所在位置
                val mapPoint = Point(gps.longitude, gps.latitude, SpatialReference.create(4326))
                val graphic = Graphic(mapPoint, locationSymbol)
                gLayerPos.graphics.add(graphic)
            }
        }
    }
}