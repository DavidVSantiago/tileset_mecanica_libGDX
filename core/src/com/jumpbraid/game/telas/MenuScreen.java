package com.jumpbraid.game.telas;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.jumpbraid.engine.scene.SceneManager;
import com.jumpbraid.engine.scene.SimpleScreen;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes;
import com.jumpbraid.engine.utils.Scenes.Tempo;

public class MenuScreen extends SimpleScreen{
    // atributos ----------------------------------------------
    private double tempoPermanencia = Tempo.LENTO.getValue();
    BitmapFont font = new BitmapFont(true); //or use alex answer to use custom font

    // construtor ---------------------------------------------
    public MenuScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.MENU.toString()));
      font.setColor(1, 0, 0, 1);
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void handlerEvents() {
        
    }

    @Override
    public void update() {
      Recursos.tempoAcumulado += Recursos.tempoDelta;
      if (Recursos.tempoAcumulado >= tempoPermanencia) {
        Recursos.tempoAcumulado = 0L;
        // fade out da tela de splash
        SceneManager.iniciarTransicaoCena(PrologueScreen.class.getName(),Tempo.MEDIO);
      }
    }

    @Override
    public void render() {
      Recursos.batch.draw(img, 0, 0,
      Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
      0,0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
      false,true);
      font.draw(Recursos.batch, "Hello World!", 10, 10);
    }

    


    
}
