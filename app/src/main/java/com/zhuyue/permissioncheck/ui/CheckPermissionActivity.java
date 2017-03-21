package com.zhuyue.permissioncheck.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.base.BasePermissioActivity;
import com.zhuyue.permissionlib.PermissionCheck;

/**
 * Created by win7 on 2017/3/8.
 */

public class CheckPermissionActivity extends BasePermissioActivity {

    private static final int REQUSTCODE = 12;//读取联系人电话
    private String calanderURL = "content://com.android.calendar/calendars";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUSTCODE:
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
                        Toast.makeText(CheckPermissionActivity.this, "联系人：" + name + "\n电话：" + phonenum, Toast.LENGTH_SHORT).show();
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_check;
    }

    /**
     * 拍照权限
     *
     * @param view
     */
    public void OnCamera(View view) {
        PermissionCheck.requestPermission(CheckPermissionActivity.this, 0x0001, new String[]{Manifest.permission.CAMERA});
    }

    /**
     * 打电话权限
     *
     * @param view
     */
    public void OnCallPhone(View view) {
        PermissionCheck.requestPermission(CheckPermissionActivity.this, 0x0002, new String[]{Manifest.permission.CALL_PHONE});
    }

    /**
     * 短信,联系人权限
     *
     * @param view
     */
    public void OnNote(View view) {
        PermissionCheck.requestPermission(CheckPermissionActivity.this, 0x0003, new String[]{Manifest.permission.READ_CONTACTS});
    }

    /**
     * r读取日程权限
     *
     * @param view
     */
    public void OnCalendar(View view) {
        PermissionCheck.requestPermission(CheckPermissionActivity.this, 0x0004, new String[]{Manifest.permission.READ_CALENDAR});
    }

    @Override
    public void grant(Object source, int requestCode) {
        if (requestCode == 0x0003) {
            Intent intent = new Intent(); //调用照相机
            intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
            startActivity(intent);
        } else if (requestCode == 0x0001) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:13045020882"));
            startActivity(intent);
        } else if (requestCode == 0x0003) {
            Intent intent3 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent3, REQUSTCODE);//请求码自己定义
        } else if (requestCode == 0x0004) {
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
                Toast.makeText(CheckPermissionActivity.this, userName, Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void rationale(Object source, int requestCode) {

    }
}
