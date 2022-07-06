package com.jumpbraid.game.telas;

import com.badlogic.gdx.graphics.Texture;
import com.jumpbraid.engine.scene.Scene;
import com.jumpbraid.engine.scene.SceneManager;
import com.jumpbraid.engine.scene.SimpleScreen;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes;
import com.jumpbraid.engine.utils.Scenes.Tempo;
import com.jumpbraid.game.telas.levels.Level_01;

public class PrologueScreen extends SimpleScreen{
    // atributos ----------------------------------------------
    private double tempoPermanencia = Tempo.LENTO.getValue();
    
    // construtor ---------------------------------------------
    public PrologueScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.PROLOGUE.toString()));
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
        SceneManager.iniciarTransicaoCena(Level_01.class.getName(),Tempo.RAPIDO);
      }
    }

    @Override
    public void render() {
      Recursos.batch.draw(img, 0, 0,
                            Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                            0,0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                            false,true);
    }

    


    
}
