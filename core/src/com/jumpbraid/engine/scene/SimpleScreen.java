package com.jumpbraid.engine.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jumpbraid.engine.images.Element;
import com.jumpbraid.engine.utils.Recursos;

public class SimpleScreen extends Scene{
    // atributos ----------------------------------------------
    public int acumuladorQuadros;
    Texture img;
    
    // construtor ---------------------------------------------
    public SimpleScreen(Texture img) {
      this.img=img;
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void handlerEvents() {
        
    }

    @Override
    public void update(long tempoDelta) {
      
    }

    @Override
    public void render(SpriteBatch batch,long tempoDelta) {
      batch.draw(img, 0, 0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                   0, 0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                  false, true);
    }

    


    
}
