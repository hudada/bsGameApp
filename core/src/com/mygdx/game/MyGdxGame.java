package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.LoadingScreen;

public class MyGdxGame extends Game {
    LoadingScreen loadingScreen;
    GameScreen gameScreen;
    MyEvent event;
    String cmds, num;
    int curr,now;

    public MyGdxGame(String cmds, String num, int curr,int now, MyEvent event) {
        this.event = event;
        this.cmds = cmds;
        this.num = num;
        this.curr = curr;
        this.now = now;
    }

    @Override
    public void create() {
        loadingScreen = new LoadingScreen(this);
        gameScreen = new GameScreen(this, cmds, num, curr,now);

        this.setScreen(gameScreen);
    }

    public void error() {
        event.notify(this, "通过失败，请重试");
    }

    public void succeed(int curr) {
        event.succeed(curr);
    }
}
