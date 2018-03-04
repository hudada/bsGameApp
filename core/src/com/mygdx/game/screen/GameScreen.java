package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Actor.Player;
import com.mygdx.game.MyGdxGame;

import javax.swing.text.LabelView;

/**
 * Created by wdxc1 on 2018/2/15.
 */

public class GameScreen implements Screen {

    MyGdxGame game;
    SpriteBatch bacth;
    Texture texture_bg, code_bg;
    Texture dirt_bg, end_bg;
    Texture the_run, the_one, the_left, the_right, the_jump, the_loop;
    Image dirt_image, pre_dirt;
    Image look;
    Stage stage;
    Stage stage1;
    Stage stage2;
    Stage stage3;
    boolean isLeftStage;
    float imgHeight = 100;
    float imgWidth = 200;
    float offset = 50;
    Array<Image> images = new Array<Image>();
    Player player;
    int currCmd = 0;
    int currAct = 0;
    boolean isCanInput = true;
    Image currCmdImg, currLooped;
    String mCmd;
    Image succeedBg;

    Image loop2, loop3, loop4, loop5, loop6, loop7;
    Texture the_loop_code2, the_loop_code3, the_loop_code4, the_loop_code5, the_loop_code6,
            the_loop_code7;
    boolean isLoopsShow;
    String[] cmdList;
    float stage2CamX;

    String mNum;
    int mCurr, mNow;
    boolean isTe;
    String mOldCmd;

    Image theRun, theOne, theLeft, theRight, theJump, theLoop;
    int mOldCmdIndex = -1;

    public FACE face = FACE.Right;

    public enum FACE {
        Right, Left, Top, Bottom
    }

    public GameScreen(MyGdxGame game, String cmds, String num, int curr, int now) {
        mNow = now;
        mOldCmd = cmds;
        if (now < 6 && now > 0) {
            mOldCmdIndex = 0;
            isTe = true;
        }
        this.game = game;
        mNum = num;
        mCurr = curr;
        StringBuffer cmdBuf = new StringBuffer();
        String[] cc = cmds.split(",");
        if (cc.length == 1) {
            if (cmds.startsWith("loop")) {
                int count = Integer.parseInt(cmds.split("-")[1].split("]")[0]);
                for (int i = 0; i < count; i++) {
                    cmdBuf.append(cmds.split("-")[0].split("\\[")[1] + "_loop");
                    cmdBuf.append(",");
                }
                cmdBuf.substring(0, cmdBuf.length() - 1);
                mCmd = cmdBuf.toString();
            } else {
                mCmd = cmds;
            }
        } else {
            for (int i = 0; i < cc.length; i++) {
                if (cc[i].startsWith("loop")) {
                    int count = Integer.parseInt(cc[i].split("-")[1].split("]")[0]);
                    for (int ii = 0; ii < count; ii++) {
                        cmdBuf.append(cc[i].split("-")[0].split("\\[")[1] + "_loop");
                        cmdBuf.append(",");
                    }
                } else {
                    cmdBuf.append(cc[i]);
                    cmdBuf.append(",");
                }
            }
            cmdBuf.substring(0, cmdBuf.length() - 1);
            mCmd = cmdBuf.toString();
        }
    }

    @Override
    public void show() {

        loop2 = new Image(new Texture("loop_2.png"));
        loop3 = new Image(new Texture("loop_3.png"));
        loop4 = new Image(new Texture("loop_4.png"));
        loop5 = new Image(new Texture("loop_5.png"));
        loop6 = new Image(new Texture("loop_6.png"));
        loop7 = new Image(new Texture("loop_7.png"));
        loop2.setScale(1.5f);
        loop3.setScale(1.5f);
        loop4.setScale(1.5f);
        loop5.setScale(1.5f);
        loop6.setScale(1.5f);
        loop7.setScale(1.5f);
        if (isTe) {
            loop4.addListener(new MyClickListener(MyClickListener.LOOP4));
        } else {
            loop2.addListener(new MyClickListener(MyClickListener.LOOP2));
            loop3.addListener(new MyClickListener(MyClickListener.LOOP3));
            loop4.addListener(new MyClickListener(MyClickListener.LOOP4));
            loop5.addListener(new MyClickListener(MyClickListener.LOOP5));
            loop6.addListener(new MyClickListener(MyClickListener.LOOP6));
            loop7.addListener(new MyClickListener(MyClickListener.LOOP7));
        }


        stage = new Stage();
        stage1 = new Stage();
        stage2 = new Stage();
        stage2CamX = stage2.getCamera().position.x;
        stage3 = new Stage();
        succeedBg = new Image(new Texture("succeed.jpg"));

        bacth = new SpriteBatch();


        texture_bg = new Texture("start_bg.jpg");
        code_bg = new Texture("code_bg.png");
        dirt_bg = new Texture("dirt_bg.jpg");
        end_bg = new Texture("end_bg.jpg");


        Image image1 = new Image(code_bg);
        image1.setSize(stage.getWidth() / 3, stage.getHeight());
        image1.setPosition(0, 0);
        stage1.addActor(image1);


        addActionImg();

        initMap();

        initPlayer();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        if (!isTe) {
            inputMultiplexer.addProcessor(stage);
            inputMultiplexer.addProcessor(stage2);
        }
        inputMultiplexer.addProcessor(stage1);
        inputMultiplexer.addProcessor(stage3);
        inputMultiplexer.addProcessor(new GestureDetector(new MyGestureListener()));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void initPlayer() {
        player = new Player(stage.getWidth() / 3 + imgWidth * 2,
                Gdx.graphics.getHeight() / 2 + dirt_image.getHeight() / 2,
                new Player.OnCmdResultListener() {
                    @Override
                    public void OnCmdSucceed() {

                    }

                    @Override
                    public void OnCmdNext() {

                        currCmd++;
                        if (currCmd <= cmdList.length - 1) {
                            if (cmdList[currCmd].endsWith("loop")) {
                                if (currLooped == null) {
                                    currCmdImg.setScale(1f);

                                    currLooped = images.get(currAct + 2);
                                    currLooped.setScale(1.2f);

                                    currCmdImg = images.get(currAct + 1);
                                    currCmdImg.setScale(1.2f);
                                    currAct += 2;
                                }
                            } else {
                                currCmdImg.setScale(1f);
                                if (currLooped != null) {
                                    currLooped.setScale(1f);
                                    currLooped = null;
                                }

                                currAct++;
                                currCmdImg = images.get(currAct);
                                currCmdImg.setScale(1.2f);
                            }


                            player.run(cmdList[currCmd], currCmd);
                        } else {
                            if (currCmd < mCmd.split(",").length) {
                                gameError();
                            } else {
                                gameSucceed();
                            }
                        }
                    }

                    @Override
                    public void OnError() {
                        gameError();
                    }
                }, mCmd);
        player.setName("player");
        stage.addActor(player);
    }

    private void initCmds() {
        currCmdImg = images.get(currAct);
        StringBuffer cmds = new StringBuffer();
        for (int i = 0; i < images.size; i++) {
            Image image = images.get(i);
            if (image.getName().startsWith("the_loop")) {
                if (i + 1 < images.size &&
                        !images.get(i + 1).getName().startsWith("the_loop")) {
                    int count = Integer.parseInt(image.getName().split("the_loop_")[1]);
                    if (i == 0) {
                        currLooped = images.get(i + 1);
                        currAct++;
                    }
                    for (int i1 = 0; i1 < count; i1++) {
                        cmds.append(images.get(i + 1).getName() + "_loop");
                        cmds.append(",");
                    }
                    i++;
                }
            } else {
                cmds.append(image.getName());
                cmds.append(",");
            }
        }
        if (cmds.toString().equals("")) {
            gameError();
            return;
        }
        cmds.substring(0, cmds.length() - 1);
        currCmdImg.setScale(1.2f);
        isCanInput = false;
        if (currLooped != null) {
            currLooped.setScale(1.2f);
        }
        cmdList = cmds.toString().split(",");
        player.run(cmdList[0], currCmd);
    }

    private void gameSucceed() {
        stage.clear();
        stage1.clear();
        stage2.clear();
        succeedBg.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mCurr != -1) {
                    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                    final Net.HttpRequest httpRequest = requestBuilder.newRequest()
                            .method(Net.HttpMethods.GET)
                            .url("http://192.168.55.102:8080/api/user/where/" + mNum + "/" + mCurr)
                            .build();
                    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                        @Override
                        public void handleHttpResponse(Net.HttpResponse httpResponse) {
                            dispose();
                            game.dispose();
                        }

                        @Override
                        public void failed(Throwable t) {
                            dispose();
                            game.dispose();
                        }

                        @Override
                        public void cancelled() {
                            dispose();
                            game.dispose();
                        }
                    });
                } else {
                    dispose();
                    game.dispose();
                }
                return true;
            }
        });
        succeedBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage3.addActor(succeedBg);
    }

    private void gameError() {
        game.error();
        player.remove();
        currCmdImg.setScale(1f);
        currCmdImg = null;
        if (currLooped != null) {
            currLooped.setScale(1f);
            currLooped = null;
        }
        currCmd = 0;
        currAct = 0;
        isCanInput = true;
        initPlayer();
    }

    private void initMap() {

        dirt_image = new Image(dirt_bg);
        float x = (stage.getWidth() / 3 + imgWidth * 2);
        dirt_image.setPosition(x, Gdx.graphics.getHeight() / 2);

        pre_dirt = dirt_image;
        stage.addActor(pre_dirt);


        String[] cmds = mCmd.split(",");
        for (int i = 0; i < cmds.length; i++) {
            String str = cmds[i];
            if (i == cmds.length - 1) {
                dirt_image = new Image(end_bg);
            } else {
                dirt_image = new Image(dirt_bg);
            }
            if (str.startsWith("the_one")) {
                switch (face) {
                    case Bottom:
                        dirt_image.setPosition(pre_dirt.getX(),
                                pre_dirt.getY() - pre_dirt.getHeight());
                        break;
                    case Right:
                        dirt_image.setPosition(pre_dirt.getX() + pre_dirt.getWidth(),
                                pre_dirt.getY());
                        break;
                    case Left:
                        dirt_image.setPosition(pre_dirt.getX() - pre_dirt.getWidth(),
                                pre_dirt.getY());
                        break;
                    case Top:
                        dirt_image.setPosition(pre_dirt.getX(),
                                pre_dirt.getY() + pre_dirt.getHeight());
                        break;
                }
                pre_dirt = dirt_image;
                stage.addActor(dirt_image);
            } else if (str.startsWith("the_jump")) {
                switch (face) {
                    case Bottom:
                        dirt_image.setPosition(pre_dirt.getX(),
                                pre_dirt.getY() - pre_dirt.getHeight() * 2);
                        break;
                    case Right:
                        dirt_image.setPosition(pre_dirt.getX() + pre_dirt.getWidth() * 2,
                                pre_dirt.getY());
                        break;
                    case Left:
                        dirt_image.setPosition(pre_dirt.getX() - pre_dirt.getWidth() * 2,
                                pre_dirt.getY());
                        break;
                    case Top:
                        dirt_image.setPosition(pre_dirt.getX(),
                                pre_dirt.getY() + pre_dirt.getHeight() * 2);
                        break;
                }
                pre_dirt = dirt_image;
                stage.addActor(dirt_image);
            } else if (str.startsWith("the_left")) {
                switch (face) {
                    case Bottom:
                        face = FACE.Right;
                        break;
                    case Right:
                        face = FACE.Top;
                        break;
                    case Left:
                        face = FACE.Bottom;
                        break;
                    case Top:
                        face = FACE.Left;
                        break;
                }
            } else if (str.startsWith("the_right")) {
                switch (face) {
                    case Bottom:
                        face = FACE.Left;
                        break;
                    case Right:
                        face = FACE.Bottom;
                        break;
                    case Left:
                        face = FACE.Top;
                        break;
                    case Top:
                        face = FACE.Right;
                        break;
                }
            }

        }
    }


    private void addActionImg() {


        the_run = new Texture("the_run.png");
        the_one = new Texture("the_one.png");
        the_left = new Texture("the_left.png");
        the_right = new Texture("the_right.png");
        the_jump = new Texture("the_jump.png");
        the_loop = new Texture("the_loop.png");
        the_loop_code2 = new Texture("the_loop_code2.png");
        the_loop_code3 = new Texture("the_loop_code3.png");
        the_loop_code4 = new Texture("the_loop_code4.png");
        the_loop_code5 = new Texture("the_loop_code5.png");
        the_loop_code6 = new Texture("the_loop_code6.png");
        the_loop_code7 = new Texture("the_loop_code7.png");

        theRun = new Image(the_run);
        theRun.setSize(imgWidth, imgHeight);
        theRun.setPosition(stage.getWidth() / 3, stage.getHeight() - imgHeight);

        theOne = new Image(the_one);
        theOne.setSize(imgWidth, imgHeight);
        theOne.setPosition(stage.getWidth() / 3, stage.getHeight() - imgHeight * 2 - offset);

        theLeft = new Image(the_left);
        theLeft.setSize(imgWidth, imgHeight);
        theLeft.setPosition(stage.getWidth() / 3, stage.getHeight() - imgHeight * 3 - offset);

        theRight = new Image(the_right);
        theRight.setSize(imgWidth, imgHeight);
        theRight.setPosition(stage.getWidth() / 3, stage.getHeight() - imgHeight * 4 - offset);

        theJump = new Image(the_jump);
        theJump.setSize(imgWidth, imgHeight);
        theJump.setPosition(stage.getWidth() / 3, stage.getHeight() - imgHeight * 5 - offset);

        theLoop = new Image(the_loop);
        theLoop.setSize(imgWidth, imgHeight);
        theLoop.setPosition(stage.getWidth() / 3, stage.getHeight() - imgHeight * 6 - offset);

        theRun.addListener(new MyClickListener(MyClickListener.RUN));
        theOne.addListener(new MyClickListener(MyClickListener.ONE));

        if (isTe) {
            switch (mNow) {
                case 2:
                    theLeft.addListener(new MyClickListener(MyClickListener.LEFT));
                    break;
                case 3:
                    theRight.addListener(new MyClickListener(MyClickListener.RIGHT));
                    break;
                case 4:
                    theJump.addListener(new MyClickListener(MyClickListener.JUMP));
                    break;
                case 5:
                    theLoop.addListener(new ClickListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            if (isLoopsShow) {

                                removeLoops();

                            } else {
                                isLoopsShow = true;
                                loop2.setPosition(theLoop.getX() + theLoop.getWidth() + 10,
                                        theLoop.getY());
                                loop3.setPosition(loop2.getX() + loop2.getWidth(),
                                        loop2.getY());
                                loop4.setPosition(loop3.getX() + loop3.getWidth(),
                                        loop3.getY());
                                loop5.setPosition(loop2.getX(),
                                        loop2.getY() - loop2.getHeight());
                                loop6.setPosition(loop5.getX() + loop5.getWidth(),
                                        loop5.getY());
                                loop7.setPosition(loop6.getX() + loop6.getWidth(),
                                        loop6.getY());
                                stage1.addActor(loop2);
                                stage1.addActor(loop3);
                                stage1.addActor(loop4);
                                stage1.addActor(loop5);
                                stage1.addActor(loop6);
                                stage1.addActor(loop7);
                                if (isTe) {
                                    mOldCmdIndex++;
                                    initLook();
                                }
                            }
                            return true;
                        }
                    });
                    break;
            }
        } else {
            theLeft.addListener(new MyClickListener(MyClickListener.LEFT));
            theRight.addListener(new MyClickListener(MyClickListener.RIGHT));
            theJump.addListener(new MyClickListener(MyClickListener.JUMP));
            theLoop.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (isLoopsShow) {

                        removeLoops();

                    } else {
                        isLoopsShow = true;
                        loop2.setPosition(theLoop.getX() + theLoop.getWidth() + 10,
                                theLoop.getY());
                        loop3.setPosition(loop2.getX() + loop2.getWidth(),
                                loop2.getY());
                        loop4.setPosition(loop3.getX() + loop3.getWidth(),
                                loop3.getY());
                        loop5.setPosition(loop2.getX(),
                                loop2.getY() - loop2.getHeight());
                        loop6.setPosition(loop5.getX() + loop5.getWidth(),
                                loop5.getY());
                        loop7.setPosition(loop6.getX() + loop6.getWidth(),
                                loop6.getY());
                        stage1.addActor(loop2);
                        stage1.addActor(loop3);
                        stage1.addActor(loop4);
                        stage1.addActor(loop5);
                        stage1.addActor(loop6);
                        stage1.addActor(loop7);
                        if (isTe) {
                            mOldCmdIndex++;
                            initLook();
                        }
                    }
                    return true;
                }
            });
        }

        stage1.addActor(theRun);
        stage1.addActor(theOne);
        stage1.addActor(theLeft);
        stage1.addActor(theRight);
        stage1.addActor(theJump);
        stage1.addActor(theLoop);

        if (isTe) {
            if (mOldCmd.startsWith("loop[")) {
                mOldCmd = "the_loop,4,the_one";
            }
            initLook();
        }
    }


    private void initLook() {
        if (look != null) {
            look.remove();
            look = null;
        }
        theRun.setScale(1f);
        theOne.setScale(1f);
        theLeft.setScale(1f);
        theRight.setScale(1f);
        theJump.setScale(1f);
        theLoop.setScale(1f);
        if (mOldCmdIndex >= 0) {

            String[] cmds = mOldCmd.split(",");
            String currCmd;
            if (cmds.length > 1) {
                if (mOldCmdIndex < cmds.length) {
                    currCmd = cmds[mOldCmdIndex];
                } else {
                    currCmd = "the_run";
                }

            } else {
                if (mOldCmdIndex < cmds.length) {
                    currCmd = mOldCmd;
                } else {
                    currCmd = "the_run";
                }
            }

            Image currAct = null;
            if (currCmd.equals("the_one")) {
                theOne.setScale(1.2f);
                currAct = theOne;
            } else if (currCmd.equals("the_left")) {
                theLeft.setScale(1.2f);
                currAct = theLeft;
            } else if (currCmd.equals("the_right")) {
                theRight.setScale(1.2f);
                currAct = theRight;
            } else if (currCmd.equals("the_jump")) {
                theJump.setScale(1.2f);
                currAct = theJump;
            } else if (currCmd.equals("the_loop")) {
                theLoop.setScale(1.2f);
                currAct = theLoop;
            } else if (currCmd.equals("the_run")) {
                theRun.setScale(1.2f);
                currAct = theRun;
            } else if (currCmd.equals("4")) {
                loop4.setScale(2f);
                currAct = loop4;
            }

            look = new Image(new Texture("look.jpg"));
            look.setPosition(currAct.getX() + currAct.getWidth() * 1.2f,
                    currAct.getY());
            look.setScale(0.3f);
            stage1.addActor(look);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bacth.begin();
        bacth.draw(texture_bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bacth.end();


        stage.act();
        stage.draw();


        stage1.act();
        stage1.draw();

        for (int i = 0; i < images.size; i++) {
            Image image;
            if (i == 0) {
                image = images.get(i);
                image.setPosition(stage.getWidth() / 6 - image.getWidth() / 2,
                        stage.getHeight() - image.getHeight() - 20);
            } else {
                image = images.get(i);
                Image pre = images.get(i - 1);
                image.setPosition(stage.getWidth() / 6 - image.getWidth() / 2,
                        pre.getY() - image.getHeight());
            }
            if (image.getName().equals("the_loop")) {
            }
            stage2.addActor(image);
        }


        stage2.act();
        stage2.draw();

        stage3.act();
        stage3.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
//        stage.clear();
//        stage1.clear();
//        stage2.clear();
//        stage.dispose();
//        stage1.dispose();
//        stage2.dispose();
        if (mCurr != -1) {
            game.succeed(mCurr);
        }
        Gdx.app.exit();
    }

    private class MyClickListener extends ClickListener {

        int mType;
        public static final int RUN = 0;
        public static final int ONE = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
        public static final int JUMP = 4;
        public static final int LOOP2 = 5;
        public static final int LOOP3 = 6;
        public static final int LOOP4 = 7;
        public static final int LOOP5 = 8;
        public static final int LOOP6 = 9;
        public static final int LOOP7 = 10;

        public MyClickListener(int type) {
            mType = type;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (!isCanInput) {
                return super.touchDown(event, x, y, pointer, button);
            }
            Image image = null;
            switch (mType) {
                case RUN:
                    if (images.size > 0) {
                        if (isTe) {
                            mOldCmdIndex = -1;
                            initLook();
                        }
                        initCmds();
                    }
                    break;
                case ONE:
                    image = new Image(the_one);
                    image.setName("the_one");
                    break;
                case LEFT:
                    image = new Image(the_left);
                    image.setName("the_left");
                    break;
                case RIGHT:
                    image = new Image(the_right);
                    image.setName("the_right");
                    break;
                case JUMP:
                    image = new Image(the_jump);
                    image.setName("the_jump");
                    break;
                case LOOP2:
                    image = new Image(the_loop_code2);
                    image.setName("the_loop_2");
                    removeLoops();
                    break;
                case LOOP3:
                    image = new Image(the_loop_code3);
                    image.setName("the_loop_3");
                    removeLoops();
                    break;
                case LOOP4:
                    image = new Image(the_loop_code4);
                    image.setName("the_loop_4");
                    removeLoops();
                    break;
                case LOOP5:
                    image = new Image(the_loop_code5);
                    image.setName("the_loop_5");
                    removeLoops();
                    break;
                case LOOP6:
                    image = new Image(the_loop_code6);
                    image.setName("the_loop_6");
                    removeLoops();
                    break;
                case LOOP7:
                    image = new Image(the_loop_code7);
                    image.setName("the_loop_7");
                    removeLoops();
                    break;
            }
            if (image != null) {
                if (image.getName().startsWith("the_loop")) {
                    image.setSize(imgWidth, imgHeight * 2);
                } else {
                    image.setSize(imgWidth, imgHeight);
                }
                final Image finalImage = image;
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        int index = images.indexOf(finalImage, false);
                        images.removeIndex(index);
                        finalImage.remove();
                    }

                });
                images.add(image);
            }
            if (isTe && mOldCmdIndex >= 0) {
                mOldCmdIndex++;
                initLook();
            }
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private void removeLoops() {
        isLoopsShow = false;
        loop2.remove();
        loop3.remove();
        loop4.remove();
        loop5.remove();
        loop6.remove();
        loop7.remove();
    }

    private class MyGestureListener implements GestureDetector.GestureListener {

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            if (x < Gdx.graphics.getWidth() / 3 && images.size > 0) {
                isLeftStage = true;
            } else {
                isLeftStage = false;
            }
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if (isLeftStage) {
                float min = stage2CamX - (stage.getWidth() / 6 - imgWidth / 2);
                float max = stage2CamX + (stage.getWidth() / 6 - imgWidth / 2);

                stage2.getCamera().position.x -= deltaX;
                stage2.getCamera().position.y += deltaY;

                if (stage2.getCamera().position.x >= max
                        || stage2.getCamera().position.x <= min) {
                    if (deltaX > 0) {
                        stage2.getCamera().position.x = min;
                    }
                    if (deltaX < 0) {
                        stage2.getCamera().position.x = max;
                    }
                }
                stage2.getCamera().update();
                return true;
            } else {
                stage.getCamera().translate(-deltaX, deltaY, 0);
                stage.getCamera().update();
            }
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }
    }
}
