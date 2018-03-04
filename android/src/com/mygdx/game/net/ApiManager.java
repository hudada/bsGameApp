package com.mygdx.game.net;

/**
 * Created by yezi on 2018/1/27.
 */

public class ApiManager {

    private static final String HTTP = "http://";
    private static final String IP = "192.168.55.102";
    private static final String PROT = ":8080";
    private static final String HOST = HTTP + IP + PROT;
    private static final String API = "/api";
    private static final String USER = "/user";
    private static final String OUTPOST = "/outpost";


    public static final String REGISTER = HOST + API + USER + "/";
    public static final String LOGIN = HOST + API + USER + "/login/";
    public static final String OUTPOST_LIST = HOST + API + OUTPOST + "/list";
}
