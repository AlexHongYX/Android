package com.ss.android.application.app.debug.location;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bytedance.bdlocation.BDLocation;
import com.bytedance.bdlocation.ILocate;
import com.bytedance.bdlocation.client.BDLocationClient;
import com.bytedance.bdlocation.client.BDLocationConfig;
import com.bytedance.bdlocation.client.BDLocationException;
import com.bytedance.bdlocation.client.BDPoint;
import com.bytedance.bdlocation.client.LocationOption;
import com.bytedance.i18n.business.debug.R;
import com.ss.alog.middleware.ALogService;
import com.ss.android.framework.page.slideback.AbsSlideBackActivity;


import java.util.Locale;

public class LocationTestMainActivity extends AbsSlideBackActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private RadioGroup rgLocationMode;
    private EditText etInterval;
    private EditText etHttpTimeout;
    private CheckBox cbOnceLocation;
    private CheckBox cbAddress;
    private CheckBox cbGpsFirst;
    private CheckBox cbCacheAble;
    private CheckBox cbOnceLastest;
    private CheckBox cbSensorAble;
    private TextView tvResult;
    private Button btLocation;
    private RadioGroup rgGeoLanguage;

    private BDLocationClient client = new BDLocationClient("main_test");
    @LocationOption.LocationMode
    private int mode = LocationOption.Hight_Accuracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
//                BdLBSResult result = client.getBdLBSResult(false);
//                ALogService.dSafely("bd", result.toString());
                BDLocation location = client.geocode(new BDPoint(13.969144, 108.013204), ILocate.WGS_84);
                if (location != null) {
                    ALogService.dSafely("bd", location.toString());
                }

            }
        }).start();
    }

    private void initView(){
        rgLocationMode = (RadioGroup) findViewById(R.id.rg_locationMode);

        etInterval = (EditText) findViewById(R.id.et_interval);
        etHttpTimeout = (EditText) findViewById(R.id.et_httpTimeout);

        cbOnceLocation = (CheckBox)findViewById(R.id.cb_onceLocation);
        cbGpsFirst = (CheckBox) findViewById(R.id.cb_gpsFirst);
        cbAddress = (CheckBox) findViewById(R.id.cb_needAddress);
        cbCacheAble = (CheckBox) findViewById(R.id.cb_cacheAble);
        cbOnceLastest = (CheckBox) findViewById(R.id.cb_onceLastest);
        cbSensorAble = (CheckBox)findViewById(R.id.cb_sensorAble);

        tvResult = (TextView) findViewById(R.id.tv_result);
        btLocation = (Button) findViewById(R.id.bt_location);

        rgGeoLanguage = (RadioGroup) findViewById(R.id.rg_language);

        rgLocationMode.setOnCheckedChangeListener(this);
        btLocation.setOnClickListener(this);
        rgGeoLanguage.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!PermissionsManager.getInstance().hasPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) ||
//                !PermissionsManager.getInstance().hasPermission(this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE) ||
//                !PermissionsManager.getInstance().hasPermission(this,
//                        Manifest.permission.READ_PHONE_STATE)) {
//            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(
//                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_PHONE_STATE},
//                    new PermissionsResultAction() {
//                        @Override
//                        public void onGranted() {
//                        }
//
//                        @Override
//                        public void onDenied(String permission) {
//                            ToastUtil.toast(
//                                    "不给就算了", Toast.LENGTH_SHORT);
//                        }
//                    });
//        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_location) {
            if (!cbOnceLocation.isChecked()) {
                if (btLocation.getText().equals("开始定位")) {
                    btLocation.setText("定位停止");
                    tvResult.setText("正在定位...");
                    startLocation();
                } else {
                    btLocation.setText("开始定位");
                    stopLocation();
                    tvResult.setText("定位停止");
                }
            } else {
                if (btLocation.getText().equals("开始定位")) {
                    startLocation();
                    tvResult.setText("正在定位...");
                }
            }
        }
    }

    private void startLocation() {
        resetOption();
        if (cbOnceLocation.isChecked()) {
            client.getLocation(new BDLocationClient.Callback() {
                @Override
                public void onLocationChanged(@Nullable final BDLocation location) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            showResult(location, null);
                        }
                    });
                }

                @Override
                public void onError(BDLocationException t) {
                    showResult(null, t);
                }
            });
        } else {
            client.startLocation(new BDLocationClient.Callback() {
                @Override
                public void onLocationChanged(@Nullable final BDLocation location) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            showResult(location, null);
                        }
                    });
                }

                @Override
                public void onError(BDLocationException t) {
                    showResult(null, t);
                }
            });
        }
    }

    private void stopLocation() {
        client.stopLocation();
    }

    private void showResult(BDLocation location, Exception e) {
            StringBuffer sb = new StringBuffer();
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if(location != null){
                sb.append("定位成功" + "\n");
//                sb.append("定位类型: " + location.getLocationType() + "\n");
                sb.append("经    度    : " + location.getLongitude() + "\n");
                sb.append("纬    度    : " + location.getLatitude() + "\n");
                sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                sb.append("提供者    : " + location.getProvider() + "\n");
                sb.append("定位类型  : " + location.getLocationType() + "\n");
                sb.append("定位内核  : " + location.getLocationSDKName() + "\n");

                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getAdministrativeArea() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoi() + "\n");
                //定位完成的时间
                sb.append("定位时间: " + Util.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
            } else {
                //定位失败
                sb.append("定位失败" + "\n");
//                sb.append("错误码:" + location.getErrorCode() + "\n");
                sb.append("错误信息:" + e.getMessage() + "\n");
//                sb.append("错误描述:" + location.getLocationDetail() + "\n");
            }
//            sb.append("***定位质量报告***").append("\n");
//            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
//            sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
//            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
//            sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
//            sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
//            sb.append("****************").append("\n");
            //定位之后的回调时间
//            sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

            //解析定位结果，
            String result = sb.toString();
            tvResult.setText(result);
    }

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        client.reset();
        // 设置是否需要显示地址信息
        if (!cbCacheAble.isChecked()) {
            client.setMaxCacheTime(0);
        } else {
            client.setMaxCacheTime(10 * 60 * 1000);
        }

        client.setLocationMode(mode);

        String strInterval = etInterval.getText().toString();
        if (!TextUtils.isEmpty(strInterval)) {
            try{
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                client.setLocationInterval(Long.valueOf(strInterval));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }

        String strTimeout = etHttpTimeout.getText().toString();
        if(!TextUtils.isEmpty(strTimeout)){
            try{
                // 设置网络请求超时时间
                client.setLocationTimeOut(Long.valueOf(strTimeout));
            }catch(Throwable e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(group == rgLocationMode) {
            if (checkedId == R.id.rb_batterySaving) {
                mode = LocationOption.Battery_Saving;
            } else if (checkedId == R.id.rb_deviceSensors) {
                mode = LocationOption.Device_Sensors;
            } else if (checkedId == R.id.rb_hightAccuracy) {
                mode = LocationOption.Hight_Accuracy;
            }
        }

        if(group == rgGeoLanguage){
            if (checkedId == R.id.rb_languageDefault) {
                BDLocationConfig.setLocale(Locale.getDefault());
            } else if (checkedId == R.id.rb_languageEN) {
                BDLocationConfig.setLocale(Locale.US);
                BDLocationConfig.setChineseChannel(false);
            } else if (checkedId == R.id.rb_languageZH) {
                BDLocationConfig.setLocale(Locale.CHINA);
                BDLocationConfig.setChineseChannel(true);
            }
        }

    }
}
