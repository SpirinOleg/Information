package com.example.information;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class WiFiActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        TextView infoProcessor = findViewById(R.id.proc_info);
        infoProcessor.setText(cpuInfo());
    }

    private String cpuInfo() {
        String result = "";
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        try {
            if (wifiManager.isWifiEnabled()) {
                String[] args = {"cat", "/proc/net/arp"};
                ProcessBuilder cmd = new ProcessBuilder(args);
                Process process = cmd.start();
                InputStream data = process.getInputStream();
                byte[] re = new byte[1024];
                while (data.read(re) != -1) {
                    System.out.print(new String(re));
                    result = result + new String(re);
                }
                data.close();
            } else {
                Toast.makeText(this, "Wi-Fi Выключен!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
