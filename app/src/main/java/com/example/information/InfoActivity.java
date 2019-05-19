package com.example.information;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.List;

public class InfoActivity extends AppCompatActivity {

    TextView txtImei1;
    TextView txtModel;
    TextView verAndroid;
    TextView ip;
    TextView nameOperator;
    TextView screenZize;
    TextView batteryCapacity;
    TextView batteryLevel;
    String phoneModel;
    String androidVersion;
    String imei;
    Integer countsim;
    TelephonyManager telephonyManager;
    final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            countsim = (Integer) arguments.get("countsim");
        }
        setContentView(R.layout.activity_info);
        txtImei1 = findViewById(R.id.txt_imei1);
        if (countsim != null) {
            if (countsim == 1) {
                txtImei1.setText(getString(R.string.txt_imei1, getIMEI(0)));
            } else if (countsim == 2) {
                txtImei1.setText(getString(R.string.txt_imei2, getIMEI(0), getIMEI(1)));
            } else if (countsim == 3) {
                txtImei1.setText(getString(R.string.txt_imei3, getIMEI(0), getIMEI(1), getIMEI(2)));
            }
        }
        txtModel = findViewById(R.id.txt_model);
        verAndroid = findViewById(R.id.txt_ver_android);
        ip = findViewById(R.id.txt_ip);
        nameOperator = findViewById(R.id.txt_name_operator1);
        screenZize = findViewById(R.id.txt_screen_size);
        batteryCapacity = findViewById(R.id.txt_battery_capacity);
        batteryLevel = findViewById(R.id.txt_battery_level);
        getInfoDetails();
        txtModel.setText("Model phone: " + phoneModel);
        getverAndroid();
        verAndroid.setText("Android version: " + androidVersion);
        getIP();
        getNameOperator();
        screenZize.setText(String.valueOf(screenSize()));
        batteryCapacity.setText(getBatteryCapacity().toString());
        batteryLevel.setText(getBatteryLevel().toString());
    }

    private String getIMEI(int i) {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkSelfPermissions();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return checkSelfPermissions().toString();
            }
            imei = telephonyManager.getImei(i);
        }
        return imei;
    }

    private Boolean checkSelfPermissions() {
        boolean result = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            result = false;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            result = false;
        }
        return result;
    }

    private String getInfoDetails() {
        return phoneModel = android.os.Build.MODEL;
    }

    private String getverAndroid() {
        return androidVersion = android.os.Build.VERSION.RELEASE;
    }

    private void getIP() {
        WifiManager wifiMan = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        ip.setText("IP: " + String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
    }

    private void getNameOperator() {
        String carrierName = null;
        String mobileNo = null;
        String countyIso = null;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkSelfPermissions();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return ;
            }
            List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

            if(subscriptionInfoList!=null && subscriptionInfoList.size()>0){
                for(SubscriptionInfo info:subscriptionInfoList){
                    carrierName = info.getCarrierName().toString();
                    mobileNo = info.getNumber();
                    countyIso = info.getCountryIso();
                    int dataRoaming = info.getDataRoaming();
                }
                nameOperator.setText("SIM №1: " + telephonyManager.getNetworkOperatorName() + "\n" + "SIM №2: " + carrierName);
            }
        }

    }

    public Double getBatteryCapacity() {

        Object powerProfile = null;
        Double batteryCapacity = 0.0;

        try {
            powerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            batteryCapacity = (Double) Class.forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(powerProfile, "battery.capacity");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;
    }

    public Integer getBatteryLevel() {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) ((level / (float) scale) * 100.0f);

        return batteryPct;
    }

    public double screenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);

        return truncate(screenInches, 1);
    }

    public static double truncate(double c, int n) {
        int d = (int) Math.pow(10, n);
        double r = (Math.rint(c * d) / d);
        return r;
    }


}
