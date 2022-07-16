package com.jumpbraid.engine.scene;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes.Tempo;
import com.jumpbraid.game.telas.BlackScreen;
import com.jumpbraid.game.telas.MenuScreen;
import com.jumpbraid.game.telas.SplashScreen;

/** Tem o objetivo de gerenciar as cenas do jogo */
public class SceneManager implements IGameloop{
    // atributos do overlay preto de transição

    public static Sprite overlayPreto;
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
        // inicializaçao do overlay preto para a transição de tela
        Pixmap pixmap = new Pixmap( Recursos.getInstance().LARGURA_TELA, Recursos.getInstance().ALTURA_TELA, Pixmap.Format.RGBA8888 );
        pixmap.setColor( 0, 0, 0, 0 );
        pixmap.fillRectangle(0,0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA);
        Texture texture = new Texture(pixmap);
        overlayPreto = new Sprite(texture);
        //pixmap.dispose();
        // 
        telaPreta = new BlackScreen(); // fica sempre carregada durante o gameplay (para auxiliar nas transições)
        cenaAtual = new MenuScreen();
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
        // desenha o overlay preto das transições
        overlayPreto.draw(Recursos.batch);
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
        alpha = alpha>1?1:alpha; // correção para valores maiores que 1
        SceneManager.overlayPreto.setAlpha(alpha);
        if(Recursos.tempoAcumulado>=tempoFade){
            _ativador_cena_fadeOut=false;
            _ativador_cena=true;
            Recursos.tempoAcumulado=0L;
            // libera os recursos da tela
            cenaAtual.disposeScene();
            cenaAtual=null;
            // muda para a tela de espera (tela preta)
            cenaAtual = telaPreta;
        }
    }

    private void temporizadorFadeIn(){
        Recursos.tempoAcumulado+=Recursos.tempoDelta;
        float alpha = (float)(Recursos.tempoAcumulado/tempoFade);
        alpha = alpha>1?1:alpha; // correção para valores maiores que 1
        SceneManager.overlayPreto.setAlpha(1-alpha);
        if(Recursos.tempoAcumulado>=tempoFade){
            _ativador_cena_fadeIn=false;
            Recursos.tempoAcumulado=0L;
        }
    }
    
	private void temporizadorTransicaoCena(){
        // se for a primeira execução desse temporizador
        if(Recursos.ESTADO!=EstadoJogo.CARREGANDO){
            Recursos.ESTADO = EstadoJogo.CARREGANDO;
            try { // ja começa a carregar na memória a nova cena
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