package com.mygdx.game;

/**
 * Created by wdxc1 on 2018/2/17.
 */

public abstract class MyEvent {
    public abstract void notify(Object obj,String msg);
    public abstract void succeed(int curr);
}
