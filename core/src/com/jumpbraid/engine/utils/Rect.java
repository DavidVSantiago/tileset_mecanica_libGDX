package com.jumpbraid.engine.utils;

public class Rect {
    // atributos ----------------------------------------------
    public short x1, y1;
    public short x2, y2;

    // construtor ---------------------------------------------
    public Rect(short x1, short y1, short x2, short y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    // métodos de colisão ------------------------------------------------
    public boolean intersedeEsquerda(Rect outro){
        return x2>=outro.x1;
    }
    public boolean intersedeEsquerda(RectF outro){ // versão RectF
        return x2>=outro.x1;
    }
    public boolean intersedeDireita(Rect outro){
        return x1<=outro.x2;
    }
    public boolean intersedeDireita(RectF outro){ // versão RectF
        return x1<=outro.x2;
    }
    public boolean intersedeCima(Rect outro){
        return y2>=outro.y1;
    }
    public boolean intersedeCima(RectF outro){ // versão RectF
        return y2>=outro.y1;
    }
    public boolean intersedeBaixo(Rect outro){
        return y1<=outro.y2;
    }
    public boolean intersedeBaixo(RectF outro){ // versão RectF
        return y1<=outro.y2;
    }
    public boolean intersedeTodo(Rect outro){
        return (intersedeEsquerda(outro) &&
                intersedeDireita(outro) &&
                intersedeCima(outro) &&
                intersedeBaixo(outro));
    }
    public boolean intersedeTodo(RectF outro){ // versão RectF
        return (intersedeEsquerda(outro) &&
                intersedeDireita(outro) &&
                intersedeCima(outro) &&
                intersedeBaixo(outro));
    }
}
