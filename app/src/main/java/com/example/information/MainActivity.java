package com.example.information;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnDetailedInfo;
    Button btnWiFi;
    Button btnShell;
    Integer serviceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_button_info);

        btnDetailedInfo = findViewById(R.id.btn_info_details);
        btnWiFi = findViewById(R.id.btn_wifi);
        btnShell = findViewById(R.id.btn_shell);
        checkSim();
        if(serviceState != null){
        if(serviceState == 1){
            Toast.makeText(this, "У вас односимочный телефон", Toast.LENGTH_SHORT).show();
        } else if(serviceState == 2){
            Toast.makeText(this, "У вас двухсимочный телефон", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "У вас трехсимочный телефон", Toast.LENGTH_SHORT).show();
        }
        } else {
            Toast.makeText(this, "Не удалось определить количество симкарт", Toast.LENGTH_SHORT).show();
        }

        btnWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWiFi();
            }
        });

        btnDetailedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInfoDetails();
            }
        });

        btnShell.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                sendShell();
            }
        });
    }

    public void sendWiFi(){
        Intent intent = new Intent(this, WiFiActivity.class);
        startActivity(intent);
    }

    public void sendInfoDetails(){
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("countsim", serviceState);
        startActivity(intent);
    }

    public void sendShell(){
        Intent intent = new Intent(this, ShellActivity.class);
        startActivity(intent);
    }

    public Integer checkSim() {//TODO использовтаь метод для условия проверки под устройство у которого несколько сим карт
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkSelfPermissions();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return checkSelfPermissions().hashCode();
            }
            serviceState = telephonyManager.getPhoneCount();
        }
        return serviceState;
    }

    private Boolean checkSelfPermissions(){
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


}
