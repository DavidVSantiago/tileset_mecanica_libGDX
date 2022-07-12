package com.jumpbraid.game.telas;

import com.badlogic.gdx.graphics.Texture;
import com.jumpbraid.engine.scene.SimpleScreen;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes;
import com.jumpbraid.engine.utils.Scenes.Tempo;

public class MenuScreen extends SimpleScreen{
    // atributos ----------------------------------------------
    private double tempoPermanencia = Tempo.LENTO.getValue();
    private Texture seletor;
    private short seletorX, seletorY;

    // construtor ---------------------------------------------
    public MenuScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.MENU.toString()));
      // define os textos da cena
      Recursos.textManager.carregarTextosCena("fases.txt", "menu");
      Recursos.textManager.carregarCaracteres("fontBase01.fnt");
      Recursos.textManager.ativarExibicao(); // para permitir que o texto seja exibido
      // ativa a renderização dos primeiros textos da cena
      Recursos.textManager.ativarTexto(0);
      Recursos.textManager.ativarTexto(1);
      Recursos.textManager.ativarTexto(2);
      // define as posições dos textos 
      Recursos.textManager.textBoxes[0].posX=
      Recursos.textManager.textBoxes[1].posX=
      Recursos.textManager.textBoxes[2].posX=180;
      Recursos.textManager.textBoxes[0].posY=77;
      Recursos.textManager.textBoxes[1].posY=97;
      Recursos.textManager.textBoxes[2].posY=117;
      // pre renderiza os textos (deve ser colocado por último)
      Recursos.textManager.preparaTextBoxes();
      // carrega a imagem do seletor
      seletor = Recursos.carregarImagem("menu-opt.png");
      seletorX = 164;
      seletorY = 80;
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void doHandlerEvents() {
      if(Recursos.keyState.k_cima){
        seletorY-=20;
        Recursos.keyState.k_cima=false;
        Recursos.keyState.k_baixo=false;
        if(seletorY<80)seletorY+=20;
      }
      if(Recursos.keyState.k_baixo){
        seletorY+=20;
        Recursos.keyState.k_cima=false;
        Recursos.keyState.k_baixo=false;
        if(seletorY>120)seletorY-=20;
      }
    }

    @Override
    public void doUpdate() {
      Recursos.tempoAcumulado += Recursos.tempoDelta;
      if (Recursos.tempoAcumulado >= tempoPermanencia) {
        Recursos.tempoAcumulado = 0L;
        // fade out da tela de splash
        //SceneManager.iniciarTransicaoCena(PrologueScreen.class.getName(),Tempo.MEDIO);
      }
    }

    @Override
    public void doRender() {
      Recursos.textManager.render(); // renderiza os textos da cena
      Recursos.batch.draw(seletor, seletorX, seletorY);
    }

    @Override
    public void disposeScene(){
      // colocar os métodos de limpeza da cena
    }
    
}
