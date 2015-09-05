package com.myniotech.motor;

import android.util.Log;

import org.puredata.core.PdBase;

import AndGraph.AGGameManager;
import AndGraph.AGInputManager;
import AndGraph.AGScene;
import AndGraph.AGSprite;

public class Game extends AGScene {

    public static String TAG = "Motor";

    AGSprite vrBtnAccel = null;
    AGSprite vrBtnDecel = null;
    AGSprite vrBtnOn = null;
    AGSprite vrBtnOff = null;

    boolean primeiroclique = true;

    float accel = 8;

    private void sendAccel() {
        if (accel < 50) {
            accel = accel + 0.3f;
            PdBase.sendFloat("accel", accel);
        }
    }

    private void sendDecel() {
        if (accel > 1) {
            accel = accel - 0.3f;
            PdBase.sendFloat("accel", accel);
        }
    }

    private void motorOn() {
        accel = 8;
        PdBase.sendBang("on");
    }

    private void motorOff() {
        accel = 8;
        PdBase.sendBang("off");
    }


    public Game(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {

        vrBtnDecel = this.createSprite(R.drawable.decel, 1, 1);
        vrBtnDecel.setScreenPercent(50, 25);
        vrBtnDecel.vrPosition.fX = vrBtnDecel.getSpriteWidth() / 2;
        vrBtnDecel.vrPosition.fY = vrBtnDecel.getSpriteHeight() / 2;

        vrBtnAccel = this.createSprite(R.drawable.accel, 1, 1);
        vrBtnAccel.setScreenPercent(50, 25);
        vrBtnAccel.vrPosition.fX = vrBtnAccel.getSpriteWidth() * 1.5f;
        vrBtnAccel.vrPosition.fY = vrBtnDecel.vrPosition.getY();


        vrBtnOff = this.createSprite(R.drawable.off, 1, 1);
        vrBtnOff.setScreenPercent(50, 25);
        vrBtnOff.vrPosition.fX = vrBtnOff.getSpriteWidth() / 2;
        vrBtnOff.vrPosition.fY = vrBtnDecel.vrPosition.getY() + vrBtnOff.getSpriteHeight();

        vrBtnOn = this.createSprite(R.drawable.on, 1, 1);
        vrBtnOn.setScreenPercent(50, 25);
        vrBtnOn.vrPosition.fX = vrBtnOn.getSpriteWidth() * 1.5f;
        vrBtnOn.vrPosition.fY = vrBtnAccel.vrPosition.getY() + vrBtnOn.getSpriteHeight();

    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {


        if (primeiroclique)
            primeiroclique = false;

        if (AGInputManager.vrTouchEvents.backButtonClicked()) {
            motorOff();
            vrGameManager.setCurrentScene(GameActivity.MENU);
        }

        //verifica se foi clicado dentro do sprite
        if (AGInputManager.vrTouchEvents.screenClicked() && vrBtnOn.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            motorOn();
            Log.d(TAG, "On");
            return;
        }

        //verifica se foi clicado dentro do sprite
        if (AGInputManager.vrTouchEvents.screenClicked() && vrBtnOff.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            motorOff();
            Log.d(TAG, "off");
            return;
        }

        //verifica se foi clicado dentro do sprite
        if ((AGInputManager.vrTouchEvents.screenDown() || AGInputManager.vrTouchEvents.screenDragged())  && vrBtnAccel.collide(AGInputManager.vrTouchEvents.getLastPosition()))

        {
            sendAccel();
            Log.d(TAG, "accel" + accel);
            return;
        }

        //verifica se foi clicado dentro do sprite
        if ((AGInputManager.vrTouchEvents.screenDown() || AGInputManager.vrTouchEvents.screenDragged()) && vrBtnDecel.collide(AGInputManager.vrTouchEvents.getLastPosition()))

        {
            sendDecel();
            Log.d(TAG, "decel");
            return;
        }


    }

}
