package com.jumpbraid.game;

import com.jumpbraid.engine.person.Person;
import com.jumpbraid.engine.scene.tile.TileLevel;

public class Fase extends TileLevel{
    // atributos -------------------------------------------------------

    // construtor -------------------------------------------------------
    public Fase(String mapaFase,String fundoFase,String imagemDir,Person person){
        super(mapaFase,fundoFase,imagemDir,person);
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
