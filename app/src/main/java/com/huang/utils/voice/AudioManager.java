package com.huang.utils.voice;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 播放语音的工具类
 *
 * @author huang
 */
public class AudioManager {

    private MediaRecorder mediaRecorder;

    private String mDir;

    private String mCurFilePath;

    private static AudioManager mInstance;

    private boolean isPrepared;

    private AudioManager(String dir) {
        mDir = dir;
    }

    public String getCurrentFilePath() {
        return mCurFilePath;
    }

    public interface AudioStateListener {

        void wellPrepared();

    }

    private AudioStateListener audioStateListener;

    public void setOnAudioStateListener(AudioStateListener audioStateListener) {
        this.audioStateListener = audioStateListener;
    }

    public static AudioManager getInstance(String dir) {
        if (mInstance == null) {

            synchronized (AudioManager.class) {

                if (mInstance == null) {
                    mInstance = new AudioManager(dir);

                }

            }

        }
        return mInstance;
    }

    /**
     * 准备
     */
    @SuppressLint("InlinedApi")
    public void preparedAudio() {
        try {
            isPrepared = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = getGenarateFileName();
            File file = new File(dir, fileName);
            mCurFilePath = file.getAbsolutePath();
            mediaRecorder = new MediaRecorder();
            // 设置输出文件
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            // 设置音频源为麦克风
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);

            // 设置音频的编码
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();// 准备

            mediaRecorder.start();
            isPrepared = true;
            if (audioStateListener != null) {
                audioStateListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getGenarateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getLevel(int maxLevel) {

        if (isPrepared && mediaRecorder != null) {
            try {
                // mediaRecorder.getMaxAmplitude() 区间为1-32767
                return maxLevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }

    public void replease() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public void cancel() {
        replease();
        if (!TextUtils.isEmpty(mCurFilePath)) {
            File file = new File(mCurFilePath);
            if (file.exists())
                Log.i(AudioManager.class.getSimpleName(), "删除 mCurFilePath 文件");
            file.delete();
            mCurFilePath = null;
        }
    }

}
