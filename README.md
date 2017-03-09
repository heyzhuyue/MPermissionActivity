# 运行时权限
Android6.0M中对用户的权限分为了一般权限和危险权限，这些危险权限除了在AndroidManifest.xml中注册以外，还需要在使用的时候对用户进行请求权限弹窗提醒，才可以使用。
* 危险权限，此类权限除了需要在AndroidManifest.xml注册外，还需要在Activty中动态获取此权限，此权限会请求用户允许

![image](https://github.com/linglongxin24/MPermissions/blob/master/screenshorts/permissions.png?raw=true)

```java
group:android.permission-group.CONTACTS
  permission:android.permission.WRITE_CONTACTS
  permission:android.permission.GET_ACCOUNTS
  permission:android.permission.READ_CONTACTS

group:android.permission-group.PHONE
  permission:android.permission.READ_CALL_LOG
  permission:android.permission.READ_PHONE_STATE
  permission:android.permission.CALL_PHONE
  permission:android.permission.WRITE_CALL_LOG
  permission:android.permission.USE_SIP
  permission:android.permission.PROCESS_OUTGOING_CALLS
  permission:com.android.voicemail.permission.ADD_VOICEMAIL

group:android.permission-group.CALENDAR
  permission:android.permission.READ_CALENDAR
  permission:android.permission.WRITE_CALENDAR

group:android.permission-group.CAMERA
  permission:android.permission.CAMERA

group:android.permission-group.SENSORS
  permission:android.permission.BODY_SENSORS

group:android.permission-group.LOCATION
  permission:android.permission.ACCESS_FINE_LOCATION
  permission:android.permission.ACCESS_COARSE_LOCATION

group:android.permission-group.STORAGE
  permission:android.permission.READ_EXTERNAL_STORAGE
  permission:android.permission.WRITE_EXTERNAL_STORAGE

group:android.permission-group.MICROPHONE
  permission:android.permission.RECORD_AUDIO

group:android.permission-group.SMS
  permission:android.permission.READ_SMS
  permission:android.permission.RECEIVE_WAP_PUSH
  permission:android.permission.RECEIVE_MMS
  permission:android.permission.RECEIVE_SMS
  permission:android.permission.SEND_SMS
  permission:android.permission.READ_CELL_BROADCASTS
```
Android6.0这些危险权限可以通过adb shell pm list permissions -d -g查看


* 一般权限，此类权限只需要在AndroidManifest.xml文件注册声明，用户打开应用时，应用会自动获取声明权限

```java
ACCESS_LOCATION_EXTRA_COMMANDS
ACCESS_NETWORK_STATE
ACCESS_NOTIFICATION_POLICY
ACCESS_WIFI_STATE
BLUETOOTH
BLUETOOTH_ADMIN
BROADCAST_STICKY
CHANGE_NETWORK_STATE
CHANGE_WIFI_MULTICAST_STATE
CHANGE_WIFI_STATE
DISABLE_KEYGUARD
EXPAND_STATUS_BAR
GET_PACKAGE_SIZE
INSTALL_SHORTCUT
INTERNET
KILL_BACKGROUND_PROCESSES
MODIFY_AUDIO_SETTINGS
NFC
READ_SYNC_SETTINGS
READ_SYNC_STATS
RECEIVE_BOOT_COMPLETED
REORDER_TASKS
REQUEST_INSTALL_PACKAGES
SET_ALARM
SET_TIME_ZONE
SET_WALLPAPER
SET_WALLPAPER_HINTS
TRANSMIT_IR
UNINSTALL_SHORTCUT
USE_FINGERPRINT
VIBRATE
WAKE_LOCK
WRITE_SYNC_SETTINGS
```

## 运行时权限方法了解

* ContextCompat.checkSelfPermission方法

  >用于检测某个权限是否已经被授予，方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED，当返回DENIED就需要进行申请授权了

* ActivityCompat.shouldShowRequestPermissionRationale方法

  > 这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。

* ActivityCompat.requestPermissions方法

  > 此方法用于运行时权限申请，通过参数String[] permissions可知，此方法支持多个权限申请，	系统会根据权限逐一向用户询问授权

* onRequestPermissionsResult方法

  > 此方法是用户申请权限回调方法，通过此方法判断权限授权状态，此方法返回参数 requestCode、String[] permissions、int[] grantResults中，requesCode是用户申请权限验证的Code,并且返回所有权限及权限申请状态


## 参考文章

[鸿洋运行时权限](http://blog.csdn.net/lmj623565791/article/details/50709663)

[运行时权限封装](http://blog.csdn.net/linglongxin24/article/details/53189359)

