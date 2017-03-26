package com.zhuyue.permissioncheck.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.base.BasePermissionActivity;

public class PermissionActivity extends BasePermissionActivity {

    private static final String TAG = "PermissionActivity";
    /**
     * 相机权限Code
     */
    private final int CAMERA_CODE = 0x0001;
    /**
     * 电话权限Code
     */
    private final int PHONE_CODE = 0x0002;
    /**
     * 联系人Code
     */
    private final int CONTACTS_CODE = 0x0003;
    /**
     * 日期Code
     */
    private final int CALENDAR_CODE = 0x0004;

    private String calanderURL = "content://com.android.calendar/calendars";

    private final int CONTACTS_RESULT_CODE = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_check;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CONTACTS_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri conatctData = data.getData();
                    Cursor cursor = managedQuery(conatctData, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String phonenum = "此联系人暂未输入电话号码";
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                        if (phones.moveToFirst()) {
                            phonenum = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        Toast.makeText(PermissionActivity.this, "联系人：" + name + "\n电话：" + phonenum, Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT < 14) {
                            phones.close();
                        }
                    }
                    if (Build.VERSION.SDK_INT < 14) {//不添加的话Android4.0以上系统运行会报错
                        cursor.close();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 拍照权限
     *
     * @param view
     */
    public void OnCamera(View view) {
        requestPermission(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
    }

    /**
     * 打电话权限
     *
     * @param view
     */
    public void OnCallPhone(View view) {
        requestPermission(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CODE);
    }

    /**
     * 联系人权限
     *
     * @param view
     */
    public void OnNote(View view) {
        requestPermission(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, CONTACTS_CODE);
    }

    /**
     * 读取日程权限
     *
     * @param view
     */
    public void OnCalendar(View view) {
        requestPermission(new String[]{Manifest.permission.READ_CALENDAR}, CALENDAR_CODE);
    }

    @Override
    public void requestPermissionSucess(int requestCode) {
        super.requestPermissionSucess(requestCode);
        switch (requestCode) {
            case CAMERA_CODE:
                Intent cameraIntent = new Intent(); //调用照相机
                cameraIntent.setAction("android.media.action.STILL_IMAGE_CAMERA");
                startActivity(cameraIntent);
                break;
            case PHONE_CODE:
                Intent phoenIntent = new Intent();
                phoenIntent.setAction(Intent.ACTION_CALL);
                phoenIntent.setData(Uri.parse("tel:13045020882"));
                startActivity(phoenIntent);
                break;
            case CONTACTS_CODE:
                Intent intent3 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent3, CONTACTS_RESULT_CODE);//请求码自己定义
                break;
            case CALENDAR_CODE:
                if (Build.VERSION.SDK_INT >= 8) {
                    calanderURL = "content://com.android.calendar/calendars";
                } else {
                    calanderURL = "content://calendar/calendars";
                }
                Cursor userCursor = getContentResolver().query(
                        Uri.parse(calanderURL), null, null, null, null);
                if (userCursor.getCount() > 0) {
                    userCursor.moveToFirst();
                    String userName = userCursor.getString(userCursor
                            .getColumnIndex("name"));
                    Toast.makeText(PermissionActivity.this, userName, Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                break;
        }
    }
}
