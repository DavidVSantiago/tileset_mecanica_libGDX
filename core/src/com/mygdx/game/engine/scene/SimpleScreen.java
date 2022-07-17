package com.mygdx.game.engine.scene;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.engine.utils.Recursos;

public abstract class SimpleScreen extends Scene{
    // atributos ----------------------------------------------
    protected Texture img;

    // construtor ---------------------------------------------
    public SimpleScreen(Texture img) {
      this.img = img;
    }

    // métodos do gameloop ------------------------------------------------
    public abstract void doHandlerEvents();
    @Override
    public void handlerEvents() {
        doHandlerEvents();
    }

    public abstract void doUpdate();
    @Override
    public void update() {
      doUpdate();
    }

    public abstract void doRender();
    @Override
    public void render() {
      Recursos.batch.draw(img, 0, 0,
                            Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                            0,0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                            false,true);
      doRender();
    }
}
