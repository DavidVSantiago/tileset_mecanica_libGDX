package com.mygdx.game.engine.person;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.engine.game.EstadoJogo;
import com.mygdx.game.engine.game.IGameloop;
import com.mygdx.game.engine.scene.SceneManager;
import com.mygdx.game.engine.utils.Camera;
import com.mygdx.game.engine.utils.KeyState;
import com.mygdx.game.engine.utils.Rect;
import com.mygdx.game.engine.utils.RectF;
import com.mygdx.game.engine.utils.Recursos;

public abstract class Person implements IGameloop{
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
        ESTADO=EstadoPerson.PULANDO;
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
        ativarCaixaColisao = false;
        ativarCaixaMove = false;
        // carrega o sprite e quadros
        sprite = Recursos.getInstance().charset;
        spriteParado = new Rect((short)0,(short)0,(short)32,(short)32);
        spriteCorrendo = new Rect((short)32,(short)0,(short)64,(short)32);
        spritePulando = new Rect((short)64,(short)0,(short)96,(short)32);
        spriteParede = new Rect((short)96,(short)0,(short)128,(short)32);
        spriteAtirando = new Rect((short)128,(short)0,(short)160,(short)32);
        spriteCorrendoAtirando = new Rect((short)160,(short)0,(short)192,(short)32);
        spritePulandoAtirando = new Rect((short)192,(short)0,(short)224,(short)32);
        spriteCaixaColisao = Recursos.getInstance().caixaColisao;
        spriteCaixaMove = Recursos.getInstance().caixaMove;
    }
    
    public void reinicia(){
        ESTADO=EstadoPerson.PULANDO;
        bloqueiaMovimentoH = false;
        bloqueiaTodoMovimento = false;
        acumuladorQuadro = 0;
        posX=(Recursos.getInstance().LARGURA_TELA/2.0f)-(largura/2);
        posY=(Recursos.getInstance().ALTURA_TELA/2.0f)-(altura/2);
        velBaseX = 1;
        velBaseY = 3;
        velY=0;
        ORIENTACAO = Orientacao.DIREITA;
        fatorDiminuicaoH = 8;
        fatorDiminuicaoV = 3;
        caixaColisao = new RectF((posX+fatorDiminuicaoH), (posY)+fatorDiminuicaoV, (posX+largura)-fatorDiminuicaoH,(posY+altura));
        caixaMove = new RectF(Recursos.getInstance().LARGURA_TELA*0.40f,
                                  Recursos.getInstance().ALTURA_TELA*0.38f,
                                  Recursos.getInstance().LARGURA_TELA*0.6f,
                                  Recursos.getInstance().ALTURA_TELA*0.72f);
    }

    // M??todos gameloop -----------------------/

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
        if (!bloqueiaMovimentoH) { // se o movimento Horizontal n??o estiver bloqueado
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
        // se pressionou para cima e n??o est?? pulando
        if (keyState.k_cima && !isPULANDO() && !isPULANDO_ATIRANDO()) {
            if(isPAREDE()){ // se o pulo acontece a partir da parede
                bloqueiaMovimentoH = true;
                if(ORIENTACAO==Orientacao.ESQUERDA) // se o personagem est?? na parede pela direita
                    camera.velX=camera.velBaseX; // move o personagem para dire????o esquerda
                else// se o personagem est?? na parede pela esquerda
                    camera.velX=-camera.velBaseX; // move o personagem para dire????o direita
                entraEstadoPULANDOdaPAREDE();
            }else{ // se o pulo acontece a partir do ch??o
                entraEstadoPULANDO();
            }
        }
    }

    @Override
    public void update(){
        // se o personagem estiver bloqueado, interrompe aqui
        if(bloqueiaTodoMovimento) return;

        // a cada quadro o personagem sempre deve come??ar pulando (para que ele sempre caia quando n??o colidir com nada)
        if(Recursos.getInstance().keyState.k_atirando)
            ESTADO = EstadoPerson.PULANDO_ATIRANDO;
        else
            ESTADO = EstadoPerson.PULANDO;

        if((isPULANDO() || isPULANDO_ATIRANDO()) && camera.velY<=camera.limiteVelY){
            camera.velY+=camera.decremVelY; // decrementa a velocidade vertical, para o personagem descer
        }
    }
    @Override
    public void render() {
        Rect sourceRect = getQuadro();
        
        if(ORIENTACAO==Orientacao.DIREITA)
            Recursos.getInstance().batch.draw(sprite, caixaColisao.x1-fatorDiminuicaoH, caixaColisao.y1-fatorDiminuicaoV, largura, altura,
                               sourceRect.x1, sourceRect.y1, sourceRect.x2-sourceRect.x1, sourceRect.y2-sourceRect.y1,
                               false,true);
        if(ORIENTACAO==Orientacao.ESQUERDA)
            Recursos.getInstance().batch.draw(sprite, caixaColisao.x1-fatorDiminuicaoH, caixaColisao.y1-fatorDiminuicaoV, largura, altura,
                               sourceRect.x1, sourceRect.y1, sourceRect.x2-sourceRect.x1, sourceRect.y2-sourceRect.y1,
                               true,true);

        if(ativarCaixaMove)
            Recursos.getInstance().batch.draw(spriteCaixaMove,caixaMove.x1,caixaMove.y1,caixaMove.x2-caixaMove.x1,caixaMove.y2-caixaMove.y1);
        if(ativarCaixaColisao)
            Recursos.getInstance().batch.draw(spriteCaixaColisao,caixaColisao.x1,caixaColisao.y1,caixaColisao.x2-caixaColisao.x1,caixaColisao.y2-caixaColisao.y1);
    }

    // M??todos quadros --------------------------------------------

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

    // M??todos posicionamento --------------------------------------------

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
    
    // M??todos de colis??o --------------------------------------------
    public void checarColisaoLevel(){
        // colis??o com limites do Level
        if(posX<0) posX=0;
        if(posX+largura>Recursos.getInstance().LARGURA_TELA)
            posX=Recursos.getInstance().ALTURA_TELA-largura;
    }

    // M??todos Verificadores de ESTADO ----------------------------------
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
    // M??todos de modifica????o de ESTADO ----------------------------------
    public void entraEstadoPARADO(){
        Camera camera = Recursos.getInstance().camera;
        camera.velY=camera.velBaseY*0.3f; // velicidade de caida mais lenta quando parado, para diminuir a velocidade de ca??da das plataformas
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
        camera.velY=-(camera.velBaseY*0.7f); // a altura do pulo a partir da parede ?? menor
        ESTADO = EstadoPerson.PULANDO;
    }
    public void entraEstadoCORRENDO(){
        Camera camera = Recursos.getInstance().camera;
        camera.velY=camera.velBaseY*0.3f; // velicidade de caida mais lenta quando parado, para diminuir a velocidade de ca??da das plataformas
        if(Recursos.getInstance().keyState.k_atirando)
            ESTADO = EstadoPerson.CORRENDO_ATIRANDO;
        else
            ESTADO = EstadoPerson.CORRENDO;
    }

    // M??todos das caixas ---------------------------------------

    public boolean colideCaixaMoveDireita(){
        if(caixaColisao.x2+camera.velX>caixaMove.x2){ // colide ?? direita do limite de movimenta????o
            float largura = caixaColisao.x2-caixaColisao.x1;
            caixaColisao.x2 = caixaMove.x2;
            caixaColisao.x1=caixaColisao.x2-largura;
            return true;
        }
        return false;
    }

    public boolean colideCaixaMoveEsquerda(){
        if(caixaColisao.x1+camera.velX<caixaMove.x1){ // colide ?? esquerda do limite de movimenta????o
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
                bloqueiaTodoMovimento=true;
                SceneManager.agendarTransicaoEstadoJogo(1e+9,EstadoJogo.MORTO);
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