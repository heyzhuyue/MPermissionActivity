package com.zhuyue.permissioncheck.ui.record.weight;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.zhuyue.permissioncheck.R;
import com.zhuyue.permissioncheck.ui.record.OnRecordStateInterface;
import com.zhuyue.permissioncheck.ui.record.dialog.RecordDialog;
import com.zhuyue.permissioncheck.ui.record.utils.TimeUtils;

import java.io.File;
import java.io.IOException;


/**
 * Created by win7 on 2016/12/16.
 */

public class RecordButton extends Button {

    private static final String TAG = "RecordButton";
    private final int Volume_What_100 = 100;//音量
    private final int Time_What_101 = 101;//时间
    private final int CancelRecord_What_102 = 102;//取消录音
    private final int CANCLE_LENGTH = -200;// 上滑取消距离
    private MediaRecorder mMediaRecorder = null;
    private String mFolderPath;//文件夹路径
    private String mFilePath;//文件路径
    private int MIN_INTERVAL_TIME = 1000;//最短录音时间
    private int MAX_INTERVAL_TIME = 1000 * 60;//最长录音时间
    private long mStartTime = 0;//开始时间
    private long mEndTimw = 0;//结束时间
    private VoiceVolumeThread voiceVolumeThread;
    private VolumeHandler volumeHandler;
    private int startY = 0;
    private RecordDialog recordDialog = null;
    private OnRecordStateInterface onRecordStateInterface;

    public RecordButton(Context context) {
        super(context);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        volumeHandler = new VolumeHandler();
    }

    /**
     * 设置文件夹路径
     *
     * @param mFolderPath
     */
    public void setfolderPath(String mFolderPath) {
        this.mFolderPath = mFolderPath;
    }

    /**
     * 设置最大录音秒数
     *
     * @param time 秒数
     */
    public void setMaxIntervalTime(int time) {
        if (time > 15 && time < 10 * 60) {
            this.MAX_INTERVAL_TIME = time * 1000;
        }
    }

    /**
     * 设置默认路径
     */
    public void setDefaultFilePath() {
        File file = new File(Environment.getExternalStorageDirectory() + "/record/");
        if (!file.exists()) {
            file.mkdir();
        }
        mFolderPath = file.getAbsolutePath();
    }

    public void setOnRecordStateInterface(OnRecordStateInterface onRecordStateInterface) {
        this.onRecordStateInterface = onRecordStateInterface;
    }

    /**
     * 初始化弹窗
     */
    public void initDialogAndStartRecord() {
        if (TextUtils.isEmpty(mFolderPath)) setDefaultFilePath();
        recordDialog = new RecordDialog(getContext(), R.style.recordbutton_alert_dialog);
        startRecording();
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        Toast.makeText(getContext(), "开始录音", Toast.LENGTH_SHORT).show();
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mFilePath = mFolderPath + TimeUtils.getCurrentTime() + ".amr";
            mMediaRecorder.setOutputFile(mFilePath);
            mMediaRecorder.setMaxDuration(MAX_INTERVAL_TIME);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mStartTime = System.currentTimeMillis();
            voiceVolumeThread = new VoiceVolumeThread();
            voiceVolumeThread.start();
        } catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录音
     */
    public void startRecording() {
        startRecord();
        recordDialog.showRecordDialog();
    }

    /**
     * 取消录音
     */
    public void cancleRecording() {
        Toast.makeText(getContext(), "取消录音", Toast.LENGTH_SHORT).show();
        recordDialog.hideRecordDialog();
        if (voiceVolumeThread != null) {
            voiceVolumeThread.exit();
            voiceVolumeThread = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
        mFilePath = "";
    }

    /**
     * 结束录音
     */
    public void finishRecording() {
        Toast.makeText(getContext(), "结束录音", Toast.LENGTH_SHORT).show();
        if (voiceVolumeThread != null) {
            voiceVolumeThread.exit();
            voiceVolumeThread = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        recordDialog.hideRecordDialog();
        long between = System.currentTimeMillis() - mStartTime;
        if (between < MIN_INTERVAL_TIME) {//录音时间过短
            Toast.makeText(getContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
            File file = new File(mFilePath);
            if (file.exists()) {
                file.delete();
                mFilePath = "";
                return;
            }
        }
        if (onRecordStateInterface != null) {
            onRecordStateInterface.onStopRecord(mFilePath);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                initDialogAndStartRecord();
                break;
            case MotionEvent.ACTION_UP:
                int endY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (endY - startY < CANCLE_LENGTH) {
                    cancleRecording();
                } else {
                    finishRecording();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int tempNowY = (int) event.getY();
                if (startY < 0)
                    return true;
                if (recordDialog != null) {
                    if (tempNowY - startY < CANCLE_LENGTH) {
                        recordDialog.setRecordState(getContext().getString(R.string.zeffect_recordbutton_releasing_finger_to_cancal_send));
                    } else {
                        recordDialog.setRecordState(getContext().getString(R.string.zeffect_recordbutton_finger_up_to_cancal_send));
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                cancleRecording();
                break;
        }
        return true;
    }

    /**
     * 模仿轮询更新麦克风状态线程
     */
    class VoiceVolumeThread extends Thread {

        private boolean isRuning = true;

        public void exit() {
            isRuning = false;
        }

        @Override
        public void run() {
            super.run();
            while (isRuning) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mMediaRecorder == null || !isRuning) {
                    break;
                }
                if (System.currentTimeMillis() - mStartTime >= MAX_INTERVAL_TIME) {
                    // 如果超过最长录音时间
                    volumeHandler.sendEmptyMessage(CancelRecord_What_102);
                }
                //发送时间
                volumeHandler.sendEmptyMessage(Time_What_101);
                //
                double ratio = (double) mMediaRecorder.getMaxAmplitude() / 1;
                double db = 0;// 分贝
                if (ratio != 0) {
                    db = 20 * Math.log10(ratio);
                    Message msg = new Message();
                    msg.obj = db;
                    msg.what = Volume_What_100;
                    volumeHandler.sendMessage(msg);
                }
            }
        }
    }

    /**
     * 更新状态Handler
     */
    class VolumeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Volume_What_100:
                    double tempVolumeMax = (double) msg.obj;
                    recordDialog.setVolumeLevel(tempVolumeMax);
                    break;
                case Time_What_101:
                    recordDialog.setRecordTime(System.currentTimeMillis() - mStartTime);
                    break;
                case CancelRecord_What_102:
                    cancleRecording();
                    break;
            }
        }
    }

}
