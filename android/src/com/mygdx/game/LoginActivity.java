package com.mygdx.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mygdx.game.bean.AccountBean;
import com.mygdx.game.bean.AccountBeanInfo;
import com.mygdx.game.bean.BaseResponse;
import com.mygdx.game.net.ApiManager;
import com.mygdx.game.utils.SpUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = ((EditText) findViewById(R.id.et_num)).getText().toString().trim();
                final String pwd = ((EditText) findViewById(R.id.et_pwd)).getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                }
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = "";
                        String inputLine;
                        try {
                            URL httpUrl = new URL(ApiManager.LOGIN + num+"/"+pwd);
                            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();

                            InputStreamReader input = new InputStreamReader(conn.getInputStream());
                            BufferedReader buffer = new BufferedReader(input);
                            while ((inputLine = buffer.readLine()) != null)
                                result += inputLine;
                            buffer.close();
                            progressDialog.dismiss();
                            if (TextUtils.isEmpty(result)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                final AccountBeanInfo accountBeanInfo = new Gson().fromJson(result, AccountBeanInfo.class);
                                if (accountBeanInfo.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    SpUtils.setUserBean(LoginActivity.this,accountBeanInfo.getData());
                                    Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, accountBeanInfo.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }

                        } catch (Exception e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });


        findViewById(R.id.btn_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
