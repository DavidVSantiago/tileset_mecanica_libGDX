package com.mygdx.game;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import com.mygdx.game.engine.audio.AudioPlayer;
import com.mygdx.game.engine.utils.Recursos;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AndroidAudioPlayer implements AudioPlayer {

    // atributos
    private Context context;
    private int contador = 1;
    private MediaPlayer playerAtual = null;
    private MediaPlayer playerProximo = null;
    private AssetFileDescriptor afd;

    public AndroidAudioPlayer(Context context) {
        this.context = context;
        try {
            afd = context.getAssets().openFd(Recursos.dirAudio + "01intro.mid");
            playerAtual = new MediaPlayer();
            playerAtual.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            playerAtual.prepare();
            criarProximo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void criarProximo() {
        try {
            playerProximo = new MediaPlayer();
            playerProximo.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            playerProximo.prepare();
            playerAtual.setOnCompletionListener(onCompletionListener);
            playerAtual.setNextMediaPlayer(playerProximo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.d("Som", "On completation");
            mediaPlayer.reset();
            playerAtual = playerProximo;
            criarProximo();
        }
    };

    public void play() {
        playerAtual.start();
    }

}