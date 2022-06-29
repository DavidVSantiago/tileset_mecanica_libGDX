package com.jumpbraid.engine.scene;

import com.jumpbraid.engine.utils.Rect;

/** Representa um tile */
public class Tile extends Rect{
    // atributos
    public int ID;
    // construtor
    public Tile(int x1, int y1, int x2, int y2, int ID) {
        super(x1,y1,x2,y2);
        this.ID = ID;
    }
}