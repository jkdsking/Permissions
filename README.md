##  Android6.0以上 动态权限获取 以适配android11 存储权限
![image](https://github.com/jkdsking/Permissions/blob/master/png/1.jpg)
![image](https://github.com/jkdsking/Permissions/blob/master/png/2.jpg)
![image](https://github.com/jkdsking/Permissions/blob/master/png/3.jpg)
![image](https://github.com/jkdsking/Permissions/blob/master/png/4.jpg)
![image](https://github.com/jkdsking/Permissions/blob/master/png/5.jpg)
 
  ## gradle接入
	
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	dependencies {
	        implementation 'com.github.jkdsking:Permissions:1.1.1'
	}
   
    或者
    implementation 'com.jkds:Ios_Popup:1.0.1'(不用添加上面的jitpack库)
	

 ## 具体使用

```java
PermissionsRequest.with(this)
        // 申请安装包权限
        //.permission(Permission.REQUEST_INSTALL_PACKAGES)
        // 申请悬浮窗权限
        //.permission(Permission.SYSTEM_ALERT_WINDOW)
        // 申请通知栏权限
        //.permission(Permission.NOTIFICATION_SERVICE)
        // 申请系统设置权限
        //.permission(Permission.WRITE_SETTINGS)
        // 申请单个权限
        .permission(Permission.RECORD_AUDIO)
        // 申请多个权限
        .permission(Permission.Group.CALENDAR)
        .request(new OnPermission() {

            @Override
            public void hasPermission(List<String> granted, boolean all) {
                if (all) {
                    toast("获取限成功");
                } else {
                    toast("获取部分权限成功，但部分权限未正常授予");
                }
            }

            @Override
            public void noPermission(List<String> denied, boolean never) {
                if (never) {
                    toast("被永久拒绝授权，请手动授予权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    PermissionsRequest.startPermissionActivity(MainActivity.this, denied);
                } else {
                    toast("获取权限失败");
                }
            }
        });
```
#### 从系统权限设置页返回判断

```java
public class XxxActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionsRequest.REQUEST_CODE) {
            if (PermissionsRequest.hasPermission(this,Permission.MANAGE_EXTERNAL_STORAGE)) {
                toast("用户已经在权限设置页授予了存储权限");
            }else if (PermissionsRequest.hasPermission(this,Permission.ACCESS_FINE_LOCATION,Permission.ACCESS_COARSE_LOCATION)){
                toast("用户已经在权限设置页授予了定位权限");

            }
        }
    }
}
```
#### Android 11 存储适配

* 如果你的项目需要适配 Android 11 存储权限，那么需要先将 targetSdkVersion 进行升级

```groovy
android
    defaultConfig {
        targetSdkVersion 30
    }
}
```

* 再添加 Android 11 存储权限注册到清单文件中

```xml
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
```

* 需要注意的是，旧版的存储权限也需要在清单文件中注册，因为在低于 Android 11 的环境下申请存储权限，框架会自动切换到旧版的申请方式

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

* 还需要在清单文件中加上这个属性，否则在 Android 10 的设备上将无法正常读写外部存储上的文件

```xml
<application
    android:requestLegacyExternalStorage="true">
```

* 最后直接调用下面这句代码

```java
PermissionsRequest.with(MainActivity.this)
        // 不适配 Android 11 可以这样写
        //.permission(Permission.Group.STORAGE)
        // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
        .permission(Permission.MANAGE_EXTERNAL_STORAGE)
        .request(new OnPermission() {

            @Override
            public void hasPermission(List<String> granted, boolean all) {
                if (all) {
                    toast("获取存储权限成功");
                }
            }

            @Override
            public void noPermission(List<String> denied, boolean never) {
                if (never) {
                    toast("被永久拒绝授权，请手动授予存储权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    PermissionsRequest.startPermissionActivity(MainActivity.this, denied);
                } else {
                    toast("获取存储权限失败");
                }
            }
        });
```
 
 
 ## 开源协议
```
Copyright jkdsking Permissions

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```        
 
