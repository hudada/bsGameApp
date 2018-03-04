package com.mygdx.game.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mygdx.game.bean.AccountBean;
import com.mygdx.game.bean.OutpostBeanList;

/**
 * Created by yezi on 2018/1/29.
 */

public class SpUtils {

    private static final String SP_NAME = "sp_name";
    private static final String ABOUT_USER = "about_user";
    private static final String ABOUT_OUTPOST = "about_outpost";

    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void setUserBean(Context context, AccountBean accountBean) {
        if (accountBean != null) {
            getSp(context).edit().putString(ABOUT_USER, new Gson().toJson(accountBean)).apply();
        }
    }

    public static AccountBean getUserBean(Context context) {
        String user = getSp(context).getString(ABOUT_USER, "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, AccountBean.class);
        } else {
            return null;
        }
    }

    public static void setOutpostBeanList(Context context, OutpostBeanList outpostBeanList) {
        if (outpostBeanList != null && outpostBeanList.getData() != null
                && outpostBeanList.getData().size() >0) {
            getSp(context).edit().putString(ABOUT_OUTPOST, new Gson().toJson(outpostBeanList)).apply();
        }
    }

    public static OutpostBeanList getOutpostBeanList(Context context) {
        String user = getSp(context).getString(ABOUT_OUTPOST, "");
        if (!TextUtils.isEmpty(user)) {
            return new Gson().fromJson(user, OutpostBeanList.class);
        } else {
            return null;
        }
    }

    public static boolean cleanUserBean(Context context) {
        return getSp(context).edit().putString(ABOUT_USER, "").commit();
    }
}
