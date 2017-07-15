package com.gos;

import com.gos.firebird.voice.PaPi;
import com.gos.firebird.voice.VoiceResource;

import java.io.IOException;

/**
 * Created by Xue Liang on 2017-07-13.
 */
public class Test {
    private static long keydownTime = 0;

    public static void main(String... args) throws InterruptedException, IOException {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            PaPi.papi();
            PaPi.papi(VoiceResource.EnterAudioStream);
            Thread.sleep(2000);
        }
    }

}
/***
 */