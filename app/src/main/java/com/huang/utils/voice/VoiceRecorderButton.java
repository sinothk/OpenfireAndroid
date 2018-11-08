package com.huang.utils.voice;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.huang.adapter.holer.AudioHolder;
import com.sinothk.openfire.android.demo.R;

public class VoiceRecorderButton extends Button implements
        AudioManager.AudioStateListener {

    // 超出按钮高度
    private static final int DICETANCE_Y_CANCEL = 50;

    private static final int STATE_NORMAL = 1;// 默认状态
    private static final int STATE_RECORDING = 2;// 录音状态
    private static final int STATE_WANT_TO_CANCEL = 3;// 要取消状态

    private int mCurrentState = STATE_NORMAL;

    private boolean isRecording = false;// 是否正在录音

    private DialogManager dialogManager;

    private AudioManager audioManager;

    private float mTime;

    // 是否触发onLongClick
    private boolean isReady;

    public VoiceRecorderButton(Context context) {
        super(context, null);
    }

    public VoiceRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        dialogManager = new DialogManager(getContext());

        // 如果不存在SD卡
        if (TextUtils.isEmpty(Environment.getExternalStorageState())
                || !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.e("SD卡", "SD卡不存在");
            return;
        }

        String dir = Environment.getExternalStorageDirectory() + "/com_yongting_care_im_audios";
        if (!TextUtils.isEmpty(dir)) {
            audioManager = AudioManager.getInstance(dir);
            audioManager.setOnAudioStateListener(this);
            setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    audioManager.preparedAudio();
                    isReady = true;
                    return false;
                }
            });
        }
    }

    /**
     * 完成录音
     */
    public interface AudioRecorderListener {

        void onFinish(float seconds, String filePath);

    }

    private AudioRecorderListener audioRecorderListener;

    public void setOnAudioRecorderListener(
            AudioRecorderListener audioRecorderListener) {
        this.audioRecorderListener = audioRecorderListener;
    }

    private static final int MSG_AUDIO_PREPARED = 0X001;
    private static final int MSG_AUDIO_CHANGED = 0X002;
    private static final int MSG_DIALOG_DISMISS = 0X003;

    /**
     * 获取音量大小
     */
    private Runnable mGetVoiceLevelRunable = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    handler.sendEmptyMessage(MSG_AUDIO_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_AUDIO_PREPARED:
                    // 显示应该在prepared之后
                    dialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLevelRunable).start();
                    break;

                case MSG_AUDIO_CHANGED:
                    dialogManager.updateVoiceLevel(audioManager.getLevel(7));
                    break;

                case MSG_DIALOG_DISMISS:
                    dialogManager.dismissDialog();
                    break;

            }
        }
    };

    @Override
    public void wellPrepared() {
        handler.sendEmptyMessage(MSG_AUDIO_PREPARED);// 准备完成
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;

            case MotionEvent.ACTION_MOVE:
                // 如果是正在录音状态
                if (isRecording) {

                    // 根据x、y的坐标，判断当前是否是要取消
                    if (wantCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:

                // 如果音频还没准备好
                if (!isReady) {
                    reset();
                    return super.onTouchEvent(event);
                }

                // 此时的情况可能是，触发了按钮的onLongClik，音频还没准备好，UP先触发了。
                if (!isRecording || mTime < 0.5f) {
                    dialogManager.tooShort();// 显示时长过短
                    reset();
                    audioManager.cancel();
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
                } else if (mCurrentState == STATE_RECORDING) {// 正常录制结束
                    dialogManager.dismissDialog();
                    // release
                    audioManager.replease();

                    // callbackToActivity
                    if (audioRecorderListener != null) {
                        audioRecorderListener.onFinish(mTime,
                                audioManager.getCurrentFilePath());
                        reset();
                    }
                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {

                    // cancel
                    dialogManager.dismissDialog();
                    audioManager.cancel();
                    // 恢复状态
                    reset();
                }
                break;

        }

        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态、标志位
     */
    private void reset() {

        isRecording = false;
        changeState(STATE_NORMAL);
        mTime = 0;
        isReady = false;

    }

    /**
     * 取消
     *
     * @param x
     * @param y
     * @return
     */
    private boolean wantCancel(int x, int y) {

        /**
         * 判断x坐标是否超出按钮范围 x<0 到了左边 x > getWidth() 到有右边
         */
        if (x < 0 || x > getWidth()) {

            return true;

        }

        /**
         * 判断y坐标 如果y坐标超出自身高度
         */
        if (y < -DICETANCE_Y_CANCEL || y > getHeight() + DICETANCE_Y_CANCEL) {

            return true;

        }

        return false;
    }

    /**
     * 改变状态
     *
     * @param state
     */
    private void changeState(int state) {

        if (mCurrentState != state) {
            mCurrentState = state;

            switch (state) {

                case STATE_NORMAL:

                    // 设置当前按钮的背景和text

                    setText(R.string.str_recorder_normal);
                    break;

                case STATE_RECORDING:

                    // 如果正在播放语音,用户长按语音键时,应该先暂住播放
                    if (MediaPlayManager.isPlaying()) {
                        MediaPlayManager.stop(AudioHolder.flag, AudioHolder.view_recorder_anim);
                    }

                    setText(R.string.str_recorder);
                    if (isRecording) {

                        dialogManager.recording();

                    }
                    break;

                case STATE_WANT_TO_CANCEL:

                    setText(R.string.str_recorder_want_cancel);

                    dialogManager.wantToCnacel();
                    break;

            }
        }

    }

}
