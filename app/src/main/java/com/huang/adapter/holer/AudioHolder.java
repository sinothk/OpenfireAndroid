//package com.huang.adapter.holer;
//
//import android.annotation.SuppressLint;
//import android.graphics.drawable.AnimationDrawable;
//import android.media.MediaPlayer;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.huang.bean.YtecConsultMsgBean;
//import com.huang.utils.CommonUtils;
//import com.huang.utils.voice.MediaPlayManager;
//import com.sinothk.comm.utils.DateUtil;
//import com.sinothk.openfire.android.bean.IMMessage;
//import com.sinothk.openfire.android.demo.R;
//
//import java.io.File;
//import java.util.Date;
//
///**
// * 5:语音
// * @author huang
// *
// */
//public class AudioHolder extends RecyclerView.ViewHolder {
//    public LinearLayout chatLinearlayoutRight_audio;
//    public TextView chatTimeTextviewRight_audio;
//    public ImageView chatImageviewRight_audio;
//    public LinearLayout chatLinearlayoutLeft_audio;
//    public TextView chatTimeTextviewLeft_audio;
//    public ImageView chatImageviewLeft_audio;
//
//    public View view_recorder_anim_left;
//    public View view_recorder_anim_right;
//    public TextView listview_chat_audio_textview_left;
//    public TextView listview_chat_audio_textview_right;
//
//    public AudioHolder(View view, Boolean flag) {
//        super(view);
//        if (flag) {// 左
//            this.chatLinearlayoutLeft_audio = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_left_audio);// 左布局
//            this.chatTimeTextviewLeft_audio = (TextView) view.findViewById(R.id.listview_chat_time_textview_left_audio);// 左时间
//            this.chatImageviewLeft_audio = (ImageView) view.findViewById(R.id.listview_chat_imageview_left_audio);// 左头像
//
//            this.view_recorder_anim_left = view.findViewById(R.id.view_recorder_anim_left);// 显示动画的view
//            this.listview_chat_audio_textview_left = (TextView) view.findViewById(R.id.listview_chat_audio_textview_left);
//        }else {
//            this.chatLinearlayoutRight_audio  = (LinearLayout) view.findViewById(R.id.listview_chat_linearlayout_right_audio);// 右布局
//            this.chatTimeTextviewRight_audio = (TextView) view.findViewById(R.id.listview_chat_time_textview_right_audio);// 右时间
//            this.chatImageviewRight_audio = (ImageView) view.findViewById(R.id.listview_chat_imageview_right_audio);// 右头像
//
//            this.view_recorder_anim_right = view.findViewById(R.id.view_recorder_anim_right);
//            this.listview_chat_audio_textview_right = (TextView) view.findViewById(R.id.listview_chat_audio_textview_right);
//        }
//    }
//
//    public void doSomething(final IMMessage bean, String doctor_id, final AudioHolder holder) {
//        if (bean.getTo().equals(doctor_id)) {// 发送者id等于本人id,那么显示右边布局
////            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewRight_audio);// 设置头像
//            holder.chatTimeTextviewRight_audio.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
//            holder.chatLinearlayoutRight_audio.setVisibility(View.VISIBLE);
//
//            if (bean.getAudio_time().equals("0")) {
//                holder.listview_chat_audio_textview_right.setText("1\"");
//            }else {
//                holder.listview_chat_audio_textview_right.setText(DateUtil.getFriendlyDate(new Date(bean.getMsgTime())));
//            }
//
//            holder.listview_chat_audio_textview_right.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if ( ! CommonUtils.onCheckSDScardExist()) {
//                        return ;// 没有SD卡不播放语音
//                    }
//
//                    // 处理用户点击语音前的事件
//                    // 当前为暂住语音播放时,不执行下面方法,即不重新播放语音
//                    if (doSomething(bean)) {
//                        return ;
//                    }
//
//                    view_recorder_anim = holder.view_recorder_anim_right;
//                    flag = false;// false右动画
//
//                    download_mar(bean);
//                }
//            });
//        } else if (bean.getReceiver_id() == doctor_id) {// 接受者id等于本人id,那么显示左边布局
//            CommonUtils.setImage(bean.getSender_photo(), holder.chatImageviewLeft_audio);// 设置头像
//            holder.chatTimeTextviewLeft_audio.setText(bean.getSend_time());
//            holder.chatLinearlayoutLeft_audio.setVisibility(View.VISIBLE);
//
//            if (bean.getAudio_time().equals("0")) {
//                holder.listview_chat_audio_textview_left.setText("1\"");
//            }else {
//                holder.listview_chat_audio_textview_left.setText(bean.getAudio_time()+"\"");
//            }
//            // 点击播放语音
//            holder.listview_chat_audio_textview_left.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if ( ! CommonUtils.onCheckSDScardExist()) {
//                        return ;// 没有SD卡不播放语音
//                    }
//
//                    // 处理用户点击语音前的事件
//                    // 当前为暂住语音播放时,不执行下面方法,即不重新播放语音
//                    if (doSomething(bean)) {
//                        return ;
//                    }
//
//                    view_recorder_anim = holder.view_recorder_anim_left;
//                    flag = true;// true左动画
//
//                    download_mar(bean);
//                }
//            });
//        }
//    }
//
//    /**
//     * 处理用户点击语音前的事件
//     * @param bean
//     * @return true为暂停，false为停止
//     */
//    private Boolean doSomething(YtecConsultMsgBean bean){
//        // 如果正在播放语音,用户点击其它语音时,应该先暂停非当前语音的播放,并关闭动画
//        if(MediaPlayManager.isPlaying()){
//            String audio_name = bean.getAudio().substring(bean.getAudio().lastIndexOf("/") + 1, bean.getAudio().length());
//            String audio_file_path = Environment.getExternalStorageDirectory() + "/com_yongting_care_im_audios/" + audio_name;
//
//            if (audio_file_path.equals(MediaPlayManager.audio_filePath)) {
//                // 点击当前正在播放的语音,则暂住语音的播放
//                MediaPlayManager.pause_second(flag , view_recorder_anim);
//
//                return true;
//            }else {
//                MediaPlayManager.stop(flag , view_recorder_anim);
//            }
//        }
//
//        return false;
//    }
//
//    private void download_mar(final YtecConsultMsgBean bean){
//        // 截取/后面的文件名,不包括/
//        final String audio_name = bean.getAudio().substring(bean.getAudio().lastIndexOf("/") + 1, bean.getAudio().length());
//        final String audio_file_path = Environment.getExternalStorageDirectory() + "/com_yongting_care_im_audios/" + audio_name;
//
//        if ((new File(audio_file_path)).exists()) {
//            // 文件存在,则播放地址为audio_file_path
//            Message message = new Message();// 发送message
//            message.what = 0;
//            message.obj = audio_file_path;
//            handler.sendMessage(message);
//        }else {
//            // 文件不存在,则先下载文件,然后再播放
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String flag = CommonUtils.fileDownLoadByUrl(CommonUtils.imageUrl + bean.getAudio(),
//                            "com_yongting_care_im_audios", audio_name);
//                    if (flag.equals("success")) {// 下载成功
//                        Message message = new Message();// 发送message
//                        message.what = 0;
//                        message.obj = audio_file_path;
//                        handler.sendMessage(message);
//                    }else{
//                        Message message = new Message();// 发送message
//                        message.what = -1;
//                        message.obj = "文件下载出错";
//                        handler.sendMessage(message);
//                    }
//                }
//            }).start();
//        }
//    }
//
//    public static Boolean flag = true;// 判断是要显示true左动画，还是false右动画
//    public static View view_recorder_anim;// 动画
//
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            System.out.println("文件"+(String) msg.obj);
//
//            switch (msg.what) {
//                case -1:// 文件下载出错
//                    //MyUtils.dealWithToast(ChatListAdapter.context, "语音下载失败,请重试", 0);
//                    break;
//                case 0:// 下载成功,播放语音
//                    // 显示语音符号
//                    if (view_recorder_anim != null) {
//                        // 设置音量变化图
//                        if (flag) {// 左动画
//                            view_recorder_anim.setBackgroundResource(R.drawable.play_anim_right);
//                        }else {// 右动画
//                            view_recorder_anim.setBackgroundResource(R.drawable.play_anim_left);
//                        }
//                        final AnimationDrawable drawable = (AnimationDrawable) view_recorder_anim.getBackground();
//                        drawable.start();// 开始动画
//
//                        // 播放语音
//                        MediaPlayManager.playSound((String) msg.obj,
//                                new MediaPlayer.OnCompletionListener() {
//                                    @Override
//                                    public void onCompletion(MediaPlayer mp) {
//                                        if (flag) {// 左动画
//                                            view_recorder_anim.setBackgroundResource(R.drawable.voice_right);
//                                        }else {// 右动画
//                                            view_recorder_anim.setBackgroundResource(R.drawable.voice_left);
//                                        }
//                                        drawable.stop();// 关闭动画
//                                    }
//                                });
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//}
//
