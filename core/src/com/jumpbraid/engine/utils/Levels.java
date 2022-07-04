package com.jumpbraid.engine.utils;

/* Registre aqui os nomes dos arquivos de cada um dos levels do jogo */
public enum Levels {
    LEVEL_01("cenario_01.tmj"),
	LEVEL_02("cenario_02.tmj");

    private final String level;

    private Levels(String level) {
        this.level = level;
    }
    public boolean equalsLevel(String level) {
        return this.level.equals(level);
    }
    public String toString() {
       return this.level;
    }
    
}
