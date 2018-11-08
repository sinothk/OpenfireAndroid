package com.huang.utils.voice;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;

import com.sinothk.openfire.android.demo.R;

import java.io.IOException;

public class MediaPlayManager {

    private static MediaPlayer mediaPlayer;

    private static boolean isPause;

    public static String audio_filePath;// 播放路径

    public static void playSound(String filePath,
                                 MediaPlayer.OnCompletionListener listener) {
        audio_filePath = filePath;

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(listener);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    public static void resume() {
        if (mediaPlayer != null && isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    public static void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 判断是否在播放语音
     *
     * @return true 为是
     */
    public static Boolean isPlaying() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 停止语音播放
     */
    public static void stop(Boolean flag, View view_recorder_anim) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();

            // 关闭动画
            if (view_recorder_anim != null) {
                if (flag) {// 左动画
                    view_recorder_anim.setBackgroundResource(R.drawable.voice_right);
                } else {// 右动画
                    view_recorder_anim.setBackgroundResource(R.drawable.voice_left);
                }
            }
        }
    }

    /**
     * 暂停语音播放
     */
    public static void pause_second(Boolean flag, View view_recorder_anim) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

            // 关闭动画
            if (view_recorder_anim != null) {
                if (flag) {// 左动画
                    view_recorder_anim.setBackgroundResource(R.drawable.voice_right);
                } else {// 右动画
                    view_recorder_anim.setBackgroundResource(R.drawable.voice_left);
                }
            }
        }
    }
}

