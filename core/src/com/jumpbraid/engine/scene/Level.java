package com.jumpbraid.engine.scene;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.JsonIterator;
import com.jumpbraid.engine.game.IGameloop;
import com.jumpbraid.engine.utils.Camera;
import com.jumpbraid.engine.utils.Recursos;

/** Tem o objetivo de atualizar e renderizar os Layers */
public abstract class Level implements IGameloop{
    // atributos
    public String imagensDir;
    public TileLayer[] listaTileLayers; // lista com todos os layers do cenário
    public Tileset[] listaTilesets; // lista com todos os tilesets do cenário
    public int qtdColunasLevel, qtdLinhasLevel; // largura e altura de todo o cenario (em tiles)
    public int larguraLevel,alturaLevel;
    Camera camera;

    // construtor
    public Level(String arquivoLevel,String imagensDir) {
        
        this.imagensDir = imagensDir;
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

        // normaliza os tileIDs e cria os tiles de destino (os desenhados na tela)
        for(int i=0;i<listaTileLayers.length;i++){
            listaTileLayers[i].normalizaIDs();
            listaTileLayers[i].criaTilesDestino();
        }

        camera = Recursos.getInstance().camera;
        camera.posY = alturaLevel-camera.altura; // camera, por padrão, começa no canto inferior esquerdo do cenário       
        camera.levelAtual = this; // obrigatório, para a camera poder calcular o seu deslocamento dentro do mapa
    }

    /** Implemente este método e especifique quais os tilesets associados a quais layers do level */
    public abstract void atribuiTilesetsLayers();

    // métodos gameloop ***********************************************
    @Override
    public final void handlerEvents() {
        camera.handlerEvents();
    }

    @Override
    public final void update() {
        camera.update();
    }

    @Override
    public final void render(SpriteBatch batch) {
        // renderiza os npcs
        listaTileLayers[0].render(batch); // renderiza Layer01-sky
        listaTileLayers[1].render(batch); // renderiza Layer02-sky
        listaTileLayers[2].render(batch); // renderiza Layer03-back
        //listaTileLayers[3].render(g); // renderiza Layer04-front
        //listaTileLayers[4].render(g); // renderiza Layer05-colliders
    }


    // métodos *********************************************************
    private void parseTileLayer(JsonValue jsonLayers) {
        listaTileLayers = new TileLayer[jsonLayers.size];
        JsonIterator it = jsonLayers.iterator();
        for(int i=0;i<jsonLayers.size;i++){
            JsonValue child = it.next();
            // lê os atributos do layer
            int qtdLinhasLayer = child.getInt("height");
            int qtdColunasLayer = child.getInt("width");
            float fatorParalaxeX = (child.has("parallaxx"))?child.getFloat("parallaxx"):1.0f;
            float fatorParalaxeY = (child.has("parallaxy"))?child.getFloat("parallaxy"):1.0f;
            // captura os IDs do layer
            int[] tileIDs = child.get("data").asIntArray();
            // cria um novo TileLayer e o adiciona à lista
            listaTileLayers[i] = new TileLayer(tileIDs, qtdLinhasLayer, qtdColunasLayer,fatorParalaxeX,fatorParalaxeY);
            
        }
    }

    private void parseTilesets(JsonValue jsonTilesets) {
        listaTilesets = new Tileset[jsonTilesets.size];
        JsonIterator it = jsonTilesets.iterator();
        for(int i=0;i<jsonTilesets.size;i++){
            JsonValue child = it.next();
            int firstGridId = child.getInt("firstgid");
            // captura os dados de cada tileset
            Texture img = Recursos.carregarImagem(imagensDir + child.getString("image"));
            int larguraTile = child.getInt("tilewidth");
            int alturaTile = child.getInt("tileheight");
            int espacoTiles = child.getInt("spacing");
            int margemTiles = child.getInt("margin");
            int larguraTileset = child.getInt("imagewidth");
            int alturaTileset = child.getInt("imageheight");
            int qtdTiles = child.getInt("tilecount");
            int qtdColunasTileset = child.getInt("columns");
            // cria um novo tileset com os dados capturados do arquivo
            Tileset tileset = new Tileset(img, firstGridId, larguraTile, alturaTile, espacoTiles, margemTiles, larguraTileset,
                    alturaTileset, qtdTiles, qtdColunasTileset);
            listaTilesets[i] = tileset;
        }
    }
    
    // métodos de colisão *******************************************

    /** Verifica se o tile está fora da área de cobertura do personagem */

    /** Testa a colisão do personagem com os tiles do cenário */

}