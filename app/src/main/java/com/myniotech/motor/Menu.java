package com.myniotech.motor;


import AndGraph.AGGameManager;
import AndGraph.AGInputManager;
import AndGraph.AGScene;
import AndGraph.AGScreenManager;
import AndGraph.AGSprite;

public class Menu extends AGScene {

    AGSprite botaoJogar = null;
    AGSprite botaoSair = null;

    public Menu(AGGameManager pManager) {
        super(pManager);
    }

    //Metodo obrigatorio definido pela cena
    public void init() {

        botaoJogar = createSprite(R.drawable.play, 1, 1);
        botaoJogar.setScreenPercent(100,50);
        botaoJogar.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        botaoJogar.vrPosition.fY = botaoJogar.getSpriteHeight()/2;

        botaoSair = createSprite(R.drawable.out, 1, 1);
        botaoSair.setScreenPercent(100,50);
        botaoSair.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        botaoSair.vrPosition.fY = botaoJogar.getSpriteHeight() * 1.5f ;


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

        if (AGInputManager.vrTouchEvents.screenClicked() && botaoJogar.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.setCurrentScene(GameActivity.GAME);
            return;
        }

        if (AGInputManager.vrTouchEvents.screenClicked() && botaoSair.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.vrActivity.finish();
        }
    }
}