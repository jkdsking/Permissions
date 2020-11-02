package com.jkds.permissions;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jkds.permission.OnPermission;
import com.jkds.permission.PermissionsRequest;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionsRequest.with(MainActivity.this)
                        .permission(Manifest.permission.CAMERA)
                        .request(new OnPermission() {

                            @Override
                            public void hasPermission(List<String> granted, boolean all) {
                                if (all) {
                                    toast("权限已申请通过");
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean never) {
                                if (never) {
                                    toast("权限已拒绝，并不再提示");
                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                    PermissionsRequest.startPermissionActivity(MainActivity.this, denied);
                                } else {
                                    toast("权限已拒绝");
                                }
                            }
                        });




            }
        });

        findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PermissionsRequest.with(MainActivity.this)


                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                        // 申请多个权限
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean all) {
                                if (all) {
                                    toast("权限已全部申请通过");
                                } else {
                                    toast("获取部分权限成功，但部分权限未正常授予"+granted.size());
                                }
                            }
                            @Override
                            public void noPermission(List<String> denied, boolean never) {
                                if (never) {
                                    toast("权限已被拒绝并不再提示");
                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                    PermissionsRequest.startPermissionActivity(MainActivity.this, denied);
                                } else {
                                    toast("权限已拒绝");
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsRequest.REQUEST_CODE) {
            if (PermissionsRequest.hasPermission(this,Manifest.permission.CAMERA)) {
                toast("用户已经在权限设置页授予了权限");
            }
        }
    }

    public void toast(CharSequence text) {
       Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
    }
}