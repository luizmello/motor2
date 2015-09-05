package com.myniotech.motor;

import AndGraph.AGGameManager;
import AndGraph.AGInputManager;
import AndGraph.AGScene;
import AndGraph.AGScreenManager;
import AndGraph.AGSprite;

/**
 * Created by luiz on 08/08/15.
 */
public class Intro extends AGScene {
    AGSprite vrSpriteUno = null;
    int estado = 0;

    //Construtor
    public Intro(AGGameManager pManager) {
        super(pManager);
    }

    //Metodo obrigatorio definido pela cena
    public void init()


    {

        vrSpriteUno = createSprite(R.drawable.myniotech, 1, 1);
        vrSpriteUno.setScreenPercent(90, 30);
        vrSpriteUno.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        vrSpriteUno.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
        vrSpriteUno.fadeIn(1000);
    }

    //Metodo obrigatorio definido pela cena
    public void restart() {
    }

    //Metodo obrigatorio definido pela cena
    public void stop() {
    }

    //Metodo obrigatorio definido pela cena
    public void loop() {
        if (AGInputManager.vrTouchEvents.backButtonClicked()) {
            vrGameManager.vrActivity.finish();
        }

        if (vrSpriteUno.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.setCurrentScene(GameActivity.MENU);
            return;
        }

        if (estado == 0) {
            if (vrSpriteUno.fadeEnded()) {
                estado = 1;
                vrSpriteUno.fadeOut(2000);
            }
        } else {
            if (vrSpriteUno.fadeEnded()) {
                vrGameManager.setCurrentScene(GameActivity.MENU);
            }
        }
    }
}
