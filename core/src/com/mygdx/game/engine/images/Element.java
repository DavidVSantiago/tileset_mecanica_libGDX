package com.mygdx.game.engine.images;

import com.mygdx.game.engine.game.IGameloop;

public abstract class Element implements IGameloop{
    // atributos -------------------------------------------------------
    public float posX, posY; // posição de desenho do layer
    public float velX, velY;

    // construtor -------------------------------------------------------
    public Element(){
        posX=posY=0;
        velX=velY=0;
    }

    // métodos gameloop -------------------------------------------------
    @Override
    public void update() {
        posX+=velX;
        posY+=velY;
    }

    // métodos ----------------------------------------------------------
}