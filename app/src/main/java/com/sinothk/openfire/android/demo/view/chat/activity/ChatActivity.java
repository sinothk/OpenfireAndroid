package com.sinothk.openfire.android.demo.view.chat.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huang.utils.CommonUtils;
import com.huang.utils.voice.MediaPlayManager;
import com.huang.utils.voice.VoiceRecorderButton;
import com.sinothk.comm.utils.IntentUtil;
import com.sinothk.openfire.android.IMCache;
import com.sinothk.openfire.android.SmackConnection;
import com.sinothk.openfire.android.SmackHelper;
import com.sinothk.openfire.android.bean.IMConstant;
import com.sinothk.openfire.android.bean.IMLastMessage;
import com.sinothk.openfire.android.bean.IMMessage;
import com.sinothk.openfire.android.demo.R;
import com.sinothk.openfire.android.demo.view.base.activity.TitleBarActivity;
import com.sinothk.openfire.android.demo.view.chat.adapter.ChatRecyclerListAdapter;
import com.sinothk.openfire.android.demo.view.contacts.activity.FriendInfoActivity;
import com.sinothk.openfire.android.patterns.Watch.Watcher;

import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.ArrayList;

/**
 * 聊天类-->即时聊天
 *
 * @author 梁玉涛
 */
public class ChatActivity extends TitleBarActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener, Watcher {

    private String currJid;

    // 对方
    private String toJid;
    private String toName;
    private String toAvatar;
    private boolean roomChatType;

    //    private Chat chat;// 单聊
    private MultiUserChat muc;// 群组

    private Button chatNewSend;// 发信息
    private EditText chatNewEditText;// 输入框
    private LinearLayout chatNewLinearLayout_other;// 显示功能布局

    private int PageNumber = 0;// 当前页数
    private SwipeRefreshLayout swipeView;// 下拉加载更多
    private RecyclerView msgListView;
    private ArrayList<IMMessage> list = new ArrayList<>();
    private ChatRecyclerListAdapter adapter;

    // 发送语音新增的内容
    private Button chatNewVoice, chatNewKeyboard, chatAddButton;// 语音按钮,键盘按钮,增加图片和配方的按钮
    private VoiceRecorderButton btn_recorder;// 长按录制语音按钮
    private String audio_seconds = "";// 录音时长
    private String audio_file_path = "";// 保存录音的路径

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        // 当前用户Jid
        currJid = SmackHelper.getCurrUser().getJid();

        // 对方 "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2594910732,993782407&fm=58"
        toJid = getIntent().getStringExtra("toJid");
        toName = getIntent().getStringExtra("toName");
        toAvatar = getIntent().getStringExtra("toAvatar");
        roomChatType = getIntent().getBooleanExtra("roomChatType", false);

        if (roomChatType) {
//            muc = LoginActivity.multiUserChatList.get(getIntent().getIntExtra("MultiUserChatPosition", 0));
        } else {
            toJid = getIntent().getStringExtra("toJid");
//            chat = XmppConnection.getInstance().getFriendChat(getIntent().getStringExtra("SingleUserChatJID"));
//            chat = IMHelper.getFriendChat(singleJid);
        }
    }

    private void initView() {
        setTitleBar(!TextUtils.isEmpty(toName) ? toName : "聊天", true, "更多", new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.openActivity(ChatActivity.this, FriendInfoActivity.class)
                        .putStringExtra("jid", toJid)
                        .putStringExtra("name", toName)
                        .start();
            }
        });

        SmackConnection.addWatcher(this);// 增加XMPP消息观察者

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);// 下拉刷新
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        btn_recorder = (VoiceRecorderButton) findViewById(R.id.btn_recorder);
        btn_recorder.setOnAudioRecorderListener(new MyAudioRecorderListener());

        chatNewVoice = (Button) findViewById(R.id.chatNewVoice);
        chatNewKeyboard = (Button) findViewById(R.id.chatNewkeyboard);
        chatAddButton = (Button) findViewById(R.id.chatAddButton);
        chatNewVoice.setOnClickListener(this);
        chatNewKeyboard.setOnClickListener(this);
        chatAddButton.setOnClickListener(this);
        chatNewSend = (Button) findViewById(R.id.chatNewSend);
        chatNewSend.setOnClickListener(this);
        chatNewLinearLayout_other = (LinearLayout) findViewById(R.id.chatLinearLayout_other);
        chatNewEditText = (EditText) findViewById(R.id.chatNewEditText);
        chatNewEditText.addTextChangedListener(new EditChangedListener());

        TextView chatNewPictureTextView = (TextView) findViewById(R.id.chatNewPictureTextView);
        chatNewPictureTextView.setOnClickListener(this);
        TextView chatNewArticleTextView = (TextView) findViewById(R.id.chatNewArticleTextView);
        chatNewArticleTextView.setOnClickListener(this);
        TextView chatNewCollectTextView = (TextView) findViewById(R.id.chatNewCollectTextView);
        chatNewCollectTextView.setOnClickListener(this);
        TextView chatNewPrescriptionTextView = (TextView) findViewById(R.id.chatNewPrescriptionTextView);
        chatNewPrescriptionTextView.setOnClickListener(this);

        msgListView = (RecyclerView) findViewById(R.id.chatNewListView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        msgListView.setLayoutManager(mLayoutManager);

        adapter = new ChatRecyclerListAdapter(this, currJid);
        msgListView.setAdapter(adapter);

        loadData(0);// 从本地读取旧的信息并加载数据源
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chatNewVoice:// 语音按钮
//                if (!CommonUtils.ExistSDCard()) {
//                    CommonUtils.dealWithToast(this, "请插入SD卡", 0);
//                } else if (CommonUtils.getSDFreeSize() < 2) {
//                    CommonUtils.dealWithToast(this, "SD卡剩余空间不足2M，请先清理SD卡空间", 0);
//                } else {
//                    btn_recorder.setVisibility(View.VISIBLE);
//                    chatNewEditText.setVisibility(View.GONE);
//                    chatNewVoice.setVisibility(View.GONE);
//                    chatNewKeyboard.setVisibility(View.VISIBLE);
//                    CommonUtils.doSomethingForKeyboard(this, false, chatNewEditText);// 隐藏软键盘
//                }
                break;
            case R.id.chatNewkeyboard:// 键盘按钮
                btn_recorder.setVisibility(View.GONE);
                chatNewEditText.setVisibility(View.VISIBLE);
                chatNewVoice.setVisibility(View.VISIBLE);
                chatNewKeyboard.setVisibility(View.GONE);
                CommonUtils.doSomethingForKeyboard(this, true, chatNewEditText);// 显示软键盘
                break;
            case R.id.chatAddButton:// 点击+号按钮,显示或者隐藏开方等布局
                if (chatNewLinearLayout_other.getVisibility() == View.GONE) {
                    chatNewLinearLayout_other.setVisibility(View.VISIBLE);
                } else {
                    chatNewLinearLayout_other.setVisibility(View.GONE);
                }
                CommonUtils.doSomethingForKeyboard(this, false, chatNewEditText);// 隐藏软键盘
                break;
            case R.id.chatNewPictureTextView:// 图片
                chatNewLinearLayout_other.setVisibility(View.GONE);

                if (!CommonUtils.ExistSDCard()) {
                    CommonUtils.dealWithToast(this, "请插入SD卡", 0);
                } else if (CommonUtils.getSDFreeSize() < 2) {
                    CommonUtils.dealWithToast(this, "SD卡剩余空间不足2M，请先清理SD卡空间", 0);
                } else {
                    // 从SD卡中读取图片,然后再发送
                }
                break;

            case R.id.chatNewArticleTextView:// 文章
                chatNewLinearLayout_other.setVisibility(View.GONE);
                break;

            case R.id.chatNewCollectTextView:// 收藏
                chatNewLinearLayout_other.setVisibility(View.GONE);
                break;

            case R.id.chatNewPrescriptionTextView:// 开方
                chatNewLinearLayout_other.setVisibility(View.GONE);
                break;

            case R.id.chatNewSend:
                String msg = chatNewEditText.getText().toString();
                if (!TextUtils.isEmpty(CommonUtils.replaceStr(msg))) {
                    // 创建单聊文本发送信息
                    IMMessage imMessage = IMMessage.createSingleTxtMsg(toJid, toName, toAvatar, msg);
                    sendMsg(imMessage);
                }
                break;
            default:
                break;
        }
    }

    /*
     * open fire发送消息
     */
    private void sendMsg(final IMMessage msg) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SmackHelper.send(msg);
//                IMHelper.send(msg);
            }
        }).start();

        // 更新界面
        chatNewEditText.setText("");// 输入框内容设置为空
        chatAddButton.setVisibility(View.VISIBLE);// 显示“+”按钮
        chatNewSend.setVisibility(View.GONE);// 隐藏发送按钮
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayManager.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MediaPlayManager.resume();
    }

    @Override
    protected void onDestroy() {
        MediaPlayManager.release();
        SmackConnection.removeWatcher(this);// 删除XMPP消息观察者
        super.onDestroy();
    }

    /*
     * 单条刷新界面
     */
    private void updateMessage(IMMessage imMessage) {
        try {
            if (imMessage == null) {
                return;
            }
            adapter.updateData(imMessage);
            msgListView.scrollToPosition(adapter.getItemCount() - 1);//刷新到底部
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 从本地读取旧的信息并加载数据源
     */
    private void loadData(int PageNumber) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                list = IMCache.findChatMsg(ChatActivity.this, toJid, currJid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData(list);
                        msgListView.scrollToPosition(adapter.getItemCount() - 1);//刷新到底部
                    }
                });
            }
        }).start();
    }

    /**
     * 发出或收到消息监听
     *
     * @param message message
     */
    @Override
    public void update(final org.jivesoftware.smack.packet.Message message) {

        final IMMessage imMessage = IMMessage.getIMMessageByMessageBody(message.getBody());

        // ==========保存完整消息到数据库=================
        if (imMessage == null || imMessage.getFromJid() == null || imMessage.getToJid() == null) {
            return;
        }

        if (imMessage.getFromJid().equals(currJid)) {
            imMessage.setFromType(IMConstant.FromType.SEND);
        } else {
            imMessage.setFromType(IMConstant.FromType.RECEIVE);
        }

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 更新界面
                    updateMessage(imMessage);
                }
            });

            IMCache.saveOrUpdateMsg(ChatActivity.this, imMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 保存最后一条数据
        try {
            IMLastMessage lastMsg = IMLastMessage.createLastMsg(imMessage);
            IMCache.saveOrUpdateLastMsg(ChatActivity.this, lastMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 长按语音按钮的监听事件
    private class MyAudioRecorderListener implements VoiceRecorderButton.AudioRecorderListener {
        @Override
        public void onFinish(float seconds, String filePath) {
            audio_seconds = Math.round(seconds) + "";
            audio_file_path = filePath;
            // 上传语音文件,上传成功后,添加语音的网络地址到列表中去
        }
    }

    // 输入框文本改变监听
    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // 输入文本之前的状态
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 输入文字中的状态，count是一次性输入字符数
        }

        @Override
        public void afterTextChanged(Editable s) {
            // 输入文字后的状态
            if (chatNewEditText.getText().toString().length() == 0) {
                chatAddButton.setVisibility(View.VISIBLE);// 显示“+”按钮
                chatNewSend.setVisibility(View.GONE);// 隐藏发送按钮
            } else {
                chatAddButton.setVisibility(View.GONE);// 隐藏“+”按钮
                chatNewSend.setVisibility(View.VISIBLE);// 显示发送按钮
            }
        }
    }

    @Override
    public void onRefresh() {
        // 下拉加载更多
        handler.sendEmptyMessageDelayed(5, 500);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:// 下拉加载更多
//                    PageNumber++;
//                    loadData(PageNumber);// 从本地读取旧的信息并加载数据源
                    swipeView.setRefreshing(false);
                    break;
            }
        }
    };
}
