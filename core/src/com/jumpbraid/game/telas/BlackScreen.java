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
        Pixmap pixmap = new Pixmap( 1, 1, Format.RGBA8888 );
        pixmap.setColor( 0, 1, 0, 1 );
        pixmap.fillRectangle(0,0,1,1);
        telaPreta = new Texture(pixmap);
        pixmap.dispose();

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
        Recursos.batch.draw(telaPreta,100,70);
        System.out.println("*");
    }
}
