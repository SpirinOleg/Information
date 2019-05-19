package com.example.information;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class CmdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmd);

        if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.cmd_container, new FragmentCmd()).commit();
        }
    }
}
