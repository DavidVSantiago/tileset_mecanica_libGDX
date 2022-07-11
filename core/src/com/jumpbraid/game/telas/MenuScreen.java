package com.jumpbraid.game.telas;

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
      Recursos.textManager.ativarExibicao(); // para permitir que o texto seja exibido
      // ativa a renderização de cada um dos textos
      Recursos.textManager.ativarTexto(0);
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
      Recursos.textManager.render(); // renderiza os textos da cena
    }

    @Override
    public void disposeScene(){
      // colocar os métodos de limpeza da cena
    }
    
}
