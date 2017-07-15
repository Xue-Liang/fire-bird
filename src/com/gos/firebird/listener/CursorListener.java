package com.gos.firebird.listener;

import com.gos.firebird.particle.Paper;
import com.gos.firebird.voice.PaPi;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;

import java.awt.*;

/**
 * 光标监听接口
 */
public class CursorListener implements CaretListener {
    @Override
    public void caretPositionChanged(CaretEvent caretEvent) {
        Editor editor = caretEvent.getEditor();
        Point point = editor.logicalPositionToXY(caretEvent.getNewPosition());
        Paper.getInstance().setCurrentCaretPosition(point);
    }

    @Override
    public void caretAdded(CaretEvent caretEvent) {
    }

    @Override
    public void caretRemoved(CaretEvent caretEvent) {

    }
}
