package com.jumpbraid.game.levels;

import com.jumpbraid.engine.scene.tile.TileLevel;
import com.jumpbraid.engine.utils.Levels;
import com.jumpbraid.engine.utils.Recursos;

public class Level_02 extends TileLevel{
    // atributos -------------------------------------------------------

    // construtor -------------------------------------------------------
    public Level_02(){
        super(Levels.LEVEL_01,Level_02.class.getName(),Recursos.getInstance().fundo_cenario_01);

        // define a posição do personagem nesse level
        person.setCaixaMoveEsq(); // amplia a caixa de movimentação, para o posicionamento inicial no cenário
        person.setPosition(camera.largura*0.2f,camera.altura*0.42f);
    }

    // métodos gameloop indiretos ---------------------------------------

    // métodos ----------------------------------------------------------
    @Override
    public void atribuiTilesetsLayers() {
        // atribui os tilesets aos seus respectivos layers
        listaTileLayers[0].tileset = listaTilesets[0]; // Layer01-paralaxBG
        listaTileLayers[1].tileset = listaTilesets[1]; // Layer02-bg
        listaTileLayers[2].tileset = listaTilesets[1]; // Layer03-mid
        listaTileLayers[3].tileset = listaTilesets[1]; // Layer04-front
        listaTileLayers[4].tileset = listaTilesets[1]; // Layer05-collision
    }
}
