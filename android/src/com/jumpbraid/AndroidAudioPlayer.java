package com.jumpbraid;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;

import com.badlogic.gdx.Audio;
import com.jumpbraid.engine.audio.AudioPlayer;
import com.jumpbraid.engine.utils.Recursos;

import java.io.InputStream;
import java.net.URL;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;


public class AndroidAudioPlayer implements AudioPlayer {

    private Decoder mDecoder;
    private AudioTrack mAudioTrack;
    public Context context;

    public AndroidAudioPlayer(Context context) {
        this.context = context;
    }
    public void play(){

        final int sampleRate = 44100;
        final int minBufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize,
                AudioTrack.MODE_STREAM);

        mDecoder = new Decoder();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream in = context.getAssets().open(Recursos.dirAudio+"sample02.mp3");
                    Bitstream bitstream = new Bitstream(in);

                    final int READ_THRESHOLD = 2147483647;
                    int framesReaded = 0;

                    Header header;
                    for (; framesReaded++ <= READ_THRESHOLD && (header = bitstream.readFrame()) != null; ) {
                        SampleBuffer sampleBuffer = (SampleBuffer) mDecoder.decodeFrame(header, bitstream);
                        short[] buffer = sampleBuffer.getBuffer();
                        mAudioTrack.setLoopPoints(0,buffer.length,6);
                        mAudioTrack.write(buffer, 0, buffer.length);

                        bitstream.closeFrame();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        mAudioTrack.play();
    }

}