package com.mygdx.game.engine.utils;

public class Scenes {

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
    /* Registre aqui os nomes dos arquivos de imagem das telas do jogo */
    public enum Telas {
        SPLASH("splash.png"),
        MENU("menu.png"),
        OPTIONS("options.png"),
        PROLOGUE("prologue.png");

        private final String tela;

        private Telas(String tela) {
            this.tela = tela;
        }

        public boolean equalsTela(String level) {
            return this.tela.equals(level);
        }

        public String toString() {
            return this.tela;
        }
    }
    public enum Tempo {
        RAPIDO(0.2e+9),
        MEDIO(1e+9),
        LENTO(3e+9);

        private final double tempo;

        private Tempo(double tempo) {
            this.tempo = tempo;
        }

        public boolean equalsTempo(double tempo) {
            return this.tempo == tempo;
        }

        public double getValue() {
            return this.tempo;
        }
    }
}
