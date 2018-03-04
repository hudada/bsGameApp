package com.mygdx.game;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mygdx.game.bean.AccountBean;
import com.mygdx.game.utils.SpUtils;

public class MainActivity extends Activity {

    private Button btn_start, btn_action;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                startActivity(intent);
            }
        });


        btn_action = (Button) findViewById(R.id.btn_login);





        //授权
        requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS}, 521);

        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(1000);
        //注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AccountBean accountBean = SpUtils.getUserBean(this);
        if (accountBean == null) {
            btn_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            ((TextView) findViewById(R.id.tv_hello)).setText("欢迎回来" + accountBean.getNumber());
            btn_action.setText("退出");
            btn_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SpUtils.cleanUserBean(MainActivity.this)){
                        System.exit(0);
                    }
                }
            });
        }
    }
}
