package com.jumpbraid.engine.utils.texts;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.utils.Rect;
import com.jumpbraid.engine.utils.Recursos;

public class TextManager implements IGameloop {

    private static TextManager instance = null;
    // atributos ---------------------------------------------------------
    private boolean ativadorGlobal; // para verificar se pode exibir texto
    private boolean[] ativadores; // para verificar qual texto será renderizado
    private TextBox[] textBoxes; // quadros de textos a serem renderizados
    private Rect[] caracteres; // arrays com as dimensões de recorte de todos os caracteres de texto
    private final String arquivoFonte = "fontBase01.fnt"; // nome do arquivo com os dados de recorte da fonte
    private final String imagemFonte = "fontBase01.png"; // nome do arquivo da imagem da fonte; 
    private Texture imagem;

    // contrutor ---------------------------------------------------------
    private TextManager(){}

    public static TextManager getInstance(){
        if(instance==null)
            instance = new TextManager();
        return instance;
    }

    // metodos gameloop ------------------------------------------------------
    @Override
    public void handlerEvents() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render() {
        if(!ativadorGlobal) return; // não for a hora de exibir texto, abandona
        // percorre os ativadores de cada texto individual
        for(short i=0;i<ativadores.length;i++){
            if(!ativadores[i]) continue; // se não for hora de exibir o texto, abandona
            // exibe o diálogo de texto.
            textBoxes[i].render();
        }
        
    }

    // metodos de ativação -----------------------------------------------
    public void ativarExibicao(){
        ativadorGlobal=true;
    }
    public void desativarExibicao(){
        ativadorGlobal=false;
    }
    public void ativarTexto(short index){
        ativadores[index]=true;
    } 
    public void desativarTexto(short index){
        ativadores[index]=false;
    }
    public void ativarTodos(){
        for(short i=0;i<ativadores.length;i++)
            ativadores[i]=true;
    }
    public void desativarTodos(){
        for(short i=0;i<ativadores.length;i++)
            ativadores[i]=false;
    } 

    // metodos ---------------------------------------------------------
    /* recebe um array de textos, para cada um dos diálogos da cena*/
    public void carregarTextosCena(String arquivo,String id){
		try {
			// carrega o arquivo de texto com os diálogos
			String[] completo = Recursos.carregarArquivoTexto(arquivo);
			ArrayList<String> selecionados = new ArrayList<>();

			// realiza a filtragem dos textos que correspondem ao identificados 'id'
			for(short i=0;i<completo.length;i++){
				if(completo[i].equals("")) continue;
				String line = completo[i];
				if(line.charAt(0)=='#'&&line.substring(1).equals(id)){ // se encontrou o marcador id
					short j =(short)(i+1);
					while(j<completo.length&&completo[j]!=""&&completo[j].charAt(0)=='*'){
						selecionados.add(completo[j++].substring(1));
					}
					break;
				}
			}

			// converte os textos filtrados para um array simples
			String[] arraySelecionados = new String[selecionados.size()];
			String[] textos = selecionados.toArray(arraySelecionados);
            textBoxes = new TextBox[textos.length]; // inicializa o array de diálogos
            ativadores = new boolean[textos.length]; // inicializa o array de ativadores dos diálogos
            
            // inicializa cada um dos diálogos com seus respectivos textos
            for(short i=0;i<textBoxes.length;i++){
                //cria cada um dos quadros de texto, atribuindo-lhes os seus respectivos textos
                textBoxes[i] = new TextBox(textos[i]);
            }
            carregarCaracteres();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void carregarCaracteres(){
        caracteres = new Rect[256]; // inicializa o array para os 256 caracteres da tabela ASCII
        // caregar o arquivo com os dados de recorte de cada caracter
        String[] completo = Recursos.carregarArquivoTexto(arquivoFonte);
        // faz a leitura do nome da imagem usada para os caracteres
        String nomeImagem = (completo[2]
            .substring(completo[2].indexOf('"')+1))
            .substring(0, completo[2].indexOf('"')-1);
        // carrega a imagem a partir do arquivo
        imagem = Recursos.carregarImagem(nomeImagem);
        // realiza o desmembramento do arquivo de recorte dos caracteres
        for(short i=4; i<completo.length; i++){
            if(!completo[i].substring(0,4).equals("char")) continue;
            String line = completo[i];
            // separa as palavras separadas por espaço de cada linha
            String[] palavras = line.split("\\s+");
            // captura o id do caracteres e suas coordenadas de recorte
            short id = Short.valueOf(palavras[1].substring(3, palavras[1].length()));
            short x = Short.valueOf(palavras[2].substring(2,palavras[2].length()));
            short y = Short.valueOf(palavras[3].substring(2,palavras[3].length()));
            short width = Short.valueOf(palavras[4].substring(6,palavras[4].length()));
            short height = Short.valueOf(palavras[5].substring(7,palavras[5].length()));
            // coloca as coordenadas de recorte no seu respectivo id
            caracteres[id] = new Rect(x, y,(short)(x+width),(short)(y+height));
        }
    }
}
