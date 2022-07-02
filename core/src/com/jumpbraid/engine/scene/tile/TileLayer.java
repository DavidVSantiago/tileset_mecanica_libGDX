package com.jumpbraid.engine.scene.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jumpbraid.engine.images.Element;
import com.jumpbraid.engine.utils.Camera;
import com.jumpbraid.engine.utils.Recursos;

/** Representa o Layer de Tiles */
public class TileLayer extends Element{
    // atributos -------------------------------------------------------
    public int qtdColunasLayer, qtdLinhasLayer; // dimensões do layer
    public Tileset tileset; // tileset associado a este layer
    
    public float fatorParalaxeX,fatorParalaxeY;
    public int[] tileIDs; // ids de mapeamento do cenário
    public Tile[][] tilesDestino; // retangulos de recorte dos tiles destino na tela
    private Camera camera;

    // construtor
    public TileLayer(int[] tileIDs, int qtdLinhasLayer, int qtdColunasLayer,
                    float fatorParalaxeX,float fatorParalaxeY) {
        super();
        this.qtdColunasLayer = qtdColunasLayer;
        this.qtdLinhasLayer = qtdLinhasLayer;
        this.fatorParalaxeX = fatorParalaxeX;
        this.fatorParalaxeY = fatorParalaxeY;
        this.tileIDs = tileIDs;
        camera = Recursos.getInstance().camera;
    }

    @Override
    public void handlerEvents() {
        // SEM USO!!
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        // percorre e desenha todos os tiles de destino
        for (int linha = 0; linha < qtdLinhasLayer; linha++) { // percorre todas as linhas do layer
            for (int coluna = 0; coluna < qtdColunasLayer; coluna++) { // percorre todas as colunas do layer
                
                // (OTIMIZAÇÃO) se o tile de destino estiver fora da camera, não o desenha
                if(coluna*tileset.larguraTile>(camera.posX*fatorParalaxeX)+camera.largura||
                linha*tileset.alturaTile>(camera.posY*fatorParalaxeY)+camera.altura) continue;
                
                // obtém o tile de destino
                Tile tileDestino = obterTileDestino(linha,coluna);
                
                // (OTIMIZAÇÃO) id 0 se refere a um tile vazio
                if(tileDestino.ID==0)continue;
                
                // obtém a origem de cada tile de destino
                Tile tileOrigem = tileset.obterTileOrigem(tileDestino.ID-1); // correção de indice
               
                float dx = (tileDestino.x1+posX)-(camera.posX*fatorParalaxeX);
                float dy = (tileDestino.y1+posY)-(camera.posY*fatorParalaxeY);
                float width = tileDestino.x2-tileDestino.x1;
                float height = tileDestino.y2-tileDestino.y1;
                // desenha cada tile em sua respectiva posição
                batch.draw(tileset.img,dx,dy,width,height,
                            tileOrigem.x1, tileOrigem.y1,
                            tileOrigem.x2-tileOrigem.x1, tileOrigem.y2-tileOrigem.y1, false,true);
            }
        }
    }

    // Métodos ----------------------------------------------------
    public void normalizaIDs(){
        for (int i = 0; i < tileIDs.length; i++) {
            if(tileIDs[i]==0) continue;
                tileIDs[i] = (tileIDs[i]-tileset.firstGridId)+1;
        }
    }
    public void criaTilesDestino(){
        // cria a lista dos tiles de destino (os tiles desenhados na tela)
        tilesDestino = new Tile[qtdLinhasLayer][qtdColunasLayer];
        for (int linha = 0; linha < qtdLinhasLayer; linha++) { // percorre todas as linhas do layer
            for (int coluna = 0; coluna < qtdColunasLayer; coluna++) { // percorre todas as colunas do layer
                int dx1 = coluna * tileset.larguraTile;
                int dy1 = linha * tileset.alturaTile;
                int dx2 = dx1 + tileset.larguraTile;
                int dy2 = dy1 + tileset.alturaTile;
                tilesDestino[linha][coluna] = new Tile(dx1, dy1, dx2, dy2, tileIDs[(linha*qtdColunasLayer)+coluna]);
            }
        }
    }
    public Tile obterTileDestino(int linha, int coluna){
        return tilesDestino[linha][coluna];
    }
}