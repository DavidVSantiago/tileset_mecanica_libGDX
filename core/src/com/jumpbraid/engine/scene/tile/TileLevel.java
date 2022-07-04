package com.jumpbraid.engine.scene.tile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.person.EstadoPerson;
import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.person.Person.Orientacao;
import com.jumpbraid.engine.scene.Scene;
import com.jumpbraid.engine.scene.SceneManager;
import com.jumpbraid.engine.utils.Camera;
import com.jumpbraid.engine.utils.KeyState;
import com.jumpbraid.engine.utils.Recursos;

/** Tem o objetivo de atualizar e renderizar os Layers */
public abstract class TileLevel extends Scene{
    // atributos
    public TileLayer[] listaTileLayers; // lista com todos os layers do cenário
    public Tileset[] listaTilesets; // lista com todos os tilesets do cenário
    public int qtdColunasLevel, qtdLinhasLevel; // largura e altura de todo o cenario (em tiles)
    public int larguraLevel,alturaLevel;
    private Texture fundoFase;
    private Camera camera;
    private KeyState keyState;
    private Person person;

    // construtor
    public TileLevel(String arquivoLevel,Texture fundoFase) {
        this.fundoFase = fundoFase;
        person = Recursos.getInstance().person;
        // carrega o arquivo json do cenario
        JsonValue fullJson = Recursos.carregarJson(arquivoLevel);
        qtdColunasLevel = fullJson.getInt("width");
        qtdLinhasLevel = fullJson.getInt("height");
        larguraLevel= qtdColunasLevel*fullJson.getInt("tilewidth");
        alturaLevel = qtdLinhasLevel*fullJson.getInt("tileheight");
        // faz o parser dos layers do cenário
        parseTileLayer(fullJson.get("layers"));
        // faz o parser dos tilesets do cenário
        parseTilesets(fullJson.get("tilesets"));
        // atribui os tilesets aos seus respectivos layers
        atribuiTilesetsLayers();

        // limpa a memória desnecessária
        fullJson = null;
        
        // normaliza os tileIDs e cria os tiles de destino (os desenhados na tela)
        for(int i=0;i<listaTileLayers.length;i++){
            listaTileLayers[i].normalizaIDs();
            listaTileLayers[i].criaTilesDestino();
        }
        
        camera = Recursos.getInstance().camera;
        camera.setPosition(0, alturaLevel-camera.altura);
        camera.levelAtual = this; // obrigatório, para a camera poder calcular o seu deslocamento dentro do mapa
        keyState = Recursos.getInstance().keyState;
        person.setCaixaMoveEsq(); // amplia a caixa de movimentação, para o posicionamento inicial no cenário
        person.setPosition(camera.largura*0.2f,camera.altura*0.42f);
    }

    /** Implemente este método e especifique quais os tilesets associados a quais layers do level */
    public abstract void atribuiTilesetsLayers();

    // métodos gameloop ***********************************************
    @Override
    public final void handlerEvents() {
        //camera.handlerEvents(); // move a tela com os direcionais
        person.handlerEvents();
    }

    @Override
    public final void update(long tempoDelta) {
        atualizaMovimentoCamera();
        person.update();
        colisaoPersonLevel(); // Testa a colisão do personagem com os tiles do cenário
        
        // controla o reinício do jogo, quando o jogador morre
        if(Recursos.ESTADO==EstadoJogo.MORTO){ 
            SceneManager.transicaoParaGameOver("cenario_01.tmj");
        }
    }

    @Override
    public final void render(SpriteBatch batch,long tempoDelta) {
        
        // renderiza os npcs
        batch.draw(fundoFase, 0, 0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                              0, 0,Recursos.getInstance().LARGURA_TELA,Recursos.getInstance().ALTURA_TELA,
                              false,true);
        listaTileLayers[0].render(batch,tempoDelta); // renderiza Layer01-sky
        listaTileLayers[1].render(batch,tempoDelta); // renderiza Layer02-sky
        listaTileLayers[2].render(batch,tempoDelta); // renderiza Layer03-back
        person.render(batch);
        listaTileLayers[3].render(batch,tempoDelta); // renderiza Layer04-front
        listaTileLayers[4].render(batch,tempoDelta); // renderiza Layer05-colliders
    }


    // métodos *********************************************************
    private void parseTileLayer(JsonValue jsonLayers) {
        listaTileLayers = new TileLayer[jsonLayers.size];
        JsonIterator it = jsonLayers.iterator();
        for(int i=0;i<jsonLayers.size;i++){
            JsonValue child = it.next();
            // lê os atributos do layer
            short qtdLinhasLayer = child.getShort("height");
            short qtdColunasLayer = child.getShort("width");
            float fatorParalaxeX = (child.has("parallaxx"))?child.getFloat("parallaxx"):1.0f;
            float fatorParalaxeY = (child.has("parallaxy"))?child.getFloat("parallaxy"):1.0f;
            // captura os IDs do layer
            short[] tileIDs = child.get("data").asShortArray();
            // cria um novo TileLayer e o adiciona à lista
            listaTileLayers[i] = new TileLayer(tileIDs, qtdLinhasLayer, qtdColunasLayer,fatorParalaxeX,fatorParalaxeY);
            
        }
    }

    private void parseTilesets(JsonValue jsonTilesets) {
        listaTilesets = new Tileset[jsonTilesets.size];
        JsonIterator it = jsonTilesets.iterator();
        for(short i=0;i<jsonTilesets.size;i++){
            JsonValue child = it.next();
            short firstGridId = child.getShort("firstgid");
            // captura os dados de cada tileset
            Texture img = Recursos.getInstance().tilesetImages.get(child.getString("name"));
            short larguraTile = child.getShort("tilewidth");
            short alturaTile = child.getShort("tileheight");
            short espacoTiles = child.getShort("spacing");
            short margemTiles = child.getShort("margin");
            short larguraTileset = child.getShort("imagewidth");
            short alturaTileset = child.getShort("imageheight");
            short qtdTiles = child.getShort("tilecount");
            short qtdColunasTileset = child.getShort("columns");
            // cria um novo tileset com os dados capturados do arquivo
            Tileset tileset = new Tileset(img, firstGridId, larguraTile, alturaTile, espacoTiles, margemTiles, larguraTileset,
                    alturaTileset, qtdTiles, qtdColunasTileset);
            listaTilesets[i] = tileset;
        }
    }
    
    // métodos de colisão *******************************************

    public void atualizaMovimentoCamera(){
        // se o personagem estiver bloqueado, interrompe aqui
        if(person.bloqueiaTodoMovimento) return;

        // atualiza a movimento da camera
        if(person.colideCaixaMoveDireita() ||
           person.colideCaixaMoveEsquerda()){ // se o person colide com os limites esquerdo e direito
            camera.moverHorizontal(); // move a camera horizontalmente
        }else{ // se o person não colide com os limites esquerdo e direito
            person.moverHorizontal(); // move o personagem horizontalmente
        }
        
        if(person.colideCaixaMoveCima() ||
           person.colideCaixaMoveBaixo()){ // se o person colide com os limites superior e inferior
            camera.moverVertical(); // move a camera verticalmente
        }else{ // se o person não colide com os limites superior e inferior
            person.moverVertical(); // move o personagem verticalmente
        }

        // colisão da camera com os limites do cenário
        person.resetCaixaMove(); // redefine os limites da caixa de movimento  
        if(camera.posX<=0){ // CANTO ESQUERDO -----------------
            camera.posX=0;
            person.setCaixaMoveEsq(); // amplia o limite da caixa de movimento para a esquerda
        }else if(camera.posX+camera.largura>=larguraLevel){ // CANTO DIREITO -----------------
            camera.posX=larguraLevel-camera.largura;
            person.setCaixaMoveDir(); // amplia o limite da caixa de movimento para a direita
        }
        if(camera.posY<=0){ // CANTO SUPERIOR -----------------
            camera.posY=0;
            person.setCaixaMoveSup(); // amplia o limite da caixa de movimento para a esquerda
        }else if(camera.posY+camera.altura>=alturaLevel){ // CANTO INFERIOR -----------------
            camera.posY=alturaLevel-camera.altura;
            person.setCaixaMoveInf(); // amplia o limite da caixa de movimento para a direita
        }
    }

    /** Verifica se o tile está fora da área de cobertura do personagem */
    public boolean tileForaAreaColisao(Tile tile){
        if(tile.x1-camera.posX>person.caixaMove.x2 ||
            tile.x2-camera.posX<person.caixaMove.x1 ||  
            tile.y1-camera.posY>person.caixaMove.y2 ||
            tile.y2-camera.posY<person.caixaMove.y1){
                return true;
        }
        return false;
    }

    /** Testa a colisão do personagem com os tiles do cenário */
    public void colisaoPersonLevel(){
        TileLayer layer05 = listaTileLayers[4]; // Layer05-collision (colidível)
        
        // percorre todos os tiles de destino do layer05
        for (int linha = 0; linha < layer05.qtdLinhasLayer; linha++) { // percorre todas as linhas do layer
            for (int coluna = 0; coluna < layer05.qtdColunasLayer; coluna++) { // percorre todas as colunas do layer
                // obtém o tile de destino
                Tile tileDestino =layer05.obterTileDestino(linha,coluna);

                // (OTIMIZAÇÃO) id 0 se refere a nenhum tile
                if(tileDestino.ID==0)continue;
                // (OTIMIZAÇÃO) se o tile de destino estiver fora da caixa de movimentação do personagem
                if(tileForaAreaColisao(tileDestino)) continue;                    
            
                // testa a colisão do personagem com cada tile dentro da caixa de movimento
                if(tileDestino.ID-1==2) // por cima
                    checaColisaoRed(tileDestino);
                else if(tileDestino.ID-1==1) // pela quina cima esquerda
                    checaColisaoOrange(tileDestino);
                else if(tileDestino.ID-1==3) // pela quina cima direita
                    checaColisaoPink(tileDestino); 
                else if(tileDestino.ID-1==0) // pela esquerda 
                    checaColisaoYellow(tileDestino);
                else if(tileDestino.ID-1==4) // pela direita
                    checaColisaoPurple(tileDestino);
                else if(tileDestino.ID-1==6) // por baixo
                    checaColisaoAqua(tileDestino);
                else if(tileDestino.ID-1==7) // pela quina baixo esquerda
                    checaColisaoGreen(tileDestino);
                else if(tileDestino.ID-1==5) // pela quina baixo direita
                    checaColisaoBlue(tileDestino); 
            }
        }
    }

    // ----------------------------------------------------------------

    public void checaColisaoYellow(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY; 
        // se o personagem está verticalmente fora do tile
        if(pY2<tileY1 || pY1>tileY2)
            return;
        // se o personagem está horizontalmente à direita (após o tile)
        if(pX2>tileX2)
            return;
        // verifica se colide da esquerda para a direita
        if(pX2>tileX1 && pX2-cVelX <= pX2){
            // recoloca o personagem na posição anterior
            float largura = person.caixaColisao.x2-person.caixaColisao.x1;
            person.caixaColisao.x2=tileX1;
            person.caixaColisao.x1=person.caixaColisao.x2-largura;
            person.ORIENTACAO = Orientacao.DIREITA;
            person.entraEstadoPAREDE();
        }
    }
    public void checaColisaoOrange(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY; 
        // se o personagem está horizontalmente e vericalmente fora quina do tile
        if(pX1>tileX2 || pY1>tileY2) return;
        // se o personagem está no ponto cego da quina
        if(pX2<tileX1 && pY2<tileY1) return;
        // se o personagem não colide com o tile
        if(!(pX2>tileX1 && pY2>tileY1)) return;
        // calcula o fator de aproximação horizontal (mais sobre a quina ou mais ao lado da quina)
        pY2-=camera.velY; // correção de queda na quina
        float fatorH = Math.abs(tileX1-pX2);
        float fatorV = Math.abs(pY2-tileY1);
        if(fatorH>fatorV){ // aproximação por cima
            // recoloca o personagem acima do tile
            float altura = person.caixaColisao.y2-person.caixaColisao.y1;
            person.caixaColisao.y2=tileY1;
            person.caixaColisao.y1=person.caixaColisao.y2-altura;
            
            if(keyState.k_esquerda||keyState.k_direita)
                person.entraEstadoCORRENDO();
            else
                person.entraEstadoPARADO();
        }else{ // aproximação pelo lado esquerdo
            /// recoloca o personagem na posição anterior
            float largura = person.caixaColisao.x2-person.caixaColisao.x1;
            person.caixaColisao.x2=tileX1+1;
            person.caixaColisao.x1=person.caixaColisao.x2-largura;
            person.ORIENTACAO = Orientacao.DIREITA;
        }
    }
    public void checaColisaoRed(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY; 
        // se o personagem está horizontalmente fora do tile
        if(pX2<tileX1 || pX1>tileX2) return;
        // se o personagem está vericalmente abaixo do tile
        if(pY2>tileY2) return;
        // verifica se colide de cima para baixo
        if(pY2>tileY1 && pY2-cVelY < pY2){
            // recoloca o personagem acima do tile
            float altura = person.caixaColisao.y2-person.caixaColisao.y1;
            person.caixaColisao.y2=tileY1;
            person.caixaColisao.y1=person.caixaColisao.y2-altura;
            
            if(keyState.k_esquerda||keyState.k_direita)
                person.entraEstadoCORRENDO();
            else
                person.entraEstadoPARADO();
        }
    }
    public void checaColisaoPink(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY;
        // se o personagem está horizontalmente e vericalmente fora quina do tile
        if(pX2<tileX1 || pY1>tileY2) return;
        // se o personagem está no ponto cego da quina
        if(pX1>tileX2 && pY2<tileY1) return;
        // se o personagem não colide com o tile
        if(!(pX1<tileX2 && pY2>tileY1)) return;
        // calcula o fator de aproximação horizontal (mais sobre a quina ou mais ao lado da quina)
        pY2-=camera.velY; // correção de queda na quina
        float fatorH = Math.abs(pX1-tileX2);
        float fatorV = Math.abs(pY2-tileY1);
        if(fatorH>fatorV){ // aproximação por cima
            // recoloca o personagem acima do tile
            float altura = person.caixaColisao.y2-person.caixaColisao.y1;
            person.caixaColisao.y2=tileY1;
            person.caixaColisao.y1=person.caixaColisao.y2-altura;
            
            if(keyState.k_esquerda||keyState.k_direita)
                person.entraEstadoCORRENDO();
            else
                person.entraEstadoPARADO();
        }else{ // aproximação pelo lado direito
            // recoloca o personagem na posição anterior
            float largura = person.caixaColisao.x2-person.caixaColisao.x1;
            person.caixaColisao.x1=tileX2-1;
            person.caixaColisao.x2=person.caixaColisao.x1+largura;
        }
    }
    public void checaColisaoPurple(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY; 
        // se o personagem está verticalmente fora do tile
        if(pY2<tileY1 || pY1>tileY2) return;
        // se o personagem está horizontalmente à esquerda (após o tile)
        if(pX1<tileX1) return;
        // verifica se colide da direita para a esquerda
        if(pX1<tileX2 && pX1-cVelX >= pX1){
            // recoloca o personagem na posição anterior
            float largura = person.caixaColisao.x2-person.caixaColisao.x1;
            person.caixaColisao.x1=tileX2;
            person.caixaColisao.x2=person.caixaColisao.x1+largura;
            person.ORIENTACAO = Orientacao.ESQUERDA;
            person.entraEstadoPAREDE();
        }
    }
    public void checaColisaoBlue(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY;
        // se o personagem está horizontalmente e vericalmente fora quina do tile
        if(pX2<tileX1 || pY2<tileY1) return;
        // se o personagem está no ponto cego da quina
        if(pX1>tileX2 && pY1>tileY2) return;
        // se o personagem não colide com o tile
        if(!(pX1<tileX2 && pY1<tileY2)) return;
        // calcula o fator de aproximação horizontal (mais abaixo da quina ou mais ao lado da quina)
        pY1-=camera.velY; // correção de aproximação da ponta
        float fatorH = Math.abs(pX1-tileX2);
        float fatorV = Math.abs(pY1-tileY2);
        if(fatorH>fatorV){ // aproximação por baixo
            // recoloca o personagem abaixo do tile
            float altura = person.caixaColisao.y2-person.caixaColisao.y1;
            person.caixaColisao.y1-=camera.velY;
            person.caixaColisao.y2=person.caixaColisao.y1+altura;
            camera.velY=camera.velBaseY*0.1f;
        }else{ // aproximação pelo lado direito
            /// recoloca o personagem na posição anterior
            float largura = person.caixaColisao.x2-person.caixaColisao.x1;
            person.caixaColisao.x1=tileX2;
            person.caixaColisao.x2=person.caixaColisao.x1+largura;
            person.ORIENTACAO = Orientacao.ESQUERDA;
            person.entraEstadoPAREDE();
        }
    }
    public void checaColisaoAqua(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY; 
        // se o personagem está horizontalmente fora do tile
        if(pX2<tileX1 || pX1>tileX2) return;
        // se o personagem está vericalmente acima do tile
        if(pY1<tileY1) return;
        // verifica se colide de baixo para cima
        if(pY1<tileY2 && pY1-cVelY > pY1){
            // recoloca o personagem abaixo do tile
            float altura = person.caixaColisao.y2-person.caixaColisao.y1;
            person.caixaColisao.y1-=camera.velY;
            person.caixaColisao.y2=person.caixaColisao.y1+altura;
            camera.velY=camera.velBaseY*0.1f;
        }
    }
    public void checaColisaoGreen(Tile tile){
        float tileX1 = tile.x1-camera.posX;
        float tileX2 = tile.x2-camera.posX;
        float tileY1 = tile.y1-camera.posY;
        float tileY2 = tile.y2-camera.posY;
        float pX1 = person.caixaColisao.x1;
        float pY1 = person.caixaColisao.y1;
        float pX2 = person.caixaColisao.x2;
        float pY2 = person.caixaColisao.y2;
        float cVelX = camera.velX;
        float cVelY = camera.velY;
        // se o personagem está horizontalmente e vericalmente fora quina do tile
        if(pX1>tileX2 || pY2<tileY1) return;
        // se o personagem está no ponto cego da quina
        if(pX2<tileX1 && pY1>tileY2) return;
        // se o personagem não colide com o tile
        if(!(pX2>tileX1 && pY1<tileY2)) return;
        // calcula o fator de aproximação horizontal (mais abaixo da quina ou mais ao lado da quina)
        pY1-=camera.velY; // correção de aproximação da ponta
        float fatorH = Math.abs(pX2-tileX1);
        float fatorV = Math.abs(pY1-tileY2);
        if(fatorH>fatorV){ // aproximação por baixo
            // recoloca o personagem abaixo do tile
            float altura = person.caixaColisao.y2-person.caixaColisao.y1;
            person.caixaColisao.y1-=camera.velY;
            person.caixaColisao.y2=person.caixaColisao.y1+altura;
            camera.velY=camera.velBaseY*0.1f;
        }else{ // aproximação pelo lado esquerdo
            // recoloca o personagem na posição anterior
            float largura = person.caixaColisao.x2-person.caixaColisao.x1;
            person.caixaColisao.x2=tileX1;
            person.caixaColisao.x1=person.caixaColisao.x2-largura;
            person.ORIENTACAO = Orientacao.DIREITA;
            person.entraEstadoPAREDE();
        }
    }  

    public void reiniciaLevel(){
        Recursos.ESTADO=EstadoJogo.EXECUTANDO;
        person.bloqueiaTodoMovimento=false;
        person.bloqueiaMovimentoH=false;
        camera.setPosition(0, alturaLevel-camera.altura);
        person.setCaixaMoveEsq(); // amplia a caixa de movimentação, para o posicionamento inicial no cenário
        person.setPosition(camera.largura*0.2f,camera.altura*0.42f);
    }
}