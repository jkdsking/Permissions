package com.jkds.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.jkds.permissions.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PermissionRequest.getInstance().build(MainActivity.this).requestPermission(new PermissionRequest.PermissionListener() {
//                    @Override
//                    public void permissionGranted() {
//                        Toast.makeText(MainActivity.this, "权限已申请通过", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void permissionDenied(ArrayList<String> permissions) {
//                        Toast.makeText(MainActivity.this, "获取拒绝", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void permissionNeverAsk(ArrayList<String> permissions) {
//                        Toast.makeText(MainActivity.this, "拒绝并不再提示", Toast.LENGTH_SHORT).show();
//                    }
//                }, new String[]{Manifest.permission.CAMERA});


                XXPermissions.with(MainActivity.this)
                        .permission(Permission.CAMERA)
                        .request(new OnPermission() {

                            @Override
                            public void hasPermission(List<String> granted, boolean all) {
                                if (all) {
                                    toast("获取拍照权限成功");
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean never) {
                                if (never) {
                                    toast("被永久拒绝授权，请手动授予拍照权限");
                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.startPermissionActivity(MainActivity.this, denied);
                                } else {
                                    toast("获取拍照权限失败");
                                }
                            }
                        });




            }
        });

        findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PermissionRequest.getInstance().build(MainActivity.this).requestPermission(new PermissionRequest.PermissionListener() {
//                    @Override
//                    public void permissionGranted() {
//                        Toast.makeText(MainActivity.this, "权限已全部申请通过", Toast.LENGTH_SHORT).show();
//                    }
//                    @Override
//                    public void permissionDenied(ArrayList<String> permissions) {
//                        Toast.makeText(MainActivity.this, "获取拒绝", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void permissionNeverAsk(ArrayList<String> permissions) {
//                        Toast.makeText(MainActivity.this, "拒绝并不再提示", Toast.LENGTH_SHORT).show();
//                    }
//                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO});




                XXPermissions.with(MainActivity.this)
                        .permission(Permission.RECORD_AUDIO)
                        // 申请多个权限
                        .permission(Permission.Group.CALENDAR)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean all) {
                                if (all) {
                                    toast("获取录音和日历权限成功");
                                } else {
                                    toast("获取部分权限成功，但部分权限未正常授予"+granted.size());
                                }
                            }
                            @Override
                            public void noPermission(List<String> denied, boolean never) {
                                if (never) {
                                    toast("被永久拒绝授权，请手动授予录音和日历权限");
                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.startPermissionActivity(MainActivity.this, denied);
                                } else {
                                    toast("获取录音和日历权限失败");
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.hasPermission(this, Permission.RECORD_AUDIO) &&
                    XXPermissions.hasPermission(this, Permission.Group.CALENDAR)) {
                toast("用户已经在权限设置页授予了录音和日历权限");
            }
        }
    }

    public void toast(CharSequence text) {
       Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
    }
}