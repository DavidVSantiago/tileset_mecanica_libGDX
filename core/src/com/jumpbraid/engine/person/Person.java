package com.jumpbraid.engine.person;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jumpbraid.engine.game.EstadoJogo;
import com.jumpbraid.engine.utils.Camera;
import com.jumpbraid.engine.utils.KeyState;
import com.jumpbraid.engine.utils.Rect;
import com.jumpbraid.engine.utils.RectF;
import com.jumpbraid.engine.utils.Recursos;

public abstract class Person{
    // atributos sprites -------------------------------------------
    public Texture sprite,spriteCaixaColisao,spriteCaixaMove;
    public Rect spriteParado, spriteCorrendo, spritePulando, spriteParede;
    public Rect spriteAtirando, spriteCorrendoAtirando, spritePulandoAtirando;
    public boolean ativarCaixaColisao,ativarCaixaMove;
    //public int paradoQuadro, correndoQuadro;
    // atributos ---------------------------------------------------
	public int largura, altura;
	public float posX, posY;
	public float velX, velY, velBaseX, velBaseY;
    //public float limiteHorizontal,limiteVertical;
    public EstadoPerson ESTADO;
    public Orientacao ORIENTACAO;
    public RectF caixaColisao, caixaMove;
    public int fatorDiminuicaoH,fatorDiminuicaoV;
    private int acumuladorQuadro;
    public boolean bloqueiaMovimentoH,bloqueiaTodoMovimento;
    private Camera camera;


    // construtor --------------------------------------------------
    public Person(){
        camera = Recursos.getInstance().camera;
        bloqueiaMovimentoH = false;
        bloqueiaTodoMovimento = false;
        acumuladorQuadro = 0;
        largura=32; // com base na imagem
		altura=32; // // com base na imagem
        posX=(Recursos.getInstance().LARGURA_TELA/2.0f)-(largura/2);
        posY=(Recursos.getInstance().ALTURA_TELA/2.0f)-(altura/2);
        velBaseX = 1;
        velBaseY = 3;
        velY=0;
        //limiteHorizontal = (Recursos.getInstance().LARGURA_TELA/2.0f);
        //limiteVertical = (Recursos.getInstance().ALTURA_TELA/2.0f);
        ESTADO = EstadoPerson.PARADO;
        ORIENTACAO = Orientacao.DIREITA;
        fatorDiminuicaoH = 8;
        fatorDiminuicaoV = 3;
        caixaColisao = new RectF((posX+fatorDiminuicaoH), (posY)+fatorDiminuicaoV, (posX+largura)-fatorDiminuicaoH,(posY+altura));
        caixaMove = new RectF(Recursos.getInstance().LARGURA_TELA*0.40f,
                                  Recursos.getInstance().ALTURA_TELA*0.38f,
                                  Recursos.getInstance().LARGURA_TELA*0.6f,
                                  Recursos.getInstance().ALTURA_TELA*0.72f);
        ativarCaixaColisao = true;
        ativarCaixaMove = true;
        // carrega o sprite e quadros
        sprite = Recursos.carregarImagem("imgs/charset.png");
        spriteParado = new Rect(0,0,32,32);
        spriteCorrendo = new Rect(32,0,64,32);
        spritePulando = new Rect(64,0,96,32);
        spriteParede = new Rect(96,0,128,32);
        spriteAtirando = new Rect(128,0,160,32);
        spriteCorrendoAtirando = new Rect(160,0,192,32);
        spritePulandoAtirando = new Rect(192,0,224,32);
        spriteCaixaColisao = Recursos.carregarImagem("imgs/caixaColisao.png");
        spriteCaixaMove = Recursos.carregarImagem("imgs/caixaMove.png");
    }

    public void handlerEvents(){
        // se o personagem estiver bloqueado, interrompe aqui
        if(bloqueiaTodoMovimento) return;

        // macanica que gerencia o bloqueio do movimento horizontal
        if(bloqueiaMovimentoH){
            acumuladorQuadro++;
            if(acumuladorQuadro >= 9){ // 9 quadros bloqueados durante o pulo da parede
                bloqueiaMovimentoH=false;
                acumuladorQuadro=0;
            }
        }

        KeyState keyState = Recursos.getInstance().keyState;
        Camera camera = Recursos.getInstance().camera;
        if (!bloqueiaMovimentoH) { // se o movimento Horizontal não estiver bloqueado
            // atribui a velocidade de movimento horizontal
            camera.velX = 0;
            if (keyState.k_direita) {
                camera.velX = camera.velBaseX;
                ORIENTACAO = Orientacao.DIREITA;
            } else if (keyState.k_esquerda) {
                camera.velX = -camera.velBaseX;
                ORIENTACAO = Orientacao.ESQUERDA;
            }
        }
        // se pressionou para cima e não está pulando
        if (keyState.k_cima && !isPULANDO() && !isPULANDO_ATIRANDO()) {
            if(isPAREDE()){ // se o pulo acontece a partir da parede
                bloqueiaMovimentoH = true;
                if(ORIENTACAO==Orientacao.ESQUERDA) // se o personagem está na parede pela direita
                    camera.velX=camera.velBaseX; // move o personagem para direção esquerda
                else// se o personagem está na parede pela esquerda
                    camera.velX=-camera.velBaseX; // move o personagem para direção direita
                entraEstadoPULANDOdaPAREDE();
            }else{ // se o pulo acontece a partir do chão
                entraEstadoPULANDO();
            }
        }
    }

    public void update(){
        // se o personagem estiver bloqueado, interrompe aqui
        if(bloqueiaTodoMovimento) return;

        // a cada quadro o personagem sempre deve começar pulando (para que ele sempre caia quando não colidir com nada)
        if(Recursos.getInstance().keyState.k_atirando)
            ESTADO = EstadoPerson.PULANDO_ATIRANDO;
        else
            ESTADO = EstadoPerson.PULANDO;

        if((isPULANDO() || isPULANDO_ATIRANDO()) && camera.velY<=camera.limiteVelY){
            camera.velY+=camera.decremVelY; // decrementa a velocidade vertical, para o personagem descer
        }
    }

    public void render(SpriteBatch batch) {
        Rect sourceRect = getQuadro();
        
        if(ORIENTACAO==Orientacao.DIREITA)
            batch.draw(sprite, caixaColisao.x1-fatorDiminuicaoH, caixaColisao.y1-fatorDiminuicaoV, largura, altura,
                               sourceRect.x1, sourceRect.y1, sourceRect.x2-sourceRect.x1, sourceRect.y2-sourceRect.y1,
                               false,true);
        if(ORIENTACAO==Orientacao.ESQUERDA)
            batch.draw(sprite, caixaColisao.x1-fatorDiminuicaoH, caixaColisao.y1-fatorDiminuicaoV, largura, altura,
                               sourceRect.x1, sourceRect.y1, sourceRect.x2-sourceRect.x1, sourceRect.y2-sourceRect.y1,
                               true,true);

        if(ativarCaixaMove)
            batch.draw(spriteCaixaMove,caixaMove.x1,caixaMove.y1,caixaMove.x2-caixaMove.x1,caixaMove.y2-caixaMove.y1);
        if(ativarCaixaColisao)
            batch.draw(spriteCaixaColisao,caixaColisao.x1,caixaColisao.y1,caixaColisao.x2-caixaColisao.x1,caixaColisao.y2-caixaColisao.y1);
    }

    // Métodos quadros --------------------------------------------

    public int paradoQuadroCont=0;
    public int correndoQuadroCont=0;
    public Rect getQuadro(){
        if(ESTADO==EstadoPerson.PARADO){
            return spriteParado;
        }else if(ESTADO==EstadoPerson.CORRENDO){
            return spriteCorrendo;
        }else if(ESTADO==EstadoPerson.PULANDO){
            return spritePulando;
        }else if(ESTADO==EstadoPerson.PAREDE){
            return spriteParede;
        }else if(ESTADO==EstadoPerson.ATIRANDO){
            return spriteAtirando;
        }else if(ESTADO==EstadoPerson.CORRENDO_ATIRANDO){
            return spriteCorrendoAtirando;
        }else if(ESTADO==EstadoPerson.PULANDO_ATIRANDO){
            return spritePulandoAtirando ;
        }
        return null;
    }

    // Métodos posicionamento --------------------------------------------

    public void moverHorizontal(){
        caixaColisao.x1 += camera.velX;
        caixaColisao.x2 += camera.velX;
    }

    public void moverVertical(){
        caixaColisao.y1 += camera.velY;
        caixaColisao.y2 += camera.velY;
    }

    public float getCentroX(){
        return posX + (largura/2.0f);
    }
    
    public float getCentroY(){
        return posY + (altura/2.0f);
    }

    public void setPosition(float x, float y){
        float largura = caixaColisao.x2-caixaColisao.x1;
        float altura = caixaColisao.y2-caixaColisao.y1;
        caixaColisao.x1=x;
        caixaColisao.x2=caixaColisao.x1+largura;
        caixaColisao.y1=y;
        caixaColisao.y2=caixaColisao.y1+altura;
    }
    
    // Métodos de colisão --------------------------------------------
    public void checarColisaoLevel(){
        // colisão com limites do Level
        if(posX<0) posX=0;
        if(posX+largura>Recursos.getInstance().LARGURA_TELA)
            posX=Recursos.getInstance().ALTURA_TELA-largura;
    }

    // Métodos Verificadores de ESTADO ----------------------------------
    public boolean isPARADO(){
        return ESTADO==EstadoPerson.PARADO;
    }
    public boolean isATIRANDO(){
        return ESTADO==EstadoPerson.ATIRANDO;
    }
    public boolean isPULANDO(){
        return ESTADO==EstadoPerson.PULANDO;
    }
    public boolean isPULANDO_ATIRANDO(){
        return ESTADO==EstadoPerson.PULANDO_ATIRANDO;
    }
    public boolean isCORRENDO(){
        return ESTADO==EstadoPerson.CORRENDO;
    }
    public boolean isCORRENDO_ATIRANDO(){
        return ESTADO==EstadoPerson.CORRENDO_ATIRANDO;
    }
    public boolean isDANO(){
        return ESTADO==EstadoPerson.DANO;
    }
    public boolean isMORRENDO(){
        return ESTADO==EstadoPerson.MORRENDO;
    }
    public boolean isPAREDE(){
        return ESTADO==EstadoPerson.PAREDE;
    }
    // Métodos de modificação de ESTADO ----------------------------------
    public void entraEstadoPARADO(){
        Camera camera = Recursos.getInstance().camera;
        camera.velY=camera.velBaseY*0.3f; // velicidade de caida mais lenta quando parado, para diminuir a velocidade de caída das plataformas
        if(Recursos.getInstance().keyState.k_atirando)
            ESTADO = EstadoPerson.ATIRANDO;
        else
            ESTADO = EstadoPerson.PARADO;
    }
    public void entraEstadoPAREDE(){
        Camera camera = Recursos.getInstance().camera;
        camera.velY=camera.velBaseYParede;
        ESTADO = EstadoPerson.PAREDE;
    }
    public void entraEstadoPULANDO(){
        Camera camera = Recursos.getInstance().camera;
        camera.velY=-camera.velBaseY;
        if(Recursos.getInstance().keyState.k_atirando)
            ESTADO = EstadoPerson.PULANDO_ATIRANDO;
        else
            ESTADO = EstadoPerson.PULANDO;
    }
    public void entraEstadoPULANDOdaPAREDE(){
        Camera camera = Recursos.getInstance().camera;
        camera.velY=-(camera.velBaseY*0.7f); // a altura do pulo a partir da parede é menor
        ESTADO = EstadoPerson.PULANDO;
    }
    public void entraEstadoCORRENDO(){
        Camera camera = Recursos.getInstance().camera;
        camera.velY=camera.velBaseY*0.3f; // velicidade de caida mais lenta quando parado, para diminuir a velocidade de caída das plataformas
        if(Recursos.getInstance().keyState.k_atirando)
            ESTADO = EstadoPerson.CORRENDO_ATIRANDO;
        else
            ESTADO = EstadoPerson.CORRENDO;
    }

    // Métodos das caixas ---------------------------------------

    public boolean colideCaixaMoveDireita(){
        if(caixaColisao.x2+camera.velX>caixaMove.x2){ // colide à direita do limite de movimentação
            float largura = caixaColisao.x2-caixaColisao.x1;
            caixaColisao.x2 = caixaMove.x2;
            caixaColisao.x1=caixaColisao.x2-largura;
            return true;
        }
        return false;
    }

    public boolean colideCaixaMoveEsquerda(){
        if(caixaColisao.x1+camera.velX<caixaMove.x1){ // colide à esquerda do limite de movimentação
            float largura = caixaColisao.x2-caixaColisao.x1;
            caixaColisao.x1 = caixaMove.x1;
            caixaColisao.x2=caixaColisao.x1+largura;
            return true;
        }
        return false;
    }

    public boolean colideCaixaMoveBaixo(){
        if(caixaColisao.y2+camera.velY>caixaMove.y2){
            float altura = caixaColisao.y2-caixaColisao.y1;
            caixaColisao.y2 = caixaMove.y2;
            caixaColisao.y1=caixaColisao.y2-altura;
            if(caixaColisao.y2>=Recursos.getInstance().ALTURA_TELA){
                Recursos.getInstance().ESTADO=EstadoJogo.MORTO;
                bloqueiaTodoMovimento=true;
            }
            return true;
        }
        return false;
    }

    public boolean colideCaixaMoveCima(){
        if(caixaColisao.y1+camera.velY<caixaMove.y1){
            float altura = caixaColisao.y2-caixaColisao.y1;
            caixaColisao.y1 = caixaMove.y1;
            caixaColisao.y2=caixaColisao.y1+altura;
            return true;
        }
        return false;
    }

    public void resetCaixaMove(){
        caixaMove.x1 = Recursos.getInstance().LARGURA_TELA*0.40f;
        caixaMove.y1 = Recursos.getInstance().ALTURA_TELA*0.38f;
        caixaMove.x2 = Recursos.getInstance().LARGURA_TELA*0.6f;
        caixaMove.y2 = Recursos.getInstance().ALTURA_TELA*0.72f;
    }

    public void setCaixaMoveEsq(){
        caixaMove.x1 = 0f;
    }
    public void setCaixaMoveDir(){
        caixaMove.x2 = Recursos.getInstance().LARGURA_TELA;
    }
    public void setCaixaMoveSup(){
        caixaMove.y1 = 0f;
    }
    public void setCaixaMoveInf(){
        caixaMove.y2 = Recursos.getInstance().ALTURA_TELA;
    }

    // ENUMS ---------------------------------------
    public enum Orientacao{
        ESQUERDA,DIREITA;
    }
}