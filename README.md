##  Android6.0以上 动态权限获取
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
	        implementation 'com.github.jkdsking:Permissions:1.0.1'
	}

 ## 单个权限使用
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
## 多个权限使用                
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
 
