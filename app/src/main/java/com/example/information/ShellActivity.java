package com.example.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class ShellActivity extends AppCompatActivity {

    EditText edtCmd;
//    TextView txtCmdResult;
    Button sendShellCmd;
    String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        sendShellCmd = findViewById(R.id.btn_send_shell_cmd);
//        FrameLayout fragmentContainer = findViewById(R.id.timing_container);
//        txtCmdResult = findViewById(R.id.txt_cmd_result);
        sendShellCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShell();
                if (result == null || result == "") {
                    Toast.makeText(getApplicationContext(), "Нет результата", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.onActivityDataListener(result);
                }

//                txtCmdResult.setText(result);
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.timing_container);
        if (fragment == null) {
            fragment = new ResultFragment();

            if (fragment instanceof OnActivityDataListener) {
                mListener = (OnActivityDataListener) fragment;
            } else {
                throw new RuntimeException(fragment.toString()
                        + " must implement onActivityDataListener");
            }

            fragmentManager.beginTransaction()
                    .add(R.id.timing_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_cmd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_cmd){
            View btnCmd = findViewById(R.id.item_cmd);
            btnCmd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendActivityCmd();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface OnActivityDataListener {
        void onActivityDataListener(String string);
    }

    private OnActivityDataListener mListener;

    public void sendShell() {
        edtCmd = findViewById(R.id.edit_cmd);
        CPU_info(edtCmd.getText().toString());
    }

    public void sendActivityCmd(){
        Intent intent = new Intent(this, CmdActivity.class);
        startActivity(intent);
    }


    private String CPU_info(String shellCmd) {
        ProcessBuilder cmd;
        result = "";
        String[] args = {"/system/bin/cat", shellCmd};
        cmd = new ProcessBuilder(args);

        try {
            Process process = cmd.start();
            InputStream data = process.getInputStream();
            byte[] re = new byte[1024];
            while (data.read(re) != -1) {
                System.out.println(new String(re));
                result = result + new String(re);
            }
            data.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
