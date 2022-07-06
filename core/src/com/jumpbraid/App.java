package com.jumpbraid;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.scene.SceneManager;
import com.jumpbraid.engine.utils.Recursos;
import com.jumpbraid.engine.utils.Scenes.Tempo;
import com.jumpbraid.game.Personagem;
import com.jumpbraid.game.telas.levels.Level_01;
import com.jumpbraid.game.telas.levels.Level_02;

public class App extends ApplicationAdapter {
	public static final int LARGURA_TELA = 426;
	public static final int ALTURA_TELA = 240;
	
	// elementos do jogo
	public OrthographicCamera camera;
    public FitViewport m_viewport;
	public boolean k_touch;
    private int interval = 17;
	long tempoInicio,tempoFinal;
	
	// elementos da cena
	Texture t1;
	Person person;
	SceneManager sceneManager;
	
	@Override
	public void create () {
		Recursos.getInstance().initRecursos(LARGURA_TELA,ALTURA_TELA);
		person = new Personagem();
		sceneManager = new SceneManager(person);

		camera = new OrthographicCamera();
        camera.setToOrtho(true,LARGURA_TELA,ALTURA_TELA);
		camera.update(); 
		m_viewport = new FitViewport(LARGURA_TELA, ALTURA_TELA,camera);
		Gdx.input.setInputProcessor(new EventoEntrada()); // gerenciador de eventos
		tempoInicio=tempoFinal=TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		
		// 01 gerencia os eventos ----------------------------------------------
		sceneManager.handlerEvents();
		// 01 fim --------------------------------------------------------------
		
		// 02 atualiza ---------------------------------------------------------
		tempoInicio = TimeUtils.nanoTime();
        Recursos.tempoDelta = tempoInicio-tempoFinal;

		sceneManager.update();
		
		tempoFinal=tempoInicio;
		// 02 fim --------------------------------------------------------------

		// 03 desenha tudo na tela ---------------------------------------------
		ScreenUtils.clear(0, 0, 0, 1);
		Recursos.batch.setProjectionMatrix(camera.combined);
		Recursos.batch.begin(); // inicio --------------------
		
		sceneManager.render();
		
		Recursos.batch.end(); // fim -------------------------

		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 03 fim --------------------------------------------------------------
	}

	
	@Override
	public void dispose () {
		Recursos.batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		m_viewport.update(width, height);
	}

	// EVENTOS ******************************************************
	public class EventoEntrada extends InputAdapter{
		public boolean keyDown (int keycode) {
			switch (keycode) {
			case Keys.UP:
                Recursos.getInstance().keyState.k_cima = true;
                break;
            case Keys.DOWN:
                Recursos.getInstance().keyState.k_baixo = true;
                break;
            case Keys.LEFT:
                Recursos.getInstance().keyState.k_esquerda = true;
                break;
            case Keys.RIGHT:
                Recursos.getInstance().keyState.k_direita = true;
                break;
            case Keys.A:
                Recursos.getInstance().keyState.k_atirando = true;
                break;
            case Keys.NUM_1:
                interval=0;
                break;
            case Keys.NUM_2:
                interval=100;
                break;
            case Keys.NUM_3:
                interval=600;
                break;
			case Keys.NUM_4:
				SceneManager.iniciarTransicaoCena(Level_01.class.getName(),Tempo.RAPIDO);
                break;
			case Keys.NUM_5:
				SceneManager.iniciarTransicaoCena(Level_02.class.getName(),Tempo.RAPIDO);
                break;
			}
			return true;
		}
		public boolean keyUp (int keycode) {
			switch (keycode) {
				case Keys.UP:
					Recursos.getInstance().keyState.k_cima = false;
					break;
				case Keys.DOWN:
					Recursos.getInstance().keyState.k_baixo = false;
					break;
				case Keys.LEFT:
					Recursos.getInstance().keyState.k_esquerda = false;
					break;
				case Keys.RIGHT:
					Recursos.getInstance().keyState.k_direita = false;
					break;
				case Keys.A:
					Recursos.getInstance().keyState.k_atirando = false;
					break;
				}
				return true;
		}
		public boolean touchDown (int screenX, int screenY, int pointer, int button) {
			// if(screenX>LARGURA_TELA/2)
			// 	Recursos.getInstance().keyState.k_direita = true;
			// else if(screenX<LARGURA_TELA/2)
			// 	Recursos.getInstance().keyState.k_esquerda = true;

			// if(screenY>ALTURA_TELA/2)
			// 	Recursos.getInstance().keyState.k_baixo = true;
			// else if(screenY<ALTURA_TELA/2)
			// 	Recursos.getInstance().keyState.k_cima = true;
			return true;
		}
		
		public boolean touchUp (int screenX, int screenY, int pointer, int button) {
			if(Recursos.ESTADO==EstadoJogo.EXECUTANDO)
				SceneManager.iniciarTransicaoCena(Level_01.class.getName(),Tempo.RAPIDO);
			// Recursos.getInstance().keyState.k_direita = false;
			// Recursos.getInstance().keyState.k_esquerda = false;
			// Recursos.getInstance().keyState.k_baixo = false;
			// Recursos.getInstance().keyState.k_cima = false;
			return true;
		}
	}

	// métodos ----------------------------------------------------------

}
