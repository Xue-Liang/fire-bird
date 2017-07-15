package com.gos.firebird.voice;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

/**
 * Created by Xue Liang on 2017-07-14.
 */
public class VoiceResource {
    private static String[] voices = {"letter.wav"};


    public final static AudioInputStream[] NormalAudioStreams = new AudioInputStream[voices.length];

    public static final AudioInputStream EnterAudioStream;

    static {
        for (int i = 0; i < voices.length; i++) {
            try (InputStream is = VoiceResource.class.getClassLoader().getResourceAsStream(voices[i])) {
                NormalAudioStreams[i] = toAudioInputStream(is);
            } catch (Exception e) {
            }
        }

        InputStream dv = VoiceResource.class.getClassLoader().getResourceAsStream("dv.wav");
        EnterAudioStream = toAudioInputStream(dv);
    }

    private static SecureRandom SRND = new SecureRandom();

    public static AudioInputStream getAudioStream() {
        int ix = SRND.nextInt(voices.length);
        return NormalAudioStreams[ix];
    }

    public static void reset(AudioInputStream is) {
        try {
            is.reset();
        } catch (IOException e) {
        }
    }

    private static AudioInputStream toAudioInputStream(InputStream ins) {
        try (final InputStream is = ins) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(is.available());
            int b;
            while ((b = is.read()) > -1) {
                bos.write(b);
            }
            return AudioSystem.getAudioInputStream(new ByteArrayInputStream(bos.toByteArray()));
        } catch (Exception e) {
            return null;
        }
    }
}
