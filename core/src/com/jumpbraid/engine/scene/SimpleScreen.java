package com.jumpbraid.engine.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jumpbraid.engine.images.Element;
import com.jumpbraid.engine.utils.Recursos;

public class SimpleScreen extends Element{
    // atributos ----------------------------------------------
    public int acumuladorQuadros;
    Texture img;
    
    // construtor ---------------------------------------------
    public SimpleScreen(String arquivo) {
		img = Recursos.carregarImagem(arquivo);
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void handlerEvents() {
        
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(img, 0, 0);
    }


    
}
