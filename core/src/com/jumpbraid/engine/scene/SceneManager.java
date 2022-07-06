package com.jumpbraid.engine.scene;

import com.badlogic.gdx.graphics.Color;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes.Tempo;
import com.jumpbraid.game.telas.BlackScreen;
import com.jumpbraid.game.telas.SplashScreen;

/** Tem o objetivo de gerenciar as cenas do jogo */
public class SceneManager implements IGameloop{
    // atributos ----------------------------------------------
    private static Scene cenaAtual, telaPreta;
    private static double tempoTransicao,tempoTransicaoEstado,tempoFade;
    // ativadores de transição
    public static boolean _ativador_cena,_ativador_estado,_ativador_cena_fadeOut,_ativador_cena_fadeIn;
    public static boolean _ativador_level_reinicio;

    
    private Scene novaCena = null;
	public static String nameClassLevelDestino,nameClassTelaDestino;
    private static EstadoJogo NOVO_ESTADO;
    
    // construtor ---------------------------------------------
    public SceneManager(Person person) {
        telaPreta = new BlackScreen();
        cenaAtual = new SplashScreen();
        tempoTransicao = Tempo.MEDIO.getValue();
        tempoFade = Tempo.RAPIDO.getValue();
    }

    // métodos gameloop ------------------------------------------------
    
    @Override
    public void handlerEvents() {
        if(Recursos.ESTADO!=EstadoJogo.CARREGANDO)
            cenaAtual.handlerEvents();
    }

    @Override
    public void update() {


        // resolve os temporizadores de fadeout da transição
        if(_ativador_cena_fadeOut) temporizadorFadeOut();
        else if(_ativador_cena) temporizadorTransicaoCena();
        else if(_ativador_cena_fadeIn) temporizadorFadeIn();
        // resolve os temporizadores de transição
        if(_ativador_estado) temporizadorTransicaoEstado();

        cenaAtual.update();
        //Recursos.printUsedMemory();
    }

    @Override
    public void render() {
        cenaAtual.render();
        System.out.println(Recursos.getInstance().person.bloqueiaTodoMovimento);
    }

    // Métodos de transição -----------------------------------
    
    public static void iniciarTransicaoCena(String nameClassLevelDestino, Tempo tempoFade){
        SceneManager.tempoFade = tempoFade.getValue();
        SceneManager.nameClassLevelDestino = nameClassLevelDestino;
        _ativador_cena_fadeOut=true;
    }

	public static void agendarTransicaoEstadoJogo(double tempo, EstadoJogo ESTADO){
		tempoTransicaoEstado = tempo;
        NOVO_ESTADO = ESTADO;
        _ativador_estado = true;
    }

    // Temporizadores de transição -----------------------------------
    private void temporizadorFadeOut(){
        Recursos.tempoAcumulado+=Recursos.tempoDelta;
        float alpha = (float)(Recursos.tempoAcumulado/tempoFade);
        Color c = Recursos.batch.getColor();
        Recursos.batch.setColor(new Color(c.r, c.g, c.b, 1.0f-alpha));
        if(Recursos.tempoAcumulado>=tempoFade){
            _ativador_cena_fadeOut=false;
            _ativador_cena=true;
            Recursos.tempoAcumulado=0L;
            cenaAtual = telaPreta;
        }
    }
    private void temporizadorFadeIn(){
        Recursos.tempoAcumulado+=Recursos.tempoDelta;
        float alpha = (float)(Recursos.tempoAcumulado/tempoFade);
        Color c = Recursos.batch.getColor();
        Recursos.batch.setColor(new Color(c.r, c.g, c.b, alpha));
        if(Recursos.tempoAcumulado>=tempoFade){
            _ativador_cena_fadeIn=false;
            Recursos.tempoAcumulado=0L;
        }
    }
    
	private void temporizadorTransicaoCena(){
        // se for a primeira execução desse temporizador
        if(Recursos.ESTADO!=EstadoJogo.CARREGANDO){
            Recursos.ESTADO = EstadoJogo.CARREGANDO;
            try {
                novaCena = (Scene) Class.forName(nameClassLevelDestino).getConstructor().newInstance();
            } catch (Exception e) { e.printStackTrace(); }
        }
        
        Recursos.tempoAcumulado+=Recursos.tempoDelta;
        if(Recursos.tempoAcumulado>=tempoTransicao){ // acumulou tempo total da transição
            Recursos.tempoAcumulado=0;
            cenaAtual = novaCena;
            _ativador_cena=false;
            _ativador_cena_fadeIn=true;
            Recursos.ESTADO =EstadoJogo.EXECUTANDO;
        }
    }
    private void temporizadorTransicaoEstado(){
        // se for a primeira execução desse temporizador
        Recursos.tempoAcumulado+=Recursos.tempoDelta;
        if(Recursos.tempoAcumulado>=tempoTransicaoEstado){ // acumulou tempo total da transição
            Recursos.tempoAcumulado=0;
            Recursos.ESTADO = NOVO_ESTADO;
            _ativador_estado=false;
        }
    }

    // ****************************************************
    
}