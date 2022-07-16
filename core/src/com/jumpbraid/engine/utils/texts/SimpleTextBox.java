package com.jumpbraid.engine.utils.texts;

public class SimpleTextBox {
    // atributos ----------------------------------------------
    public char[] texto;
    public short posX, posY; // posição
    public short space; // espaço entre os caracteres do texto

    // contrutor ----------------------------------------------
    public SimpleTextBox(String texto){
        this.texto = texto.toCharArray();
        posX=posY=0;
        space=5;
    }
}
