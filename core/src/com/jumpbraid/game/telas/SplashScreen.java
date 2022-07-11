package com.jumpbraid.game.telas;

import com.jumpbraid.engine.scene.SceneManager;
import com.jumpbraid.engine.scene.SimpleScreen;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes;
import com.jumpbraid.engine.utils.Scenes.Tempo;

public class SplashScreen extends SimpleScreen{
    // atributos ----------------------------------------------
    private double tempoFadeIn,tempoPermanencia;
    private boolean _ativador_01;

    // construtor ---------------------------------------------
    public SplashScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.SPLASH.toString()));
      tempoFadeIn = Tempo.MEDIO.getValue();
      tempoPermanencia = Tempo.LENTO.getValue();
      _ativador_01=true;
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void doHandlerEvents() {
        
    }

    @Override
    public void doUpdate() {
      if (_ativador_01) { // fade in (inicio da tela de splash)
        Recursos.tempoAcumulado += Recursos.tempoDelta;
        float alpha = (float) (Recursos.tempoAcumulado/tempoFadeIn);
        alpha = alpha>1?1:alpha; // correção para valores maiores que 1
        SceneManager.overlayPreto.setAlpha(1-alpha);
        if (Recursos.tempoAcumulado >= tempoFadeIn) {
          _ativador_01 = false;
          Recursos.tempoAcumulado = 0L;
        }
      } else { // sustenta a exibição da tela de splash
        Recursos.tempoAcumulado += Recursos.tempoDelta;
        if (Recursos.tempoAcumulado >= tempoPermanencia) {
          Recursos.tempoAcumulado = 0L;
          // fade out da tela de splash
          SceneManager.iniciarTransicaoCena(MenuScreen.class.getName(),Tempo.MEDIO);
        }

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
