package com.mygdx.game.engine.utils.texts;

import com.mygdx.game.engine.utils.Rect;

/* Esta classe representa um texto a ser exibido na tela */
public class SimpleTextBox {
    // atributos ----------------------------------------------
    public Char[] caracteres; // armazena cada caractere da caixa de texto com suas posições de desenho
    public short posX, posY; // posição de toda a caixa de texto
    public short space; // espaço entre os caracteres da caixa de texto
    public boolean ativarRender; // flag de renderização da caixa de texto

    // contrutor ----------------------------------------------
    public SimpleTextBox(char[] caracteres){
        this.caracteres = new Char[caracteres.length];
        for(short i=0;i<caracteres.length;i++){
            this.caracteres[i] = new Char(caracteres[i]);
        }
        posX=0;
        posY=0;
        space=5;
        ativarRender=false;
    }
}
