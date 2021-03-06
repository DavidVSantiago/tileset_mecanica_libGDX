package com.mygdx.game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.JetPlayer;
import android.media.MediaPlayer;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.mygdx.game.engine.audio.MidiPlayer;
import com.mygdx.game.engine.utils.Recursos;

public class AndroidMidiPlayer implements MidiPlayer {

private MediaPlayer mediaPlayer;
private JetPlayer jetPlayer;
private Context context;
private boolean looping;
private float volume;

public AndroidMidiPlayer(Context context) {
    this.context = context;
    this.mediaPlayer = new MediaPlayer();
    this.looping = false;
    this.volume = 1;
}

public void open(String fileName) {

    reset();

    try {
        AssetFileDescriptor afd = context.getAssets().openFd(Recursos.dirAudio+fileName);
        mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        mediaPlayer.prepare();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

//TODO: This should probably be replaced with something better.
//I had to reset the player to avoid error when
//opening a second midi file.
private void reset() {
    mediaPlayer.reset();
    mediaPlayer.setLooping(looping);
    setVolume(volume);
}


public boolean isLooping() {
    return mediaPlayer.isLooping();
}

public void setLooping(boolean loop) {
    mediaPlayer.setLooping(loop);
}

public void play() {
    mediaPlayer.start();
}

public void pause() {
    mediaPlayer.pause();
}

public void stop() {
    mediaPlayer.stop();
}

public void release() {
    mediaPlayer.release();
}

public boolean isPlaying() {
    return mediaPlayer.isPlaying();
}

public void setVolume(float volume) {
    mediaPlayer.setVolume(volume, volume);
}

}