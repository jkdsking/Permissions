package com.jkds.permission;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;


import java.util.ArrayList;
import java.util.List;
/**
 * @author wjk
 * @date 2020/11/2
 */

@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.M)
public class PermissionFragment extends Fragment {
    /** 请求的权限 */
    private static final String PERMISSION_GROUP = "permission_group";

    /** 请求码（自动生成） */
    private static final String REQUEST_CODE = "request_code";

    /** 权限请求码存放集合 */
    private static SparseBooleanArray sRequestCodes = new SparseBooleanArray();

    /**
     * 开启权限申请
     */
    public static void beginRequest(Activity activity, ArrayList<String> permissions, OnPermission callback) {
        PermissionFragment fragment = new PermissionFragment(callback);
        Bundle bundle = new Bundle();
        int requestCode;
        // 请求码随机生成，避免随机产生之前的请求码，必须进行循环判断
        do {
            requestCode = PermissionUtil.getRandomRequestCode();
        } while (sRequestCodes.get(requestCode));
        // 标记这个请求码已经被占用
        sRequestCodes.put(requestCode, true);
        bundle.putInt(REQUEST_CODE, requestCode);
        bundle.putStringArrayList(PERMISSION_GROUP, permissions);
        fragment.setArguments(bundle);
        // 设置保留实例，不会因为配置变化而重新创建
        fragment.setRetainInstance(true);
        addFragment(activity.getFragmentManager(), fragment);
    }

    /**
     * 添加 Fragment
     */
    public static void addFragment(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction().add(fragment, fragment.toString()).commitAllowingStateLoss();
    }

    /**
     * 移除 Fragment
     */
    public static void removeFragment(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    /** 是否申请了特殊权限 */
    private boolean mSpecialRequest;

    /** 是否申请了危险权限 */
    private boolean mDangerousRequest;

    /** 权限回调对象 */
    private OnPermission mCallBack;

    public PermissionFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public PermissionFragment(OnPermission callback) {
        super();
        mCallBack = callback;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消引用监听器，避免内存泄漏
        mCallBack = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 如果在 Activity 不可见的状态下添加 Fragment 并且去申请权限会导致授权对话框显示不出来
        // 所以必须要在 Fragment 的 onResume 来申请权限，这样就可以保证应用回到前台的时候才去申请权限
        if (mSpecialRequest) {
            return;
        }

        mSpecialRequest = true;
        if (mCallBack == null) {
            removeFragment(getFragmentManager(), this);
            return;
        }
        requestSpecialPermission();
    }

    /**
     * 申请危险权限
     */
    public void requestDangerousPermission() {
        final ArrayList<String> allPermissions = getArguments().getStringArrayList(PERMISSION_GROUP);
        if (allPermissions == null || allPermissions.size() == 0) {
            return;
        }

        ArrayList<String> locationPermission = null;
        // Android 11 定位策略发生改变，申请后台定位权限的前提是要有前台定位权限（授予了精确或者模糊任一权限）
        if (PermissionUtil.isAndroid11() && allPermissions.contains(Permission.ACCESS_BACKGROUND_LOCATION)) {
            locationPermission = new ArrayList<>();
            if (allPermissions.contains(Permission.ACCESS_COARSE_LOCATION) &&
                    !PermissionUtil.isPermissionGranted(getActivity(), Permission.ACCESS_COARSE_LOCATION)) {
                locationPermission.add(Permission.ACCESS_COARSE_LOCATION);
            }

            if (allPermissions.contains(Permission.ACCESS_FINE_LOCATION) &&
                    !PermissionUtil.isPermissionGranted(getActivity(), Permission.ACCESS_FINE_LOCATION)) {
                locationPermission.add(Permission.ACCESS_FINE_LOCATION);
            }
        }

        // 如果不需要申请前台定位权限就直接申请危险权限
        if (locationPermission == null || locationPermission.isEmpty()) {
            requestPermissions(allPermissions.toArray(new String[allPermissions.size() - 1]), getArguments().getInt(REQUEST_CODE));
            return;
        }

        // 先申请前台定位权限，再申请后台定位权限
        PermissionFragment.beginRequest(getActivity(), locationPermission, new OnPermission() {

            @Override
            public void hasPermission(List<String> granted, boolean all) {
                if (all && isAdded()) {
                    requestPermissions(allPermissions.toArray(new String[allPermissions.size() - 1]), getArguments().getInt(REQUEST_CODE));
                }
            }

            @Override
            public void noPermission(List<String> denied, boolean never) {
                requestPermissions(allPermissions.toArray(new String[allPermissions.size() - 1]), getArguments().getInt(REQUEST_CODE));
            }
        });
    }

    /**
     * 申请特殊权限
     */
    public void requestSpecialPermission() {
        List<String> permissions = getArguments().getStringArrayList(PERMISSION_GROUP);

        // 是否需要申请特殊权限
        boolean requestSpecialPermission = false;

        // 判断当前是否包含特殊权限
        if (PermissionUtil.containsSpecialPermission(permissions)) {

            if (permissions.contains(Permission.MANAGE_EXTERNAL_STORAGE) && !PermissionUtil.hasStoragePermission(getActivity())) {
                // 当前必须是 Android 11 及以上版本，因为 hasStoragePermission 在旧版本上是拿旧权限做的判断，所以这里需要多判断一次版本
                if (PermissionUtil.isAndroid11()) {
                    // 跳转到存储权限设置界面
                    startActivityForResult(PermissionSettingPage.getStoragePermissionIntent(getActivity()), getArguments().getInt(REQUEST_CODE));
                    requestSpecialPermission = true;
                }
            }

            if (permissions.contains(Permission.REQUEST_INSTALL_PACKAGES) && !PermissionUtil.hasInstallPermission(getActivity())) {
                // 跳转到安装权限设置界面
                startActivityForResult(PermissionSettingPage.getInstallPermissionIntent(getActivity()), getArguments().getInt(REQUEST_CODE));
                requestSpecialPermission = true;
            }

            if (permissions.contains(Permission.SYSTEM_ALERT_WINDOW) && !PermissionUtil.hasWindowPermission(getActivity())) {
                // 跳转到悬浮窗设置页面
                startActivityForResult(PermissionSettingPage.getWindowPermissionIntent(getActivity()), getArguments().getInt(REQUEST_CODE));
                requestSpecialPermission = true;
            }

            if (permissions.contains(Permission.NOTIFICATION_SERVICE) && !PermissionUtil.hasNotifyPermission(getActivity())) {
                // 跳转到通知栏权限设置页面
                startActivityForResult(PermissionSettingPage.getNotifyPermissionIntent(getActivity()), getArguments().getInt(REQUEST_CODE));
                requestSpecialPermission = true;
            }

            if (permissions.contains(Permission.WRITE_SETTINGS) && !PermissionUtil.hasSettingPermission(getActivity())) {
                // 跳转到系统设置权限设置页面
                startActivityForResult(PermissionSettingPage.getSettingPermissionIntent(getActivity()), getArguments().getInt(REQUEST_CODE));
                requestSpecialPermission = true;
            }
        }

        // 当前必须没有跳转到悬浮窗或者安装权限界面
        if (!requestSpecialPermission) {
            requestDangerousPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != getArguments().getInt(REQUEST_CODE)) {
            return;
        }

        OnPermission callBack = mCallBack;
        mCallBack = null;

        // 根据请求码取出的对象为空，就直接返回不处理
        if (callBack == null) {
            return;
        }

        for (int i = 0; i < permissions.length; i++) {

            String permission = permissions[i];

            if (PermissionUtil.isSpecialPermission(permission)) {
                // 如果这个权限是特殊权限，那么就重新进行权限检测
                grantResults[i] = PermissionUtil.getPermissionStatus(getActivity(), permission);
                continue;
            }

            if (PermissionUtil.isAndroid11() && Permission.ACCESS_BACKGROUND_LOCATION.equals(permission)) {
                // 这个权限是后台定位权限并且当前手机版本是 Android 11 及以上，那么就需要重新进行检测
                // 因为只要申请这个后台定位权限，grantResults 数组总对这个权限申请的结果返回 -1（拒绝）
                grantResults[i] = PermissionUtil.getPermissionStatus(getActivity(), permission);
                continue;
            }

            if (!PermissionUtil.isAndroid10()) {
                // 重新检查 Android 10.0 的三个新权限
                if (Permission.ACCESS_BACKGROUND_LOCATION.equals(permission) ||
                        Permission.ACTIVITY_RECOGNITION.equals(permission) ||
                        Permission.ACCESS_MEDIA_LOCATION.equals(permission)) {
                    // 如果当前版本不符合最低要求，那么就重新进行权限检测
                    grantResults[i] = PermissionUtil.getPermissionStatus(getActivity(), permission);
                    continue;
                }
            }

            if (!PermissionUtil.isAndroid8()) {
                // 重新检查 Android 8.0 的两个新权限
                if (Permission.ANSWER_PHONE_CALLS.equals(permission) ||
                        Permission.READ_PHONE_NUMBERS.equals(permission)) {
                    // 如果当前版本不符合最低要求，那么就重新进行权限检测
                    grantResults[i] = PermissionUtil.getPermissionStatus(getActivity(), permission);
                }
            }
        }

        // 释放对这个请求码的占用
        sRequestCodes.delete(requestCode);
        // 将 Fragment 从 Activity 移除
        removeFragment(getFragmentManager(), this);

        // 获取已授予的权限
        List<String> grantedPermission = PermissionUtil.getGrantedPermission(permissions, grantResults);

        // 如果请求成功的权限集合大小和请求的数组一样大时证明权限已经全部授予
        if (grantedPermission.size() == permissions.length) {
            // 代表申请的所有的权限都授予了
            callBack.hasPermission(grantedPermission, true);
            return;
        }

        // 获取被拒绝的权限
        List<String> deniedPermission = PermissionUtil.getDeniedPermission(permissions, grantResults);

        // 代表申请的权限中有不同意授予的，如果有某个权限被永久拒绝就返回 true 给开发人员，让开发者引导用户去设置界面开启权限
        callBack.noPermission(deniedPermission, PermissionUtil.isPermissionPermanentDenied(getActivity(), deniedPermission));

        // 证明还有一部分权限被成功授予，回调成功接口
        if (!grantedPermission.isEmpty()) {
            callBack.hasPermission(grantedPermission, false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mDangerousRequest && requestCode == getArguments().getInt(REQUEST_CODE)) {
            mDangerousRequest = true;
            // 需要延迟执行，不然有些华为机型授权了但是获取不到权限
            getActivity().getWindow().getDecorView().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 如果用户离开太久，会导致 Activity 被回收掉
                    // 所以这里要判断当前 Fragment 是否有被添加到 Activity
                    // 可在开发者模式中开启不保留活动来复现这个 Bug
                    if (isAdded()) {
                        // 请求其他危险权限
                        requestDangerousPermission();
                    }
                }
            }, 300);
        }
    }
}
