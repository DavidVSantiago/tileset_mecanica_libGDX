package com.jumpbraid.engine.utils.texts;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.utils.Rect;
import com.jumpbraid.engine.utils.Recursos;

public class TextManager implements IGameloop {

    private static TextManager instance = null;
    // atributos ---------------------------------------------------------
    private final String imagemFonte = "fontBase01.png"; // nome do arquivo da imagem da fonte; 
    private final String arquivoFonte = "fontBase01.fnt"; // nome do arquivo com os dados de recorte da fonte
    public static Texture imagem; // imagem do sprite dos caracteres
    private Rect[] caracteres; // arrays com as dimensões de recorte de todos os caracteres de texto
    private SimpleTextBox[] textBoxes; // quadros dos textos a serem renderizados
    private Texture[] textImages; // imagens dos textos a serem renderizados
    private boolean[] ativadores; // para verificar qual texto será renderizado
    private boolean ativadorGlobal; // para verificar se pode exibir texto

    // contrutor ---------------------------------------------------------
    private TextManager(){
        
    }

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
            // desenha a textura ma tela;
                Recursos.batch.draw(textImages[i],
                        0,0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                        0,0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,false,true);
        }
    }

    // metodos de ativação -----------------------------------------------
    public void ativarExibicao(){
        ativadorGlobal=true;
    }
    public void desativarExibicao(){
        ativadorGlobal=false;
    }
    public void ativarTexto(int index){
        ativadores[index]=true;
    } 
    public void desativarTexto(int index){
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

			// realiza a filtragem dos textos que correspondem ao identificador 'id'
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
            textBoxes = new SimpleTextBox[textos.length]; // inicializa o array de diálogos
            ativadores = new boolean[textos.length]; // inicializa o array de ativadores dos diálogos
            
            // inicializa cada um dos diálogos com seus respectivos textos
            for(short i=0;i<textBoxes.length;i++){
                //cria cada um dos quadros de texto, atribuindo-lhes os seus respectivos textos
                textBoxes[i] = new SimpleTextBox(textos[i]);
            }
            carregarCaracteres();
            preparaTextBoxes();
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

    /* Desenha os caracteres do texto em uma única imagem pré pronta, para usar durante o gameplay (questão de desempenho!) */
    public void preparaTextBoxes(){
        textImages = new Texture[textBoxes.length];
        for(short i=0; i<textBoxes.length; i++){
            // captura i-ésimo diálogo
            char[] texto = textBoxes[i].texto;
            // obtém a posição inicial de deslocamento
            short deslocX = textBoxes[i].posX;
            // cria e inicia o framebuffer para desenhar os caracteres
            FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888,Recursos.getInstance().LARGURA_TELA, Recursos.getInstance().ALTURA_TELA, false);
            frameBuffer.begin(); // a partir daqui, o metodo draw desenhará sobre o backbuffer.
            // percorre todos os c-ésimos caracteres do diálogo
            for(short c=0;c<texto.length;c++){
                // obtém as coordenadas de recorte de origem (da imagem do caracter)
                short sX1 = caracteres[texto[c]].x1;
                short sY1 = caracteres[texto[c]].y1;
                short sW = texto[c]==' '?textBoxes[i].space:(short)(caracteres[texto[c]].x2-sX1); // correção para o símbolo de espaço
                short sH = (short)(caracteres[texto[c]].y2-sY1);
                // obtém as coordenadas de posicionamento no destino (na tela)
                short dX1 = deslocX;
                deslocX+=sW;
                short dY1 = (short)(textBoxes[i].posY+(16-sH));
                dY1 = corrigePosY(texto[c], dY1);
                dX1 = corrigePosX(texto[c], dX1);
                // desenha cada caractere no frameBuffer 
                Recursos.batch.begin();
                Recursos.batch.draw(imagem,dX1,dY1,sW,sH,sX1,sY1,sW,sH,false,true);
                Recursos.batch.end();
            }
            // extrai a textura mesclada do backbuffer
            textImages[i] = frameBuffer.getColorBufferTexture();
            frameBuffer.end(); // finaliza o desenho no framebuffer
        }

    }

    private short corrigePosX(char c, short dX){
        if(c=='±'||c=='×'||c=='»'||c=='<'||c=='-'||c=='+') return (short)(dX+1); // correção de posX
        if(c=='°'||c=='º'||c=='²'||c=='®'||c=='©') return (short)(dX-1); // correção de posX
        if(c==','||c=='¼'||c=='¾') return (short)(dX-2); // correção de posX
        return dX;
    }
    private short corrigePosY(char c, short dY){
        if(c=='\"'||c=='\''||c=='*') return (short)(dY-6); // correção de posY
        if(c=='°'||c=='¹'||c=='²'||c=='³') return (short)(dY-5); // correção de posY
        if(c=='ª'||c=='º'||c=='-') return (short)(dY-3); // correção de posY
        if(c=='~'||c=='='||c=='^'||c=='`'||c=='´') return (short)(dY-2); // correção de posY
        if(c=='.'||c=='['||c==']'||c=='('||c==')'||c=='{'||c=='}'||c=='|'||c=='$') return (short)(dY+1); // correção de posY
        if(c=='ç'||c=='Ç'||c=='/'||c=='\\'||c==';') return (short)(dY+2); // correção de posY
        if(c=='p'||c=='q'||c=='j'||c=='y'||c=='g') return (short)(dY+3); // correção de posY
        if(c==',') return (short)(dY+4); // correção de posY
        return dY;
    }
}
