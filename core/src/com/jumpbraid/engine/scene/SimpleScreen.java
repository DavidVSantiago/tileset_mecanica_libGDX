package com.jumpbraid.engine.scene;

import com.badlogic.gdx.graphics.Texture;
import com.jumpbraid.engine.utils.Recursos;

public class SimpleScreen extends Scene{
    // atributos ----------------------------------------------
    protected Texture img;

    // construtor ---------------------------------------------
    public SimpleScreen(Texture img) {
      this.img = img;
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
      Recursos.batch.draw(img, 0, 0,
                            Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                            0,0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                            false,true);
    }
}
