package com.gos;

import com.gos.firebird.voice.Voice;
import sun.audio.AudioData;
import sun.audio.AudioDevice;
import sun.audio.AudioPlayer;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Xue Liang on 2017-07-13.
 */
public class Test {
    private static final ByteArrayInputStream audioStream = Voice.getAudioStream();
    private static final AudioDevice audioDevice = AudioDevice.device;
    private static long keydownTime = 0;
    AudioData data = null;

    public static void main(String... args) throws InterruptedException, IOException {
        audioDevice.open();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println("第" + (i + 1) + "次播放.");
            papi();
        }
    }

    static void papi() {
        if (System.currentTimeMillis() - keydownTime < 3000) {
            return;
        }

        AudioPlayer.player.stop(Voice.getAudioStream());

        keydownTime = System.currentTimeMillis();

        Voice.reset();

        AudioPlayer.player.start(Voice.getAudioStream());
    }

}
/**
 * fdsfdsfhjksdfajsdfjfjdsjfk l dkj fdksjflkjdksflkajldsfjkasf
 * fffdafdfdsafasdfasdfa
 */