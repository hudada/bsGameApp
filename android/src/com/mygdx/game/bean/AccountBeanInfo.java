package com.mygdx.game.bean;

/**
 * Created by wdxc1 on 2018/2/27.
 */

public class AccountBeanInfo extends BaseResponse {
    private AccountBean data;

    public AccountBean getData() {
        return data;
    }

    public void setData(AccountBean data) {
        this.data = data;
    }
}
