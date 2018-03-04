package com.mygdx.game.bean;

/**
 * Created by wdxc1 on 2018/2/27.
 */

public class OutpostBean {
    private String cmds;
    private String showCmds;
    private int theOrder;

    public String getCmds() {
        return cmds;
    }

    public void setCmds(String cmds) {
        this.cmds = cmds;
    }

    public String getShowCmds() {
        return showCmds;
    }

    public void setShowCmds(String showCmds) {
        this.showCmds = showCmds;
    }

    public int getTheOrder() {
        return theOrder;
    }

    public void setTheOrder(int theOrder) {
        this.theOrder = theOrder;
    }

    public OutpostBean(String cmds, int theOrder) {
        this.cmds = cmds;
        this.theOrder = theOrder;
    }
}
