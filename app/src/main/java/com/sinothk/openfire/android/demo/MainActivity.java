package com.sinothk.openfire.android.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinothk.openfire.android.IMHelper;
import com.sinothk.openfire.android.bean.IMCode;
import com.sinothk.openfire.android.bean.IMResult;
import com.sinothk.openfire.android.inters.IMCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ author LiangYT
 * @ create 2018/10/18 15:06
 * @ Describe
 */
public class MainActivity extends AppCompatActivity {

    EditText userNameEt, userPwdEt;
    TextView logMsgTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameEt = this.findViewById(R.id.userNameEt);
        userPwdEt = this.findViewById(R.id.userPwdEt);
        logMsgTv = this.findViewById(R.id.logMsgTv);

        IMHelper.init("127.0.0.1", "192.168.2.135", 5222);

        IMHelper.exeConnection(this, new IMCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(IMResult result) {
                if (result.getCode() == IMCode.SUCCESS) {
                    show(result.getTip());
                    logPrint(result.getTip());
                } else {
                    show(result.getTip());
                    logPrint(result.getMsg());
                }
            }
        });
    }

    private void logPrint(String msg) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String msgAll = logMsgTv.getText().toString();
        msgAll += "\n" + sdf.format(new Date()) + "_" + msg;

        logMsgTv.setText(msgAll);
    }

    public void loginBtn(View view) {

        String userName = userNameEt.getText().toString();
        String userPwd = userPwdEt.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, "输入内容不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        IMHelper.login(MainActivity.this, userName, userPwd, new IMCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(IMResult result) {
                if (result.getCode() == IMCode.SUCCESS) {
                    show(result.getTip());
                    logPrint(result.getTip());
                } else {
                    show(result.getTip());
                    logPrint(result.getMsg());
                }
            }
        });
    }

    private void show(String tip) {
        Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
    }

    public void logoutBtn(View view) {
        IMHelper.disconnect(MainActivity.this, new IMCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(IMResult result) {
                if (result.getCode() == IMCode.SUCCESS) {
                    show(result.getTip());
                    logPrint(result.getTip());
                } else {
                    show(result.getTip());
                    logPrint(result.getMsg());
                }
            }
        });
    }

    public void registerBtn(View view) {

        String userName = userNameEt.getText().toString();
        String userPwd = userPwdEt.getText().toString();

        IMHelper.signUp(MainActivity.this, userName, userPwd, new IMCallback(){
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(IMResult result) {
                if (result.getCode() == IMCode.SUCCESS) {
                    show(result.getTip());
                    logPrint(result.getTip());
                } else {
                    show(result.getTip());
                    logPrint(result.getMsg());
                }
            }
        });
    }
}
