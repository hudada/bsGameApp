package com.mygdx.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mygdx.game.bean.AccountBean;
import com.mygdx.game.bean.BaseResponse;
import com.mygdx.game.bean.OutpostBean;
import com.mygdx.game.bean.OutpostBeanList;
import com.mygdx.game.net.ApiManager;
import com.mygdx.game.utils.SpUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SelectActivity extends Activity {

    RecyclerView rl_list;
    private ProgressDialog progressDialog;
    private ArrayList<OutpostBean> mData;
    private AccountBean accountBean;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);


        rl_list = (RecyclerView) findViewById(R.id.rl_list);
        rl_list.setLayoutManager(new GridLayoutManager(this, 5));
        rl_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = dip2px(SelectActivity.this, 10);
                outRect.bottom = dip2px(SelectActivity.this, 10);
            }
        });

        accountBean = SpUtils.getUserBean(this);

        mData = new ArrayList<>();
        OutpostBean outpostBean1 = new OutpostBean("the_one", 1);
        OutpostBean outpostBean2 = new OutpostBean("the_one,the_left,the_one", 2);
        OutpostBean outpostBean3 = new OutpostBean("the_one,the_right,the_one", 3);
        OutpostBean outpostBean4 = new OutpostBean("the_one,the_one,the_jump,the_one", 4);
        OutpostBean outpostBean5 = new OutpostBean("loop[the_one-4]", 5);
        mData.add(outpostBean1);
        mData.add(outpostBean2);
        mData.add(outpostBean3);
        mData.add(outpostBean4);
        mData.add(outpostBean5);


        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        accountBean = SpUtils.getUserBean(this);
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }

    private void loadData() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(SelectActivity.this);
        }
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                String inputLine;
                try {
                    URL httpUrl = new URL(ApiManager.OUTPOST_LIST);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                    conn.setConnectTimeout(5000);
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
                                Toast.makeText(SelectActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                myAdapter = new MyAdapter();
                                rl_list.setAdapter(myAdapter);
                            }
                        });

                    } else {
                        final OutpostBeanList outpostBeanList = new Gson().fromJson(result, OutpostBeanList.class);
                        if (outpostBeanList != null && outpostBeanList.getData().size() > 0) {
                            SpUtils.setOutpostBeanList(SelectActivity.this, outpostBeanList);
                            mData.addAll((ArrayList<OutpostBean>) outpostBeanList.getData());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myAdapter = new MyAdapter();
                                rl_list.setAdapter(myAdapter);
                            }
                        });
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    if (SpUtils.getOutpostBeanList(SelectActivity.this) != null) {
                        mData.addAll((ArrayList<OutpostBean>) SpUtils.getOutpostBeanList(SelectActivity.this).getData());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myAdapter = new MyAdapter();
                                rl_list.setAdapter(myAdapter);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SelectActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                myAdapter = new MyAdapter();
                                rl_list.setAdapter(myAdapter);
                            }
                        });
                    }
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(SelectActivity.this).inflate(R.layout.item_select, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.setItem(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private View root;

        public MyViewHolder(View itemView) {
            super(itemView);
            root = itemView;

        }

        public void setItem(final OutpostBean outpostBean) {
            final ImageView iv_bg = (ImageView) root.findViewById(R.id.iv_bg);
            TextView tv_int = (TextView) root.findViewById(R.id.tv_int);

            tv_int.setText(outpostBean.getTheOrder() + "");
            final int position = outpostBean.getTheOrder();
            if (accountBean != null) {

                final int curr = accountBean.getCurrwhere();


                if (position < curr) {
                    iv_bg.setBackgroundResource(R.drawable.select_ok_bg);
                    tv_int.setVisibility(View.GONE);
                } else {
                    iv_bg.setBackgroundResource(R.drawable.select_bg);
                    tv_int.setVisibility(View.VISIBLE);
                }


                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position <= curr) {
                            Intent intent = new Intent(SelectActivity.this,
                                    AndroidLauncher.class);
                            intent.putExtra("cmds", outpostBean.getCmds());
                            intent.putExtra("num", accountBean.getNumber());
                            intent.putExtra("now", position);
                            if (position == curr) {
                                intent.putExtra("curr", accountBean.getCurrwhere() + 1);
                            }
                            startActivity(intent);
                        } else {
                            Toast.makeText(SelectActivity.this, "请先通关之前关卡", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SelectActivity.this,
                                AndroidLauncher.class);
                        intent.putExtra("now", position);
                        intent.putExtra("cmds", outpostBean.getCmds());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
