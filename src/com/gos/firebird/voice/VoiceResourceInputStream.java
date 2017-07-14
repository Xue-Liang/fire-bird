package com.gos.firebird.voice;

import java.io.ByteArrayInputStream;

/**
 * Created by Xue Liang on 2017-07-14.
 */
public class VoiceResourceInputStream extends ByteArrayInputStream {

    public VoiceResourceInputStream(byte[] buf) {
        super(buf);
    }

    public VoiceResourceInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    @Override
    public void reset() {
        this.pos = 0;
        this.mark = 0;
        this.count = this.buf.length;
    }
}
