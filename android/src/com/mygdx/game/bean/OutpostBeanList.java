package com.mygdx.game.bean;

import java.util.List;

/**
 * Created by wdxc1 on 2018/2/27.
 */

public class OutpostBeanList extends BaseResponse {
    private List<OutpostBean> data;

    public List<OutpostBean> getData() {
        return data;
    }

    public void setData(List<OutpostBean> data) {
        this.data = data;
    }
}
