package com.mygdx.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mygdx.game.bean.BaseResponse;
import com.mygdx.game.net.ApiManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends Activity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = ((EditText) findViewById(R.id.et_num)).getText().toString().trim();
                final String pwd = ((EditText) findViewById(R.id.et_pwd)).getText().toString().trim();
                String pwd2 = ((EditText) findViewById(R.id.et_pwd2)).getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    Toast.makeText(RegisterActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pwd.equals(pwd2)) {
                    Toast.makeText(RegisterActivity.this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(RegisterActivity.this);
                }
                progressDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = "";
                        String inputLine;
                        try {
                            URL httpUrl = new URL(ApiManager.REGISTER + num+"/"+pwd);
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
                                        Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                final BaseResponse baseResponse = new Gson().fromJson(result, BaseResponse.class);
                                if (baseResponse.getCode() == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
}
