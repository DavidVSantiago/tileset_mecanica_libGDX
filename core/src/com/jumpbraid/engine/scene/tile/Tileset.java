package com.jumpbraid.engine.scene.tile;

import com.badlogic.gdx.graphics.Texture;

/** Representa o TileSet ***************************************************/
public class Tileset {
    // atributos
    public Texture img; // imagem qo tileset
    short firstGridId;
    short larguraTile, alturaTile; // dimensões de cada tile do tileset (em pixel)
    short espacoTiles, margemTiles;
    short larguraTileset, alturaTileset; // dimensões da imagem do tileset (em pixel)
    short qtdTiles; // quantidade total de tiles no tileset
    short qtdColunasTileset; // quantidade de colunas do tileset
    public Tile[] tilesOrigem; // retangulos de recorte do tileset

    // construtor
    public Tileset(Texture img, short firstGridId,
            short larguraTile, short alturaTile,
            short espacoTiles, short margemTiles, short larguraTileset, short alturaTileset,
            short qtdTiles, short qtdColunasTileset) {
        this.firstGridId = firstGridId;
        this.img = img;
        this.larguraTile = larguraTile;
        this.alturaTile = alturaTile;
        this.espacoTiles = espacoTiles;
        this.margemTiles = margemTiles;
        this.larguraTileset = larguraTileset;
        this.alturaTileset = alturaTileset;
        this.qtdTiles = qtdTiles;
        this.qtdColunasTileset = qtdColunasTileset;
        // inicializa todos os tiles de recorte do tileset
        tilesOrigem = new Tile[qtdTiles];
        for (short i = 0; i < qtdTiles; i++) {
            // define as coordenadas x e y de recorte na imagem do tileset
            short x1 = (short)((i % qtdColunasTileset) * larguraTile);
            short y1 = (short)((i / qtdColunasTileset) * alturaTile);
            short x2 = (short)(x1 + larguraTile);
            short y2 = (short)(y1 + alturaTile);
            tilesOrigem[i] = new Tile(x1, y1, x2, y2, i);
        }
    }

    public Tile obterTileOrigem(int tileId) {
        return tilesOrigem[tileId];
    }

}