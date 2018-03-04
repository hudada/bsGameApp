package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.bean.AccountBean;
import com.mygdx.game.utils.SpUtils;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new MyGdxGame(getIntent().getStringExtra("cmds"),
                getIntent().getStringExtra("num"),
                getIntent().getIntExtra("curr",-1),
                getIntent().getIntExtra("now",1),
                new MyEvent() {
                    @Override
                    public void notify(Object obj, final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AndroidLauncher.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void succeed(int curr) {
                        AccountBean bean = SpUtils.getUserBean(AndroidLauncher.this);
                        if (bean != null) {
                            bean.setCurrwhere(curr);
                            SpUtils.setUserBean(AndroidLauncher.this, bean);
                        }
                    }
                }), config);
    }
}
