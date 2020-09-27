package com.jkds.permissions;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jkds.permission.PermissionRequest;
import com.jkds.permissions.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionRequest.getInstance().build(MainActivity.this).requestPermission(new PermissionRequest.PermissionListener() {
                    @Override
                    public void permissionGranted() {
                        Toast.makeText(MainActivity.this, "权限已申请通过", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void permissionDenied(ArrayList<String> permissions) {
                        Toast.makeText(MainActivity.this, "获取拒绝", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionNeverAsk(ArrayList<String> permissions) {
                        Toast.makeText(MainActivity.this, "拒绝并不再提示", Toast.LENGTH_SHORT).show();
                    }
                }, new String[]{Manifest.permission.CAMERA});

            }
        });

        findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionRequest.getInstance().build(MainActivity.this).requestPermission(new PermissionRequest.PermissionListener() {
                    @Override
                    public void permissionGranted() {
                        Toast.makeText(MainActivity.this, "权限已全部申请通过", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void permissionDenied(ArrayList<String> permissions) {
                        Toast.makeText(MainActivity.this, "获取拒绝", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionNeverAsk(ArrayList<String> permissions) {
                        Toast.makeText(MainActivity.this, "拒绝并不再提示", Toast.LENGTH_SHORT).show();
                    }
                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO});

            }
        });


    }
}