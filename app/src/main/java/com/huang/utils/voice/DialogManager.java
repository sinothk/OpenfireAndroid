package com.huang.utils.voice;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinothk.openfire.android.demo.R;


public class DialogManager {

    private Dialog mDailog;

    private ImageView iv_recorder_dialog_icon;

    private ImageView iv_recorder_dialog_voice;

    private TextView tx_recorder_dialog_lable;

    private Context mContext;

    public DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    public void showRecordingDialog() {
        mDailog = new Dialog(mContext, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_recorder, null);
        mDailog.setContentView(view);
        iv_recorder_dialog_icon = (ImageView) mDailog
                .findViewById(R.id.iv_recorder_dialog_icon);
        iv_recorder_dialog_voice = (ImageView) mDailog
                .findViewById(R.id.iv_recorder_dialog_voice);
        tx_recorder_dialog_lable = (TextView) mDailog
                .findViewById(R.id.tx_recorder_dialog_lable);
        mDailog.show();
    }

    public void recording() {
        if (mDailog != null && mDailog.isShowing()) {
            iv_recorder_dialog_icon.setVisibility(View.VISIBLE);
            iv_recorder_dialog_voice.setVisibility(View.VISIBLE);
            tx_recorder_dialog_lable.setVisibility(View.VISIBLE);

            iv_recorder_dialog_icon.setImageResource(R.drawable.recorder);
            tx_recorder_dialog_lable
                    .setText(R.string.str_recorder_up_touch_cancel);
        }
    }

    public void wantToCnacel() {
        if (mDailog != null && mDailog.isShowing()) {
            iv_recorder_dialog_icon.setVisibility(View.VISIBLE);
            iv_recorder_dialog_voice.setVisibility(View.GONE);
            tx_recorder_dialog_lable.setVisibility(View.VISIBLE);

            iv_recorder_dialog_icon
                    .setImageResource(R.drawable.recorder_to_cancel);
            tx_recorder_dialog_lable.setText(R.string.str_recorder_want_cancel);
        }

    }

    public void tooShort() {
        if (mDailog != null && mDailog.isShowing()) {
            iv_recorder_dialog_icon.setVisibility(View.VISIBLE);
            iv_recorder_dialog_voice.setVisibility(View.GONE);
            tx_recorder_dialog_lable.setVisibility(View.VISIBLE);

            iv_recorder_dialog_icon
                    .setImageResource(R.drawable.recorder_voice_short);
            tx_recorder_dialog_lable.setText(R.string.str_recorder_too_short);
        }
    }

    public void dismissDialog() {

        if (mDailog != null && mDailog.isShowing())
            mDailog.dismiss();
    }

    public void updateVoiceLevel(int level) {

        if (mDailog != null && mDailog.isShowing()) {
            int resID = mContext.getResources().getIdentifier("amp" + level,
                    "drawable", mContext.getPackageName());

            iv_recorder_dialog_voice.setImageResource(resID);
        }

    }
}
