package com.gos.firebird.voice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Xue Liang on 2017-07-14.
 */
public class Voice {
    private static VoiceResourceInputStream audioStream;
    static {
        try (InputStream is = VoiceResourceInputStream.class.getClassLoader().getResourceAsStream("keyboard.wav")) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(is.available());
            int b;
            while ((b = is.read()) > -1) {
                bos.write(b);
            }
            audioStream = new VoiceResourceInputStream(bos.toByteArray());
        } catch (Exception e) {
        }
    }

    public static ByteArrayInputStream getAudioStream() {
        return audioStream;
    }

    public static void reset() {
        audioStream.reset();
    }
}
