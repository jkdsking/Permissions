package com.jkds.permission;


import android.app.Fragment;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * @author 王金珂
 * @date 2020/7/4
 */
public class PermissionFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_CODE = 0;
    private PermissionRequest.PermissionListener listener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, PermissionRequest.PermissionListener listener) {
        this.listener = listener;
        if(!PermissionUtil.hasPermission(this.getContext(),permissions)) {
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
        }else {
            listener.permissionGranted();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return;
        }
        if(listener!=null) {
            if (PermissionUtil.hasPermission(getContext(), permissions)) {
                listener.permissionGranted();
            } else {
                if (PermissionUtil.isNeverAsk(getContext(), permissions)) {
                    listener.permissionNeverAsk(PermissionUtil.getNeverAskPermissions(getContext(), permissions));
                } else {
                    listener.permissionDenied(PermissionUtil.getDeniedPermissions(getContext(), permissions));
                }
            }
        }
    }
}
