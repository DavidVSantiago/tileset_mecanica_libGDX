package com.jumpbraid.engine.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.scene.tile.TileLevel;
import com.jumpbraid.engine.utils.Levels;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.game.Fase;

/** Tem o objetivo de gerenciar as cenas do jogo */
public class SceneManager implements IGameloop{
    // atributos ----------------------------------------------
    private static Scene cenaAtual, cenaGameOver, cenaLoading, temp[];
    private static Thread thread;
    private static double tempoTransicaoLevel,tempoGameOver,tempoFadeOut;
    // ativadores de transição
    public static boolean _ativador_level,_ativador_transicao_level;
    public static boolean _ativador_level_reinicio;
    public static String dirIMGS = "imgs/";
    long tempoAcumulado;
    // levels
    private TileLevel novoLevel = null;
    
    // construtor ---------------------------------------------
    public SceneManager(Person person) {
        cenaGameOver = new SimpleScreen(Recursos.getInstance().telaGameOver);
        cenaLoading = new SimpleScreen(Recursos.getInstance().telaLoading);
        cenaAtual = new Fase("imgs/cenario_01.tmj",Recursos.getInstance().fundo_cenario_01);
        tempoTransicaoLevel = 2e+9;
        tempoFadeOut = 1e+9;
        tempoGameOver = 3000;
        tempoAcumulado=0L;
    }

    // métodos gameloop ------------------------------------------------
    
    @Override
    public void handlerEvents() {
        cenaAtual.handlerEvents();
    }

    @Override
    public void update(long tempoDelta) {

        // resolve os temporizadores de transição dos levels do jogo
        if(_ativador_level) temporizadorTransicaoLevel(tempoDelta);

        // resolve o temporizador de transição de reinicio do level
        if(_ativador_level_reinicio) temporizadorLevelReinicio(tempoDelta);

        cenaAtual.update(tempoDelta);
        
        //Recursos.printUsedMemory();
    }

    @Override
    public void render(SpriteBatch batch,long tempoDelta) {
        if(_ativador_transicao_level){
            tempoAcumulado+=tempoDelta;
            if(tempoAcumulado>=tempoFadeOut){

            }
        }
        cenaAtual.render(batch,tempoDelta);
    }

    // Métodos de transição de level -----------------------------------
    public static void iniciarTransicaoLevel(Levels levelDestino){
        Recursos.NOME_LEVEL_DESTINO = levelDestino;
        _ativador_transicao_level=true;
    }

    // Temporizadores de transição de level -----------------------------------
	private void temporizadorTransicaoLevel(long tempoDelta){
        if(cenaAtual == cenaLoading && Recursos.ESTADO!=EstadoJogo.CARREGANDO){
            Recursos.ESTADO = EstadoJogo.CARREGANDO;
            novoLevel = new Fase(dirIMGS+Recursos.NOME_LEVEL_DESTINO,Recursos.getInstance().fundo_cenario_01);
        }
        if(tempoAcumulado==0)
            cenaAtual = cenaLoading;
        
        tempoAcumulado+=tempoDelta;
        if(tempoAcumulado>=tempoTransicaoLevel){ // acumulou tempo total da transição
            tempoAcumulado=0;
            cenaAtual = novoLevel;
            _ativador_level=false;
            Recursos.ESTADO =EstadoJogo.EXECUTANDO;
        }
    }
    private void temporizadorLevelReinicio(long tempoDelta){
     
    }
    public static void transicaoParaGameOver(final String level){
		thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long tempoInicio = TimeUtils.nanoTime();
                    cenaAtual = cenaGameOver;
                    Recursos.ESTADO = EstadoJogo.CARREGANDO;
                    Recursos.getInstance().person.reinicia();
                    TileLevel novoLevel=null;
                    for(int i=0;i<10;i++){
                        novoLevel = new Fase("imgs/"+level,Recursos.getInstance().fundo_cenario_01);
                    }
                    long tempoFim = TimeUtils.nanoTime();
                    double tempoDelta = (tempoFim-tempoInicio)*1e-6;
                    long tempoEspera = (long)(tempoGameOver-tempoDelta);
                    System.out.println(tempoEspera);
                    if(tempoEspera > 0)
                        Thread.sleep(tempoEspera);
                    cenaAtual = novoLevel;
                    Recursos.ESTADO = EstadoJogo.EXECUTANDO;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
		thread.start();
    }

}