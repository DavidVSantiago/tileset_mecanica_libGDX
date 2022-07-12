package com.jumpbraid.engine.scene;

import com.jumpbraid.engine.game.IGameloop;

public abstract class Scene implements IGameloop{
    // atributos ----------------------------------------------    
    
    // construtor ---------------------------------------------
    public Scene() {

    }
    // metodos ------------------------------------------------
    public abstract void disposeScene(); // tem que disposar tods as Textures!
}