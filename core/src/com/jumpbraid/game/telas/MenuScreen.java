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

    // construtor ---------------------------------------------
    public MenuScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.MENU.toString()));
      // define os textos da cena
      Recursos.textManager.carregarTextosCena("fases.txt", "menu");
      Recursos.textManager.ativarExibicao(); // para permitir que o 
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
        SceneManager.iniciarTransicaoCena(PrologueScreen.class.getName(),Tempo.MEDIO);
      }
    }

    @Override
    public void doRender() {
      
    }

    


    
}
