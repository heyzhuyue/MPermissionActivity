package com.zhuyue.permissioncheck.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.ui.alum.AlumActivity;
import com.zhuyue.permissioncheck.ui.record.RecordActivity;

/**
 * Created by zhuyue on 2017/3/26
 */

public class CheckRunPermissionActivity extends AppCompatActivity {
    private static final String TAG = "CheckRunPermissionActiv";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runpermission_check);
    }

    public void OnRecord(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }

    public void OnAlum(View view) {
        startActivity(new Intent(this, AlumActivity.class));
    }
}
