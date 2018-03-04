package com.mygdx.game.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by wdxc1 on 2018/2/21.
 */

public class Player extends Actor {
    public static float x;
    public static float y;
    public float statetime;

    Texture texture;
    TextureRegion currentFrame;


    Animation aniRight;
    Animation aniLeft;
    Animation aniTop;
    Animation aniBottom;

    Animation aniRightJump;
    Animation aniLeftJump;
    Animation aniTopJump;
    Animation aniBottomJump;

    Animation aniIdle;

    public STATE state;
    public MOVE move;

    public enum STATE {
        Right, Left, Top, Bottom,Error
    }

    public enum MOVE {
        One,Jump, Idel,Face
    }

    float moveSum = 0;
    float jumpSum = 0;
    OnCmdResultListener onCmdResultListener;
    String[] trueCmds;

    public Player(float x, float y, OnCmdResultListener onCmdResultListener,String trueCmd) {
        this.x = x;
        this.y = y;
        this.statetime = 0;
        this.show();
        move = MOVE.Idel;
        state = STATE.Right;
        this.onCmdResultListener = onCmdResultListener;
        this.trueCmds = trueCmd.split(",");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        statetime += Gdx.graphics.getDeltaTime();

        this.update();

        this.check();

        batch.draw(currentFrame, x, y);
    }

    public void update() {
        if (move == MOVE.One){
            if (state == STATE.Right) {
                if (moveSum >= 80) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 3f;
                    this.x += 3f;
                }
            } else if (state == STATE.Left) {
                if (moveSum >= 80) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 3f;
                    this.x -= 3f;
                }
            } else if (state == STATE.Top) {
                if (moveSum >= 80) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 3f;
                    this.y += 3f;
                }
            } else if (state == STATE.Bottom) {
                if (moveSum >= 80) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 3f;
                    this.y -= 3f;
                }
            }
        }else if (move == MOVE.Jump){
            if (state == STATE.Right) {
                if (moveSum >= 160) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    jumpSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 6f;
                    this.x += 6f;
                    if (jumpSum <= 80){
                        jumpSum += 6f;
                        this.y += 3f;
                    }else{
                        this.y -= 3f;
                    }
                }
            } else if (state == STATE.Left) {
                if (moveSum >= 160) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    jumpSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 6f;
                    this.x -= 6f;
                    if (jumpSum <= 80){
                        jumpSum += 6f;
                        this.y += 3f;
                    }else{
                        this.y -= 3f;
                    }
                }
            } else if (state == STATE.Top) {
                if (moveSum >= 160) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 6f;
                    this.y += 6f;
                }
            } else if (state == STATE.Bottom) {
                if (moveSum >= 160) {
                    move = MOVE.Idel;
                    moveSum = 0;
                    onCmdResultListener.OnCmdNext();
                } else {
                    moveSum += 6f;
                    this.y -= 6f;
                }
            }
        }
        else if (move ==MOVE.Face){
            if (moveSum >= 80) {
                move = MOVE.Idel;
                moveSum = 0;
                onCmdResultListener.OnCmdNext();
            } else {
                moveSum += 3f;
            }
        }
    }

    public void check() {
        if (state == STATE.Error){
            if (moveSum >= 80) {
                move = MOVE.Idel;
                moveSum = 0;
                state = STATE.Right;
                onCmdResultListener.OnError();
            } else {
                moveSum += 3f;
            }
        }
        if (move == MOVE.One){
            if (state == STATE.Right) {
                currentFrame = (TextureRegion) aniRight.getKeyFrame(statetime, true);
            } else if (state == STATE.Left) {
                currentFrame = (TextureRegion) aniLeft.getKeyFrame(statetime, true);
            } else if (state == STATE.Top) {
                currentFrame = (TextureRegion) aniTop.getKeyFrame(statetime, true);
            } else if (state == STATE.Bottom) {
                currentFrame = (TextureRegion) aniBottom.getKeyFrame(statetime, true);
            }
        }else if (move == MOVE.Jump){
            if (state == STATE.Right) {
                currentFrame = (TextureRegion) aniRightJump.getKeyFrame(statetime, true);
            } else if (state == STATE.Left) {
                currentFrame = (TextureRegion) aniLeftJump.getKeyFrame(statetime, true);
            } else if (state == STATE.Top) {
                currentFrame = (TextureRegion) aniTopJump.getKeyFrame(statetime, true);
            } else if (state == STATE.Bottom) {
                currentFrame = (TextureRegion) aniBottomJump.getKeyFrame(statetime, true);
            }
        }
        else{
            currentFrame = (TextureRegion) aniIdle.getKeyFrame(statetime, true);
        }
    }

    public void show() {
        Texture texture = new Texture("player.png");
        TextureRegion[][] player = TextureRegion.split(texture, 50, 88);
        TextureRegion[][] playerFlip = TextureRegion.split(texture, 50, 88);

        for(TextureRegion[] region1 : playerFlip){
           for(TextureRegion region2 : region1){
               region2.flip(true, false);
           }
       }
        
        
        TextureRegion[] regionR = new TextureRegion[3];
        regionR[0] = player[2][2];
        regionR[1] = player[2][6];
        regionR[2] = player[2][0];
        aniRight = new Animation(0.1f, regionR);

        TextureRegion[] regionL = new TextureRegion[3];
        regionL[0] = playerFlip[2][2];
        regionL[1] = playerFlip[2][6];
        regionL[2] = playerFlip[2][0];
        aniLeft = new Animation(0.1f, regionL);

        TextureRegion[] regionT = new TextureRegion[3];
        regionT[0] = player[4][2];
        regionT[1] = player[4][6];
        regionT[2] = player[4][0];
        aniTop = new Animation(0.1f, regionT);

        TextureRegion[] regionB = new TextureRegion[3];
        regionB[0] = player[0][2];
        regionB[1] = player[0][6];
        regionB[2] = player[0][0];
        aniBottom = new Animation(0.1f, regionB);

        TextureRegion[] regionI = new TextureRegion[1];
        regionI[0] = player[0][0];
        aniIdle = new Animation(0.1f, regionI);

        TextureRegion[] regionRJump = new TextureRegion[1];
        regionRJump[0] = player[2][2];
        aniRightJump = new Animation(0.1f, regionRJump);

        TextureRegion[] regionLJump = new TextureRegion[1];
        regionLJump[0] = playerFlip[2][2];
        aniLeftJump = new Animation(0.1f, regionLJump);

        TextureRegion[] regionTJump = new TextureRegion[1];
        regionTJump[0] = player[4][2];
        aniTopJump = new Animation(0.1f, regionTJump);

        TextureRegion[] regionBJump = new TextureRegion[1];
        regionBJump[0] = player[0][2];
        aniBottomJump = new Animation(0.1f, regionBJump);
    }

    public void run(String cmd,int cmdIndex) {
        if (cmdIndex > trueCmds.length-1 ||
                (!cmd.startsWith(trueCmds[cmdIndex]) && !trueCmds[cmdIndex].startsWith(cmd))){
            state = STATE.Error;
            return;
        }
        if (cmd.equals("the_one") || cmd.equals("the_one_loop")) {
            move = MOVE.One;
        } else if (cmd.equals("the_left") || cmd.equals("the_left_loop")) {
            switch (state){
                case Top:
                    state = STATE.Left;
                    break;
                case Left:
                    state = STATE.Bottom;
                    break;
                case Right:
                    state = STATE.Top;
                    break;
                case Bottom:
                    state = STATE.Right;
                    break;
            }
            move = MOVE.Face;
        } else if (cmd.equals("the_right") || cmd.equals("the_right_loop")) {
            switch (state){
                case Top:
                    state = STATE.Right;
                    break;
                case Left:
                    state = STATE.Top;
                    break;
                case Right:
                    state = STATE.Bottom;
                    break;
                case Bottom:
                    state = STATE.Left;
                    break;
            }
            move = MOVE.Face;
        } else if (cmd.equals("the_jump") || cmd.equals("the_jump_loop")) {
            move = MOVE.Jump;
        } else if (cmd.equals("the_loop")) {

        }
    }

    public interface OnCmdResultListener {
        void OnCmdSucceed();

        void OnCmdNext();

        void OnError();
    }
}
