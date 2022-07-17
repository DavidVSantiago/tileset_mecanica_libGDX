package com.mygdx.game.game.telas;

import com.mygdx.game.engine.scene.SceneManager;
import com.mygdx.game.engine.scene.SimpleScreen;
import com.mygdx.game.engine.utils.Recursos;
import com.mygdx.game.engine.utils.Scenes;
import com.mygdx.game.engine.utils.Scenes.Tempo;
import com.mygdx.game.game.telas.levels.Level_01;

public class PrologueScreen extends SimpleScreen{
    // atributos ----------------------------------------------
    private double tempoPermanencia = Tempo.LENTO.getValue();
    
    // construtor ---------------------------------------------
    public PrologueScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.PROLOGUE.toString()));
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void doHandlerEvents() {
        
    }

    @Override
    public void doUpdate() {
      Recursos.tempoAcumulado += Recursos.tempoDelta;
      if (Recursos.tempoAcumulado >= tempoPermanencia) {
        Recursos.tempoAcumulado = 0L;
        // fade out da tela de splash
        SceneManager.iniciarTransicaoCena(Level_01.class.getName(),Tempo.RAPIDO);
      }
    }

    @Override
    public void doRender() {
      
    }

    @Override
    public void disposeScene(){
      // colocar os métodos de limpeza da cena
    }
    
}
