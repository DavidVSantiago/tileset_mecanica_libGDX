package com.jumpbraid.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.jumpbraid.engine.game.EstadoJogo;

public class Recursos {
	private static Recursos singleton = null;
	// atributos
	public static int qtdQuadrosGameOver = 100;
	public static int qtdQuadrosMorto = 50;
	public int LARGURA_TELA;
	public int ALTURA_TELA;
	public Camera camera;
	public KeyState keyState;
	public boolean permiteMoverH,permiteMoverV;
	public EstadoJogo ESTADO;
	public int vidas;

	private Recursos() {
	}
	// Métodos --------------------------------------------
	public void initRecursos(int larguraTela,int alturaTela){
		LARGURA_TELA = larguraTela;
		ALTURA_TELA = alturaTela;
		camera = new Camera(50,0,larguraTela,alturaTela);
		keyState = new KeyState();
		permiteMoverH = true;
		permiteMoverV = true;
		vidas = 3;
		ESTADO = EstadoJogo.EXECUTANDO;
	}
	// Métodos de estado --------------------------------------------

	public boolean isEXECUTANDO(){
		return ESTADO==EstadoJogo.EXECUTANDO;
	}
	public boolean isGAMEOVER(){
		return ESTADO==EstadoJogo.GAMEOVER;
	}

	public boolean isMORTO(){
		return ESTADO==EstadoJogo.MORTO;
	}

	// Métodos estáticos --------------------------------------------
	public static Recursos getInstance(){
		if(singleton==null)
			singleton = new Recursos();
		return singleton;
	}

	public static Texture carregarImagem(String file){
		return new Texture(file);
	}

    public static JsonValue carregarJson(String arquivo) throws NullPointerException{
		JsonReader json = new JsonReader();
		JsonValue base = json.parse(Gdx.files.internal(arquivo));
		return base;
	}
}