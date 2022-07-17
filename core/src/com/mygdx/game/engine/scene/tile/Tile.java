package com.mygdx.game.engine.scene.tile;

import com.mygdx.game.engine.utils.Rect;

/** Representa um tile */
public class Tile extends Rect{
    // atributos
    public short ID;
    // construtor
    public Tile(short x1, short y1, short x2, short y2, short ID) {
        super(x1,y1,x2,y2);
        this.ID = ID;
    }
}