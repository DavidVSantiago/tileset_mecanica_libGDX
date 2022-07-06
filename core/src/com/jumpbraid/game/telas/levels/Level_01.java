package com.jumpbraid.game.telas.levels;

import com.jumpbraid.engine.scene.tile.TileLevel;
import com.jumpbraid.engine.utils.Levels;
import com.jumpbraid.engine.utils.Recursos;

public class Level_01 extends TileLevel{
    // atributos -------------------------------------------------------

    // construtor -------------------------------------------------------
    public Level_01(){
        super(Levels.LEVEL_01,Level_01.class.getName());

        // define a posição do personagem nesse level
        person.setCaixaMoveEsq(); // amplia a caixa de movimentação, para o posicionamento inicial no cenário
        person.setPosition(camera.largura*0.2f,camera.altura*0.42f);
    }

    // métodos gameloop indiretos ---------------------------------------

    // métodos ----------------------------------------------------------
    @Override
    public void atribuiTilesetsLayers() {
        // atribui os tilesets aos seus respectivos layers
        listaTileLayers[0].tileset = listaTilesets[2]; // Layer01-horizon
        listaTileLayers[1].tileset = listaTilesets[0]; // Layer02-back-paralax
        listaTileLayers[2].tileset = listaTilesets[1]; // Layer03-back
        listaTileLayers[3].tileset = listaTilesets[1]; // Layer04-front
        listaTileLayers[4].tileset = listaTilesets[1]; // Layer05-collision
    }
}