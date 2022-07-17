package com.mygdx.game.game.telas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.engine.scene.SimpleScreen;
import com.mygdx.game.engine.utils.Recursos;
import com.mygdx.game.engine.utils.Scenes;
import com.mygdx.game.engine.utils.Scenes.Tempo;

public class MenuScreen extends SimpleScreen{
    // atributos ----------------------------------------------
    private MenuLevel menuLevel; // nível que o menu se encontra
    private double tempoPermanencia = Tempo.LENTO.getValue();
    private Texture seletorImage;
    private short seletorX, seletorY, seletorStartY,seletorStartX, spaceY, qtdSeletores;
    // atributos de audio -----------
    Sound menuSound;

    // construtor ---------------------------------------------
    public MenuScreen() {
      super(Recursos.carregarImagem(Scenes.Telas.MENU.toString()));
      menuLevel = menuLevel.MAIN;
      menuSound = Gdx.audio.newSound(Gdx.files.internal("audio/menu.ogg"));
      // carrega a imagem do seletor
      seletorImage = Recursos.carregarImagem("menu-opt.png");
      // define os valores de posicionamento do seletor do menu
      seletorStartX = 164;
      seletorStartY=120;
      seletorX = seletorStartX;
      seletorY = seletorStartY;
      spaceY = 20;
      qtdSeletores = 3;
      
      // 01 - carrega as caixas de texto da cena
      Recursos.textManager.carregaDialogos("fases.txt", "menu","fontBase01.fnt",5);
      // 02 - define as posições das caixas de texto da cena
      Recursos.textManager.textBoxes[0].posX = (short)(seletorStartX+16);
      Recursos.textManager.textBoxes[0].posY = (short)(seletorStartY-4);
      Recursos.textManager.textBoxes[1].posX = (short)(seletorStartX+16);
      Recursos.textManager.textBoxes[1].posY = (short)(seletorStartY+(spaceY)-4);
      Recursos.textManager.textBoxes[2].posX = (short)(seletorStartX+16);
      Recursos.textManager.textBoxes[2].posY = (short)(seletorStartY+(spaceY*2)-4);
      Recursos.textManager.textBoxes[3].posX = (short)(seletorStartX+6);
      Recursos.textManager.textBoxes[3].posY = (short)(seletorStartY+7);
      Recursos.textManager.textBoxes[4].posX = (short)(seletorStartX+90);
      Recursos.textManager.textBoxes[4].posY = (short)(seletorStartY+7);
      Recursos.textManager.textBoxes[5].posX = (short)(seletorStartX+90);
      Recursos.textManager.textBoxes[5].posY = (short)(seletorStartY+7);
      Recursos.textManager.textBoxes[6].posX = (short)(seletorStartX+90);
      Recursos.textManager.textBoxes[6].posY = (short)(seletorStartY+7);
      Recursos.textManager.textBoxes[7].posX = (short)(seletorStartX+6);
      Recursos.textManager.textBoxes[7].posY = (short)(seletorStartY+(spaceY)+7);
      // 03 - prepara os diálogos da cena para serem renderizados
      Recursos.textManager.preparaTextoCena();

      // para permitir que os diálogos possam ser exibidos
      Recursos.textManager.setAtivacaoExibicao(true);

      setVisibleTextos01(true);
    }

    // métodos de colisão ------------------------------------------------
    @Override
    public void doHandlerEvents() {
      if(Recursos.keyState.k_cima){
        
        seletorY-=spaceY;
        Recursos.keyState.k_cima=false;
        Recursos.keyState.k_baixo=false;
        if(seletorY<seletorStartY)
          seletorY+=spaceY; // correção, desfaz o movimento
        else
          tocarSomMenu();
      }if(Recursos.keyState.k_baixo){
        seletorY+=spaceY;
        Recursos.keyState.k_cima=false;
        Recursos.keyState.k_baixo=false;
        if(seletorY>seletorStartY+(spaceY*(qtdSeletores-1)))
          seletorY-=spaceY; // correção, desfaz o movimento
        else
          tocarSomMenu();
      }if(Recursos.keyState.k_atirando){
        Recursos.keyState.k_atirando=false;
        tocarSomMenu();
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
            ativaProximaDificuldade();
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
    public void ativaProximaDificuldade(){
      if(Recursos.textManager.isAtivacaoTexto(6)){ // está na dificuldade EASY
        Recursos.textManager.setAtivacaoTexto(6,false); // desativa EASY
        Recursos.textManager.setAtivacaoTexto(5,true); // ativa NORMAL
      }else if(Recursos.textManager.isAtivacaoTexto(5)){ // esta na dificuldade NORMAL
        Recursos.textManager.setAtivacaoTexto(5,false); // desativa NORMAL
        Recursos.textManager.setAtivacaoTexto(4,true); // ativa HARD
      }else if(Recursos.textManager.isAtivacaoTexto(4)){ // esta na dificuldade HARD
        Recursos.textManager.setAtivacaoTexto(4,false); // desativa HARD
        Recursos.textManager.setAtivacaoTexto(6,true); // ativa EASY
      }
    }

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
      Recursos.textManager.setAtivacaoTexto(5,value);
      Recursos.textManager.setAtivacaoTexto(7,value);
      if(!value){ // correção para desativação
        Recursos.textManager.setAtivacaoTexto(4,value);
        Recursos.textManager.setAtivacaoTexto(6,value);
      }
    }

    // métodos de audio ------------------------------------------------
    public void tocarSomMenu(){
      menuSound.play();
    }

    // outras estruturas ------------------------------------------------
    private enum MenuLevel{
      MAIN, OPTIONS;
    }
    
}
