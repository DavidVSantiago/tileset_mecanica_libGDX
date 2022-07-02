package com.jumpbraid.engine.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.scene.tile.TileLevel;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.game.Fase;

/** Tem o objetivo de gerenciar as cenas do jogo */
public class SceneManager implements IGameloop{
    // atributos ----------------------------------------------
    public int acumuladorQuadros,acumuladorQuadrosGameOver;
    TileLevel level;
    SimpleScreen gameOverScreen;
    
    // construtor ---------------------------------------------
    public SceneManager(Person person) {
        acumuladorQuadros=0;
        acumuladorQuadrosGameOver=0;
        // inicializa elementos do cenario
		level = new Fase("cenario_01.tmj","fundo_cenario_01.png","imgs/",person);	
        gameOverScreen = new SimpleScreen("imgs/game_over.png");
    }

    // métodos de colisão ------------------------------------------------
    
    @Override
    public void handlerEvents() {
        level.handlerEvents();
    }

    @Override
    public void update() {
        
        level.update();

        // controla o reinício do jogo, quando o jogador morre
        if(Recursos.getInstance().ESTADO==EstadoJogo.MORTO){
            
            
            if(Recursos.getInstance().vidas==0){ // se acabaram todas as vidas
                Recursos.getInstance().ESTADO = EstadoJogo.GAMEOVER; // entra em game over
            }else if(acumuladorQuadros==Recursos.qtdQuadrosGameOver){
                Recursos.getInstance().vidas--; // se morreu, perde logo uma vida
                level.reiniciaLevel();
                acumuladorQuadros=0;
            }
            acumuladorQuadros++;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(Recursos.getInstance().isGAMEOVER()){
            acumuladorQuadrosGameOver++;
            if(acumuladorQuadrosGameOver>=Recursos.qtdQuadrosGameOver){
                acumuladorQuadrosGameOver=0;
                level.reiniciaLevel();
            }
            gameOverScreen.render(batch);
        }else{
            level.render(batch);
        }
    }

}