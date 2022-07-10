package com.jumpbraid.engine.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.jumpbraid.engine.audio.AudioPlayer;
import com.jumpbraid.engine.audio.MidiPlayer;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.utils.texts.TextManager;
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
	public static SpriteBatch batch;
	public static String dirIMGS = "imgs/";
	public static String dirAudio = "audio/";
	public static String dirTexts = "texts/";
	public static long tempoDelta, tempoAcumulado;
	
	// Profiling --------------------
	private static Runtime rt = Runtime.getRuntime();
    private static double total_mem,free_mem, used_mem;
	
	// Imagens dos NPCs
	public Texture charset,caixaColisao,caixaMove;
	
	public static MidiPlayer midiPlayer;
	public static AudioPlayer audioPlayer;
	public static TextManager textManager;

	private Recursos() {
	}
	// Métodos --------------------------------------------
	public void initRecursos(int larguraTela,int alturaTela, MidiPlayer midiPlayer, AudioPlayer audioPlayer){
		Recursos.textManager = TextManager.getInstance();
		Recursos.midiPlayer = midiPlayer;
		Recursos.audioPlayer = audioPlayer;
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

	private void carregarImagens(){	
		// carrega as imagens dos NPCs
		charset = carregarImagem("charset.png");
		caixaColisao = carregarImagem("caixaColisao.png");
		caixaMove = carregarImagem("caixaMove.png");
	}

	public static Texture carregarImagem(String arquivo){
		return new Texture(dirIMGS+arquivo);
	}

	public static void printUsedMemory(){
		total_mem = rt.totalMemory();
    	free_mem = rt.freeMemory();
   		used_mem = (total_mem - free_mem)* 1e-6;
		System.out.println("Amount of used memory: " + used_mem+"MB");
	}

	public static String[] carregarArquivoTexto(String arquivo){
		FileHandle fh = Gdx.files.internal(dirTexts+arquivo);
		String bruto = fh.readString();
		// formata o arquivo em um array de linhas
		return bruto.split("\\r?\\n");
	}

	// Métodos estáticos de audio --------------------------------------------
	public static void playMenuMusic(){
		audioPlayer.play();
	}
}