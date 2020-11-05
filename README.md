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
	        implementation 'com.github.jkdsking:Permissions:1.1.1'
	}
	

 ## 单个权限使用
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
                                } else {
                                    toast("权限已拒绝");
                                }
                            }
                        });
## 多个权限使用                
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
                                } else {
                                    toast("权限已拒绝");
                                }
                            }
                        });
 
 
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
 
