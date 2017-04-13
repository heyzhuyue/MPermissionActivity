package com.zhuyue.permissioncheck.ui.record.utils;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by win7 on 2016/12/16.
 * <p>
 * 录音工具类
 */

public class WzAudioRecordUtils {

    public static final String TAG = "WzAudioRecordUtils";
    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间
    private static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;
    private MediaRecorder mMediaRecorder;//录音
    private String mFolderPath = "";//文件夹路径
    private String mFilePath = "";//录音文件路径
    private long mStartTime;
    private long mEndTime;
    private static WzAudioRecordUtils wzAudioRecordUtils = null;
    private OnAudioStatusUpdateListener mOnAudioStatusUpdateListener;

    public static WzAudioRecordUtils getInstance() {
        if (wzAudioRecordUtils == null) {
            wzAudioRecordUtils = new WzAudioRecordUtils();
        }
        return wzAudioRecordUtils;
    }


    /**
     * 默认构造方法
     */
    public WzAudioRecordUtils() {
        this(Environment.getExternalStorageDirectory() + "/record/");
    }

    /**
     * 构造方法
     *
     * @param floderPath 文件夹路径
     */
    public WzAudioRecordUtils(String floderPath) {
        File file = new File(floderPath);
        if (!file.exists()) {
            file.mkdir();
        }
        this.mFolderPath = floderPath;
    }

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener mOnAudioStatusUpdateListener) {
        this.mOnAudioStatusUpdateListener = mOnAudioStatusUpdateListener;
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mFilePath = mFolderPath + TimeUtils.getCurrentTime() + ".amr";
            mMediaRecorder.setOutputFile(mFilePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mStartTime = System.currentTimeMillis();
            updateMicStatus();
        } catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消录音
     */
    public void cancleRecord() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
        mFilePath = "";
    }

    /**
     * 结束录音
     *
     * @return
     */
    public long endRecord() {
        if (mMediaRecorder == null)
            return 0L;
        mEndTime = System.currentTimeMillis();
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;

        mOnAudioStatusUpdateListener.onStop(mFilePath);
        mFilePath = "";
        return mEndTime - mStartTime;

    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };

    /**
     * 更新麦克风状态
     */
    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if (null != mOnAudioStatusUpdateListener) {
                    mOnAudioStatusUpdateListener.onUpdate(db, System.currentTimeMillis() - mStartTime);
                }
            }
            handler.postDelayed(runnable, SPACE);
        }
    }


    public interface OnAudioStatusUpdateListener {
        /**
         * 录音中...
         *
         * @param db   当前声音分贝
         * @param time 录音时长
         */
        public void onUpdate(double db, long time);

        /**
         * 停止录音
         *
         * @param filePath 保存路径
         */
        public void onStop(String filePath);
    }


}
