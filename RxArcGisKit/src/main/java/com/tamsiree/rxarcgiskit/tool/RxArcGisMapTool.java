package com.tamsiree.rxarcgiskit.tool;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedEvent;
import com.esri.arcgisruntime.mapping.view.MapScaleChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.tamsiree.rxarcgiskit.model.MapPreview;
import com.tamsiree.rxarcgiskit.view.RxMapScaleView;
import com.tamsiree.rxtool.RxDataTool;
import com.tamsiree.rxtool.RxImageTool;
import com.tamsiree.rxtool.RxLocationTool;
import com.tamsiree.rxtool.RxMapTool;
import com.tamsiree.rxtool.RxSPTool;
import com.tamsiree.rxtool.model.Gps;
import com.tamsiree.rxtool.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Vondear on 2017/8/9.
 */

public class RxArcGisMapTool {

    /**
     * initialize ArcGis MapView
     *
     * @param mMapView 地图容器
     */
    public static void initAcrMap(final Context mContext, final MapView mMapView, final TextView mTvPlottingScale) {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud5954166547,none,2K0RJAY3FLGP9KB10136");

        mMapView.addMapScaleChangedListener(new MapScaleChangedListener() {
            @Override
            public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {
                double s = (int) RxMapTool.screenPixelToMetre(RxImageTool.dp2px(30) * 1.5, mapScaleChangedEvent.getSource().getMapScale(), mContext);
                mTvPlottingScale.setText(RxDataTool.changeDistance(s,false));
            }
        });
    }

    public static void initAcrMap(final Context mContext, final MapView mMapView, final RxMapScaleView mScaleView) {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud5954166547,none,2K0RJAY3FLGP9KB10136");
        mScaleView.setMapView(mMapView);
        mMapView.addMapScaleChangedListener(new MapScaleChangedListener() {
            @Override
            public void mapScaleChanged(MapScaleChangedEvent mapScaleChangedEvent) {
                mScaleView.refreshScaleView();
            }
        });
    }

    public static void mapScaleIn(MapView mMapView) {
        double mScale = mMapView.getMapScale();
        mMapView.setViewpointScaleAsync(mScale * 0.5);
    }

    public static void mapScaleOut(MapView mMapView) {
        double mScale1 = mMapView.getMapScale();
        mMapView.setViewpointScaleAsync(mScale1 * 2);
    }

    /**
     * 获取 保存在本地的 离线地图切片 TPK
     *
     * @param mContext 实体
     * @param unitCode 填报单位代码
     * @return
     */
    @NonNull
    public static String getTpkPath(Context mContext, String unitCode) {
        String tpkPath = RxSPTool.getContent(mContext, "TPK");
        return new File(tpkPath).getParent() + File.separator + unitCode + ".zip";
    }

    /**
     * 首次定位成功后的 地图 平移 与 缩放
     *
     * @param mMapView 地图工具
     * @param mapPoint 定位成功的点
     */
    public static void locationFirst(MapView mMapView, Point mapPoint) {
        mMapView.setViewpointCenterAsync(mapPoint, 600);
    }

    /**
     * 地图 镜头平移到  当前 定位
     *
     * @param mMapView 地图工具
     * @param mapPoint 定位成功的点
     */
    public static void locationSelf(MapView mMapView, Point mapPoint) {
        if (mapPoint == null) {
            RxToast.normal("GPS正在定位中");
        } else {
            mMapView.setViewpointCenterAsync(mapPoint, 600);
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
    public static Point markLocationSingle(MapView mMapView, Location location, GraphicsOverlay gLayerPos, PictureMarkerSymbol mMarkerSymbol, boolean isGPS, boolean isCenter) {
        gLayerPos.getGraphics().clear();
        return markLocation(mMapView, location, gLayerPos, mMarkerSymbol, isGPS, isCenter);
    }

    public static Point markLocationSingle(MapView mMapView, double longitude, double latitude, GraphicsOverlay gLayerPos, PictureMarkerSymbol mMarkerSymbol, boolean isGPS, boolean isCenter) {
        gLayerPos.getGraphics().clear();
        return markLocation(mMapView, longitude, latitude, gLayerPos, mMarkerSymbol, isGPS, isCenter);
    }

    public static Gps getGps(double longitude, double latitude, boolean isGps) {
        Gps gps;
        if (isGps) {
            gps = new Gps(longitude, latitude);
        } else {
            gps = RxLocationTool.GPS84ToGCJ02(longitude, latitude);
        }
        return gps;
    }

    public static Point markLocation(MapView mMapView, Location location, GraphicsOverlay gLayerPos, PictureMarkerSymbol mMarkerSymbol, boolean isGPS, boolean isCenter) {
        Gps gps = getGps(location.getLongitude(), location.getLatitude(), isGPS);
        return markLocation(mMapView, gps, gLayerPos, mMarkerSymbol, isCenter);
    }

    public static Point markLocation(MapView mMapView, double longitude, double latitude, GraphicsOverlay gLayerPos, PictureMarkerSymbol mMarkerSymbol, boolean isGPS, boolean isCenter) {
        Gps gps = getGps(longitude, latitude, isGPS);
        return markLocation(mMapView, gps, gLayerPos, mMarkerSymbol, isCenter);
    }

    public static Point markLocation(MapView mMapView, Gps gps, GraphicsOverlay gLayerPos, PictureMarkerSymbol mMarkerSymbol, boolean isCenter) {
        //经度
        double locx = gps.getLongitude();
        //纬度
        double locy = gps.getLatitude();
        //定位到所在位置
        Point mapPoint = new Point(locx, locy, SpatialReference.create(4326));
        Graphic graphic = new Graphic(mapPoint, mMarkerSymbol);
        gLayerPos.getGraphics().add(graphic);
        if (isCenter) {
            mMapView.setViewpointCenterAsync(mapPoint, 600);
        }
        return mapPoint;
    }

    public static void addMapLocalTPK(MapView mMapView, String path) {
        if (new File(path).exists()) {
            TileCache mainTileCache = new TileCache(path);
            ArcGISTiledLayer layer = new ArcGISTiledLayer(mainTileCache);
            ArcGISMap arcGISMap = mMapView.getMap();
            arcGISMap.getBasemap().getReferenceLayers().add(layer);
            //将离线地图加载到MapView中
            mMapView.setMap(arcGISMap);
        }
    }

    public static void addMapLocalVTPK(MapView mMapView, String vtpk) {
        if (new File(vtpk).exists()) {
            ArcGISVectorTiledLayer mVectorTiledLayer = new ArcGISVectorTiledLayer(vtpk);
            ArcGISMap arcGISMap = mMapView.getMap();
            if (arcGISMap == null) {
                arcGISMap = new ArcGISMap(new Basemap(mVectorTiledLayer));
            } else {
                LayerList layers = arcGISMap.getBasemap().getBaseLayers();
                layers.add(layers.size(), mVectorTiledLayer);
            }
            Viewpoint vp = new Viewpoint(27.6699, 111.8236, 8000000);
            arcGISMap.setInitialViewpoint(vp);
            //将离线地图加载到MapView中
            mMapView.setMap(arcGISMap);
        }
    }

    public static void addMapLocalMMPK(final MapView mMapView, String mmpk) {
        final MobileMapPackage mainMobileMapPackage = new MobileMapPackage(mmpk);
        mainMobileMapPackage.loadAsync();
        mainMobileMapPackage.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                LoadStatus mainLoadStatus = mainMobileMapPackage.getLoadStatus();
                if (mainLoadStatus == LoadStatus.LOADED) {
                    List<ArcGISMap> mainArcGISMapL = mainMobileMapPackage.getMaps();
                    ArcGISMap mainArcGISMap = mainArcGISMapL.get(0);
                    mMapView.setMap(mainArcGISMap);
                }
            }
        });
    }

    public static void addMapLocalMMPK(final Context mContext, final MapView mMapView, String mmpk) {
        final MobileMapPackage mMobileMapPackage = new MobileMapPackage(mmpk);
        final ArrayList<MapPreview> mMapPreviews = new ArrayList<>();
        mMobileMapPackage.loadAsync();
        mMobileMapPackage.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (mMobileMapPackage.getLoadStatus() == LoadStatus.LOADED &&
                        mMobileMapPackage.getMaps().size() > 0) {
                    LocatorTask mLocatorTask = mMobileMapPackage.getLocatorTask();
                    //default to display of first map in package
                    ArcGISMap map = mMobileMapPackage.getMaps().get(0);
                    RouteTask mRouteTask;
                    RouteParameters mRouteParameters;
                    //if map contains transport network setup route task
                    if (map.getTransportationNetworks().size() > 0) {
                        mRouteTask = new RouteTask(mContext, map.getTransportationNetworks().get(0));
                        try {
                            mRouteParameters = mRouteTask.createDefaultParametersAsync().get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //only allow routing on map with transport networks
                        mRouteTask = null;
                    }
                    mMapView.setMap(map);
                    String mMMPkTitle = mMobileMapPackage.getItem().getTitle();
                    //for each map in the mobile map package, pull out relevant thumbnail information
                    for (int i = 0; i < mMobileMapPackage.getMaps().size(); i++) {
                        ArcGISMap currMap = mMobileMapPackage.getMaps().get(i);
                        final MapPreview mapPreview = new MapPreview();
                        //set map number
                        mapPreview.setMapNum(i);
                        //set map title. If null use the index of the list of maps to name each map Map #
                        if (currMap.getItem() != null && currMap.getItem().getTitle() != null) {
                            mapPreview.setTitle(currMap.getItem().getTitle());
                        } else {
                            mapPreview.setTitle("Map " + i);
                        }
                        //set map description. If null use package description instead
                        if (currMap.getItem() != null && currMap.getItem().getDescription() != null) {
                            mapPreview.setDesc(currMap.getItem().getDescription());
                        } else {
                            mapPreview.setDesc(mMobileMapPackage.getItem().getDescription());
                        }
                        //check if map has transport data
                        if (currMap.getTransportationNetworks().size() > 0) {
                            mapPreview.setTransportNetwork(true);
                        }
                        //check if map has geocoding data
                        if (mMobileMapPackage.getLocatorTask() != null) {
                            mapPreview.setGeocoding(true);
                        }
                        //set map preview thumbnail
                        final ListenableFuture<byte[]> thumbnailAsync;
                        if (currMap.getItem() != null && currMap.getItem().fetchThumbnailAsync() != null) {
                            thumbnailAsync = currMap.getItem().fetchThumbnailAsync();
                        } else {
                            thumbnailAsync = mMobileMapPackage.getItem().fetchThumbnailAsync();
                        }
                        thumbnailAsync.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                if (thumbnailAsync.isDone()) {
                                    try {
                                        mapPreview.setThumbnailByteStream(thumbnailAsync.get());
                                        mMapPreviews.add(mapPreview);
                                    } catch (InterruptedException | ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } else {
                    //log an issue if the mobile map package fails to load
                    Log.e("TAG", mMobileMapPackage.getLoadError().getMessage());
                }
            }
        });
    }

    public static void getPointLine(List<String> shapes, MapView mMapView, GraphicsOverlay gLayerRoadHistory, GraphicsOverlay gLayerRoadHistoryLine, PictureMarkerSymbol mMarkerSymbolHistory, boolean isGPS) {
        gLayerRoadHistory.getGraphics().clear();
        gLayerRoadHistoryLine.getGraphics().clear();
        if (shapes != null) {
            for (String ldModel : shapes) {
                if (!RxDataTool.isNullString(ldModel)) {
                    String shape = ldModel.substring(11, ldModel.length() - 1);
                    if (!RxDataTool.isNullString(shape)) {
                        String[] arrayShape = shape.split(",");
                        PolylineBuilder lineGeometry = new PolylineBuilder(SpatialReference.create(4326)/*SpatialReferences.getWebMercator()*/);
                        for (int i = 0; i < arrayShape.length; i++) {
                            String str = arrayShape[i];
                            if (!RxDataTool.isNullString(str)) {
                                String[] arrayPoint = str.split(" ");
                                Gps gps = getGps(RxDataTool.stringToDouble(arrayPoint[0]), RxDataTool.stringToDouble(arrayPoint[1]), isGPS);
                                if (gps != null) {
                                    //经度
                                    double locx = gps.getLongitude();
                                    //纬度
                                    double locy = gps.getLatitude();
                                    Point mapPoint = new Point(locx, locy/*, SpatialReference.create(4326)*/);
                                    //定位到所在位置
                                    if (i == 0) {
                                        markLocation(mMapView, RxDataTool.stringToDouble(arrayPoint[0]), RxDataTool.stringToDouble(arrayPoint[1]), gLayerRoadHistory, mMarkerSymbolHistory, isGPS, false);
                                        lineGeometry.addPoint(mapPoint);
                                    } else {
                                        if (i == arrayShape.length - 1) {
                                            lineGeometry.addPoint(mapPoint);
                                            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xCC52BA97, 3);
                                            Graphic lineGraphic = new Graphic(lineGeometry.toGeometry());
                                            SimpleRenderer lineRenderer = new SimpleRenderer(lineSymbol);
                                            gLayerRoadHistoryLine.setRenderer(lineRenderer);
                                            gLayerRoadHistoryLine.getGraphics().add(lineGraphic);
                                            markLocation(mMapView, RxDataTool.stringToDouble(arrayPoint[0]), RxDataTool.stringToDouble(arrayPoint[1]), gLayerRoadHistory, mMarkerSymbolHistory, isGPS, false);
                                        } else {
                                            lineGeometry.addPoint(mapPoint);
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
    public static double lengthPolyline(Polyline pline) {
        return GeometryEngine.length(pline);
    }

    public static void initPointLine(String shapeStr, List<Gps> gpsList, GraphicsOverlay gLayerPos, PictureMarkerSymbol orginLocationSymbol, PictureMarkerSymbol finalLocationSymbol, PictureMarkerSymbol locationSymbol, GraphicsOverlay gLayerPosLine, MapView mMapView) {
        gLayerPosLine.getGraphics().clear();
        gLayerPos.getGraphics().clear();

        if (!RxDataTool.isNullString(shapeStr)) {
            String shape = shapeStr;
            if (shapeStr.contains("LINESTRING")) {
                shape = shapeStr.substring(12, shapeStr.length() - 1);
            }
            if (!RxDataTool.isNullString(shape)) {
                String[] arrayShape = shape.split(", ");

                String[] originShape = arrayShape[0].split(" ");
                Gps originGps = getGps(RxDataTool.stringToDouble(originShape[0]), RxDataTool.stringToDouble(originShape[1]), true);
                //定位到所在位置
                Point mapOriginPoint = new Point(originGps.getLongitude(), originGps.getLatitude(), SpatialReference.create(4326));
                Graphic graphic = new Graphic(mapOriginPoint, orginLocationSymbol);
                gLayerPos.getGraphics().add(graphic);

                String[] finalShape = arrayShape[arrayShape.length - 1].split(" ");
                Gps finalGps = getGps(RxDataTool.stringToDouble(finalShape[0]), RxDataTool.stringToDouble(finalShape[1]), true);
                //定位到所在位置
                Point mapFinalPoint = new Point(finalGps.getLongitude(), finalGps.getLatitude(), SpatialReference.create(4326));
                Graphic graphicFinal = new Graphic(mapFinalPoint, finalLocationSymbol);
                gLayerPos.getGraphics().add(graphicFinal);

                String shapes = arrayShape[arrayShape.length / 2];
                String[] currentPoint = shapes.split(" ");
                Gps currentGps = getGps(RxDataTool.stringToDouble(currentPoint[0]), RxDataTool.stringToDouble(currentPoint[1]), true);

                Point currentMapPoint = new Point(currentGps.getLongitude(), currentGps.getLatitude(), SpatialReference.create(4326));
                mMapView.setViewpointCenterAsync(currentMapPoint, 60000);

                PolylineBuilder lineGeometry = new PolylineBuilder(SpatialReference.create(4326));
                for (int i = 0; i < arrayShape.length; i++) {
                    String str = arrayShape[i];
                    if (!RxDataTool.isNullString(str)) {
                        String[] arrayPoint = str.split(" ");
                        Gps gps = getGps(RxDataTool.stringToDouble(arrayPoint[0]), RxDataTool.stringToDouble(arrayPoint[1]), true);
                        if (gps != null) {
                            //经度
                            double locx = gps.getLongitude();
                            //纬度
                            double locy = gps.getLatitude();
                            Point mapPoint = new Point(locx, locy);
                            if (i == 0) {
                                lineGeometry.addPoint(mapPoint);
                            } else {
                                if (i == arrayShape.length - 1) {
                                    lineGeometry.addPoint(mapPoint);
                                    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xCC2196F3, 5);
                                    Graphic lineGraphic = new Graphic(lineGeometry.toGeometry());
                                    SimpleRenderer lineRenderer = new SimpleRenderer(lineSymbol);
                                    gLayerPosLine.setRenderer(lineRenderer);
                                    gLayerPosLine.getGraphics().add(lineGraphic);
                                } else {
                                    lineGeometry.addPoint(mapPoint);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (gpsList != null) {
            for (Gps gps : gpsList) {
                //定位到所在位置
                Point mapPoint = new Point(gps.getLongitude(), gps.getLatitude(), SpatialReference.create(4326));
                Graphic graphic = new Graphic(mapPoint, locationSymbol);
                gLayerPos.getGraphics().add(graphic);
            }
        }

    }
}
