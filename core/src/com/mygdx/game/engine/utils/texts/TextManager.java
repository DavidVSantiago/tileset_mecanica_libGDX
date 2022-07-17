package com.mygdx.game.engine.utils.texts;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.mygdx.game.engine.game.IGameloop;
import com.mygdx.game.engine.utils.Rect;
import com.mygdx.game.engine.utils.Recursos;

public class TextManager implements IGameloop {

    private static TextManager instance = null;
    // atributos ---------------------------------------------------------
    public static Texture imagem; // imagem do sprite dos caracteres
    private Char[] caracteresOrigem; // arrays com as dimensões de recorte de todos os caracteres de origem de texto
    public SimpleTextBox[] textBoxes; // quadros dos textos a serem renderizados
    private boolean ativadorGlobal; // para verificar se pode exibir textos

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
        // percorre as i-ésimas caixas de textos
        for(short i=0;i<textBoxes.length;i++){
            if(!textBoxes[i].ativarRender) continue; // se não for hora de exibir o texto, abandona
            // percorre os c-ésimos caracteres de cada uma das i-ésimas caixas de texto
            for(short c=0;c<textBoxes[i].caracteres.length;c++){
                Char caractere = textBoxes[i].caracteres[c];
                // desenha o caractere na tela
                Recursos.batch.draw(imagem,
                    caractere.posX,caractere.posY,caractere.largura,caractere.altura,
                    caracteresOrigem[caractere.caractere].posX,caracteresOrigem[caractere.caractere].posY,
                    caracteresOrigem[caractere.caractere].largura,caracteresOrigem[caractere.caractere].altura,false,true);
                
            }
        }
    }

    // metodos de ativação -----------------------------------------------
    public void setAtivacaoExibicao(boolean value){
        ativadorGlobal=value;
    }
    public boolean isAtivacaoExibicao(){
        return ativadorGlobal;
    }
    public void setAtivacaoTexto(int index, boolean value){
        textBoxes[index].ativarRender=value;
    } 
    public boolean isAtivacaoTexto(int index){
        return textBoxes[index].ativarRender;
    }
    public void setAtivacaoTodos(boolean value){
        for(short i=0;i<textBoxes.length;i++)
            textBoxes[i].ativarRender=value;
    }

    // metodos ---------------------------------------------------------
    public void carregaDialogos(String textoDialogos,String dialogoID, String arquivoFonte, int espacoCaracteres){
        // carrega o arquivo da fonte dos caracteres usado nesta cena
        carregarCaracteres(arquivoFonte); 
        // carrega o arquivo com os diálogos usados nesta cena
        carregarTextosCena(textoDialogos, dialogoID,(short)espacoCaracteres);
    }
        /* recebe um array de textos, para cada um dos diálogos da cena*/
    private void carregarTextosCena(String arquivo,String id, short espacoCaracteres){
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
					while(j<completo.length&&!completo[j].equals("")&&completo[j].charAt(0)=='*'){
						selecionados.add(completo[j++].substring(1));
					}
					break;
				}
			}

			// converte os diálogos filtrados para um array simples
			String[] textos = new String[selecionados.size()];
			selecionados.toArray(textos);
            // inicializa as caixas de texto e passa os diálogos carregados para a cena
            textBoxes = new SimpleTextBox[textos.length]; 
            
            // inicializa cada um dos diálogos com seus respectivos textos
            for(short i=0;i<textBoxes.length;i++){
                char[] texto = textos[i].toCharArray(); // captura cada texto
                textBoxes[i] = new SimpleTextBox(texto); // inicializa cada textbox com cada diálogo capturado
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /* Esta função carrega o arquivo da imagem e das informações dos caracteres */
    private void carregarCaracteres(String arquivoFonte){
        caracteresOrigem = new Char[256]; // inicializa o array para os 256 caracteres da tabela ASCII
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
            short largua = Short.valueOf(palavras[4].substring(6,palavras[4].length()));
            short altura = Short.valueOf(palavras[5].substring(7,palavras[5].length()));
            // coloca as coordenadas de recorte no seu respectivo id
            caracteresOrigem[id] = new Char((char)id);
            caracteresOrigem[id].posX = x;
            caracteresOrigem[id].posY = y;
            caracteresOrigem[id].largura = largua;
            caracteresOrigem[id].altura = altura;
        }
    }
    /* prepara os diálogos da cena para serem renderizados */
    public void preparaTextoCena(){
        // percorre cada uma das caixas de texto
        for(short i=0; i<textBoxes.length; i++){
            // captura o diálogo da i-ésima caixa de texto
            char[] texto = new char[textBoxes[i].caracteres.length];
            for(short c=0;c<texto.length;c++){
                texto[c] = textBoxes[i].caracteres[c].caractere;
            }
            short space = textBoxes[i].space; // captura o espaço entre os caracteres
            short posX = textBoxes[i].posX; // pos X da caixa de texto
            short posY = textBoxes[i].posY; // pos Y da caixa de texto
            short deslocX = posX; // valor inaicial do deslocamento de cada caractere 
            // formata a posição do c-ésimo caractere do diálogo da i-ésima caixa de texto
            for(short c=0;c<texto.length;c++){
                // obtém a largura e altura de cada caracter na origem de origem
                short sW = texto[c]==' '?space:caracteresOrigem[texto[c]].largura; // correção para o símbolo de espaço
                short sH = caracteresOrigem[texto[c]].altura;
                // define as coordenadas de destino (a pos na tela onde cada c-ésimo caracter será desenhado)
                short dX = deslocX;
                short dY = (short)(posY+(16-sH));
                dY = corrigePosY(texto[c], dY); // TODO. tentar evita isso FUTURAMENTE!
                dX = corrigePosX(texto[c], dX); // TODO. tentar evita isso FUTURAMENTE!
                // modifica as posições de cada caracter do diálogo da caixa de texto
                textBoxes[i].caracteres[c].posX = dX;
                textBoxes[i].caracteres[c].posY = dY;
                textBoxes[i].caracteres[c].largura = sW;
                textBoxes[i].caracteres[c].altura = sH;
                deslocX+=sW; // incrementa o deslocamento para o próximo caracter
            }
        }
    }
    /* Desenha os caracteres do texto em uma única imagem pré pronta, para usar durante o gameplay (questão de desempenho!) */
    /*public void preparaTextBoxes(){
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
                short sX1 = caracteresOrigem[texto[c]].x1;
                short sY1 = caracteresOrigem[texto[c]].y1;
                short sW = texto[c]==' '?textBoxes[i].space:(short)(caracteresOrigem[texto[c]].x2-sX1); // correção para o símbolo de espaço
                short sH = (short)(caracteresOrigem[texto[c]].y2-sY1);
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

    }*/

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
