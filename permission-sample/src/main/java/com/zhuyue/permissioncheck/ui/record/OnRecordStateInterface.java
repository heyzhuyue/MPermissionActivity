package com.zhuyue.permissioncheck.ui.record;

/**
 * Created by zhuyue on 2017/4/5
 */

public interface OnRecordStateInterface {

    /**
     * 开始录音
     */
    void onStartRecord();

    /**
     * 结束录音
     */
    void onStopRecord(String filePath);

    /**
     * 取消录音
     */
    void onCancleRecord();
}
