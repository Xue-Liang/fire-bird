package com.gos.firebird.voice;

import javax.sound.sampled.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Xue Liang on 2017-07-14.
 */
public class PaPi implements Runnable {
    private final AtomicBoolean Lock = new AtomicBoolean(false);

    public static final PaPi INSTANCE = new PaPi();

    static {
        Thread t = new Thread(INSTANCE, "PaPi");
        t.start();
    }


    private AudioInputStream audioInputStream = null;

    private PaPi() {

    }


    public static void papi() {
        if (!INSTANCE.Lock.get()) {
            AudioInputStream audioStream = VoiceResource.getAudioStream();
            papi(audioStream);
        }
    }

    public static void papi(AudioInputStream audioStream) {
        if (!INSTANCE.Lock.get()) {
            INSTANCE.setAudioInputStream(audioStream);
            INSTANCE.notifyPlay();
        }
    }


    @Override
    public void run() {
        while (true) {
            Lock.set(true);
            SourceDataLine line = null;
            try {
                AudioFormat format = this.audioInputStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                int size = 0;
                byte[] buff = new byte[8192];
                while ((size = this.audioInputStream.read(buff)) > -1) {
                    line.write(buff, 0, size);
                }
                this.audioInputStream.reset();
            } catch (Exception e) {
            } finally {
                if (line != null) {
                    line.drain();
                    line.close();
                }
            }
            Lock.set(false);
            this.waitFor(0);
        }

    }

    private void waitFor(long ms) {
        synchronized (Lock) {
            try {
                Lock.wait(ms);
            } catch (InterruptedException e) {
            }
        }
    }

    public void notifyPlay() {
        if (!Lock.get()) {
            synchronized (Lock) {
                Lock.notifyAll();
            }
        }
    }

    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }


}
