package com.jumpbraid.game.telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.jumpbraid.engine.scene.SimpleScreen;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes;
import com.jumpbraid.engine.utils.Scenes.Tempo;

public class MenuScreen extends SimpleScreen{
    // atributos ----------------------------------------------
    // nível que o menu se encontra
    private MenuLevel menuLevel;
    private double tempoPermanencia = Tempo.LENTO.getValue();
    private Texture seletorImage;
    private short seletorX, seletorY, seletorStartY,seletorStartX, spaceY, qtdSeletores;

    // construtor ---------------------------------------------
    public MenuScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.MENU.toString()));
      menuLevel = menuLevel.MAIN;
      // define os textos da cena
      Recursos.textManager.carregarTextosCena("fases.txt", "menu");
      Recursos.textManager.carregarCaracteres("fontBase01.fnt");
      Recursos.textManager.setAtivacaoExibicao(true); // para permitir que o texto seja exibido
      // carrega a imagem do seletor
      seletorImage = Recursos.carregarImagem("menu-opt.png");

      // define os valores de posicionamento dos seletores
      seletorStartX = 164;
      seletorStartY=120;
      seletorX = seletorStartX;
      seletorY = seletorStartY;
      spaceY = 20;
      qtdSeletores = 3;

      // define as posições dos textos das telas do menu
      posicionaMenu01(); // menu principal
      posicionaMenu02(); // menu de opções

      setVisibleTextos01(true);

      // pre renderiza os textos (deve ser colocado por último)
      Recursos.textManager.preparaTextBoxes();
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void doHandlerEvents() {
      if(Recursos.keyState.k_cima){
        seletorY-=spaceY;
        Recursos.keyState.k_cima=false;
        Recursos.keyState.k_baixo=false;
        if(seletorY<seletorStartY)seletorY+=spaceY; // correção, desfaz o movimento
      }if(Recursos.keyState.k_baixo){
        seletorY+=spaceY;
        Recursos.keyState.k_cima=false;
        Recursos.keyState.k_baixo=false;
        if(seletorY>seletorStartY+(spaceY*(qtdSeletores-1)))seletorY-=spaceY; // correção, desfaz o movimento
      }if(Recursos.keyState.k_atirando){
        Recursos.keyState.k_atirando=false;
        if(menuLevel==MenuLevel.MAIN){ // tela principal do menu
          if(seletorY==seletorStartY){ // pressionou start
            System.out.println("START");
          }else if(seletorY==seletorStartY+spaceY){
            System.out.println("OTIONS");
            mostrarCena02();
          }else if(seletorY==seletorStartY+(spaceY*2)){
            System.out.println("QUIT GAME");
            Gdx .app.exit();
          }
        }else if(menuLevel==MenuLevel.OPTIONS){ // tela de opções do menu
          if(seletorY==seletorStartY){ // pressionou dificult
            System.out.println("DIFICULT");
          }else if(seletorY==seletorStartY+spaceY){
            System.out.println("BACK");
            mostrarCena01();
          }
        }
      }
    }

    @Override
    public void doUpdate() {
      
    }

    @Override
    public void doRender() {
      Recursos.textManager.render(); // renderiza os textos da cena
      Recursos.batch.draw(seletorImage, seletorX, seletorY);
    }

    @Override
    public void disposeScene(){
      // colocar os métodos de limpeza da cena
    }

    // métodos de exibição de cena ----------------------------------------
    public void mostrarCena02(){
      menuLevel=MenuLevel.OPTIONS;
      setVisibleTextos01(false);
      setVisibleTextos02(true);
      qtdSeletores=2;
      seletorStartX-=10;
      seletorX=seletorStartX;
      seletorStartY+=10;
      seletorY=seletorStartY;
    }
    public void mostrarCena01(){
      menuLevel=MenuLevel.MAIN;
      setVisibleTextos02(false);
      setVisibleTextos01(true);
      qtdSeletores=3;
      seletorStartX+=10;
      seletorX=seletorStartX;
      seletorStartY-=10;
      seletorY=seletorStartY;
    }

    // métodos de posicionamento ------------------------------------------
    /* define o posicionamentos dos textos da tela principal do menu */
    public void posicionaMenu01(){
      // define as posições dos textos
      Recursos.textManager.textBoxes[0].posX=
      Recursos.textManager.textBoxes[1].posX=
      Recursos.textManager.textBoxes[2].posX=(short)(seletorStartX+16);
      for(short i=0;i<qtdSeletores;i++){
        Recursos.textManager.textBoxes[i].posY=(short)(seletorStartY-3+(spaceY*i));
      }
    }
    /* define o posicionamentos dos textos da tela de opções do menu */
    public void posicionaMenu02(){
      // define as posições dos textos
      Recursos.textManager.textBoxes[3].posX=
      Recursos.textManager.textBoxes[4].posX=
      Recursos.textManager.textBoxes[5].posX=
      Recursos.textManager.textBoxes[6].posX=
      Recursos.textManager.textBoxes[7].posX=(short)(seletorStartX+6);
      Recursos.textManager.textBoxes[3].posY=(short)(seletorStartY+7);
      Recursos.textManager.textBoxes[7].posY=(short)(seletorStartY+7+spaceY);
    }

    // métodos de ativação ------------------------------------------------
    public void setVisibleTextos01(boolean value){
      Recursos.textManager.setAtivacaoTexto(0,value);
      Recursos.textManager.setAtivacaoTexto(1,value);
      Recursos.textManager.setAtivacaoTexto(2,value);
    }
    public void setVisibleTextos02(boolean value){
      Recursos.textManager.setAtivacaoTexto(3,value);
      Recursos.textManager.setAtivacaoTexto(4,value);
      Recursos.textManager.setAtivacaoTexto(5,value);
      Recursos.textManager.setAtivacaoTexto(6,value);
      Recursos.textManager.setAtivacaoTexto(7,value);
    }

    private enum MenuLevel{
      MAIN, OPTIONS;
    }
    
}
