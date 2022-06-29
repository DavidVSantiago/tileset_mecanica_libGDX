package com.jumpbraid.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.jumpbraid.engine.scene.Level;

public class Recursos {
	private static Recursos singleton = null;
	// atributos
	public int LARGURA_TELA;
	public int ALTURA_TELA;
	public Camera camera;
	public KeyState keyState;
	public boolean permiteMoverH,permiteMoverV;
	public long quadroAtual,contadorQuadros;

	private Recursos() {
	}
	// Métodos --------------------------------------------
	public void initRecursos(int larguraTela,int alturaTela){
		LARGURA_TELA = larguraTela;
		ALTURA_TELA = alturaTela;
		camera = new Camera(0,0,larguraTela,alturaTela);
		keyState = new KeyState();
		permiteMoverH = true;
		permiteMoverV = true;
		contadorQuadros = 0;
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