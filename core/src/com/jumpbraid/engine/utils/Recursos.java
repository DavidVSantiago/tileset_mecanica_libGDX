package com.jumpbraid.engine.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.game.Personagem;

public class Recursos {
	private static Recursos singleton = null;
	// atributos --------------
	public static int qtdQuadrosGameOver = 100;
	public static int qtdQuadrosMorto = 50;
	public int LARGURA_TELA;
	public int ALTURA_TELA;
	public Camera camera;
	public KeyState keyState;
	public boolean permiteMoverH,permiteMoverV;
	public Person person;
	public int vidas;
	public static EstadoJogo ESTADO;
	public SpriteBatch batch;
	public static String dirIMGS = "imgs/";

	public long tempoDelta, tempoAcumulado;

	// Profiling --------------------
	private static Runtime rt = Runtime.getRuntime();
    private static double total_mem,free_mem, used_mem;

	// Imagens de fundo dos cenários usados no jogo ----------------------
	public Texture fundo_cenario_01;
	// Imagens dos tilesets usados no jogo -------------------------------
	public Map<String,Texture> tilesetImages;
	// Imagens dos NPCs
	public Texture charset,caixaColisao,caixaMove;
	// Imagens das telas
	public Texture telaGameOver,telaLoading;

	// Threads
	private static Thread thread;

	private Recursos() {
	}
	// Métodos --------------------------------------------
	public void initRecursos(int larguraTela,int alturaTela){
		batch = new SpriteBatch();
		tempoDelta=0L;
		carregarImagens();
		LARGURA_TELA = larguraTela;
		ALTURA_TELA = alturaTela;
		camera = new Camera(50,0,larguraTela,alturaTela);
		keyState = new KeyState();
		permiteMoverH = true;
		permiteMoverV = true;
		vidas = 3;
		ESTADO = EstadoJogo.EXECUTANDO;
		person = new Personagem();
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

    public static JsonValue carregarJson(String arquivo) throws NullPointerException{
		JsonReader json = new JsonReader();
		JsonValue base = json.parse(Gdx.files.internal(arquivo));
		return base;
	}
	//

	public void carregarImagens(){
		// carrega as imagens de fundo dos cenários
		fundo_cenario_01 = new Texture("imgs/fundo_cenario_01.png");
		// carrega as imagens dos tilesets usados nos cenários
		tilesetImages = new HashMap<>();
		tilesetImages.put("tileset_sky_02", new Texture("imgs/tileset_sky_02.png"));
		tilesetImages.put("tileset", new Texture("imgs/tileset.png"));
		// carrega as imagens dos NPCs
		charset = new Texture("imgs/charset.png");
		caixaColisao = new Texture("imgs/caixaColisao.png");
		caixaMove = new Texture("imgs/caixaMove.png");
		// carrega as imagens das telas
		telaGameOver = new Texture("imgs/game_over.png");
		telaLoading = new Texture("imgs/loading.png");
	}

	public static void printUsedMemory(){
		total_mem = rt.totalMemory();
    	free_mem = rt.freeMemory();
   		used_mem = (total_mem - free_mem)* 1e-6;
		System.out.println("Amount of used memory: " + used_mem+"MB");
	}
}