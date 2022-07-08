package com.jumpbraid;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.jumpbraid.App;
import com.jumpbraid.engine.audio.MidiPlayer;

public class AndroidLauncher extends AndroidApplication {


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useImmersiveMode = true;
		MidiPlayer midiPlayer = new AndroidMidiPlayer(getApplicationContext());
		AndroidAudioPlayer audioPlayer = new AndroidAudioPlayer(getApplicationContext());
		initialize(new App(midiPlayer,audioPlayer), config);
	}
}
