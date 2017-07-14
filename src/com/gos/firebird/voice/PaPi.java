package com.gos.firebird.voice;

import sun.audio.AudioPlayer;

/**
 * Created by Xue Liang on 2017-07-14.
 */
public class PaPi {
    private static long keydownTime = 0;

    public static void papi() {

        if (System.currentTimeMillis() - keydownTime < 3000) {
            return;
        }

        AudioPlayer.player.stop(Voice.getAudioStream());

        keydownTime = System.currentTimeMillis();

        Voice.reset();

        AudioPlayer.player.start(Voice.getAudioStream());
    }
}
