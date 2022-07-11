package com.jumpbraid.game.telas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jumpbraid.engine.scene.Scene;
import com.jumpbraid.engine.utils.Recursos;

public class BlackScreen extends Scene{
    // atributos ----------------------------------------------
    Texture telaPreta;

    // construtor ---------------------------------------------
    public BlackScreen() {
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void handlerEvents() {
        
    }

    @Override
    public void update() {
    }

    @Override
    public void render() {
    }

    @Override
    public void disposeScene(){
      // colocar os métodos de limpeza da cena
    }
}
