package com.gos.firebird.listener;


import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.gos.firebird.FireBirdManager;
import com.gos.firebird.color.ColorFactory;
import com.gos.firebird.config.Config;
import com.gos.firebird.particle.Paper;
import com.gos.firebird.shake.ShakeManager;

import java.awt.*;
import java.util.ArrayList;

/**
 * 震动文本监听接口
 * Created by suika on 15-12-13.
 */
public class EditorDocumentListener implements DocumentListener {

    private Config.State state = Config.getInstance().state;

    private Project mProject;
    private ArrayList<Document> mDocumentList = new ArrayList<>();

    private Editor mEditor;
    private CaretModel mCaretModel;

    private CursorListener mActivatePowerCaretListener = new CursorListener();

    public EditorDocumentListener(Project project) {
        mProject = project;
    }

    @Override
    public void beforeDocumentChange(DocumentEvent documentEvent) {

        FireBirdManager manage = FireBirdManager.getInstance();

        if (state.IS_COMBO) {
            // 文本变化在 CLICK_TIME_INTERVAL 时间内
            if (System.currentTimeMillis() - manage.getClickTimeStamp() <= state.CLICK_TIME_INTERVAL) {
                manage.setClickCombo(manage.getClickCombo() + 1);
                state.MAX_CLICK_COMBO = manage.getClickCombo() > state.MAX_CLICK_COMBO ? manage.getClickCombo() : state.MAX_CLICK_COMBO;
            } else
                manage.setClickCombo(0);

            manage.setClickTimeStamp(System.currentTimeMillis());
        }

        if ((state.IS_COMBO && manage.getClickCombo() > state.OPEN_FUNCTION_BORDER && mProject != null) || (!state.IS_COMBO && mProject != null))
            handleActivatePower(manage);
    }

    @Override
    public void documentChanged(DocumentEvent documentEvent) {

    }

    public boolean addDocument(Document document) {
        if (!mDocumentList.contains(document)) {
            mDocumentList.add(document);
            return true;
        } else {
            return false;
        }
    }

    private void unbindDocument(Document document, boolean isRemoveInList) {
        if (document != null && mDocumentList.contains(document)) {
            document.removeDocumentListener(this);
            if (isRemoveInList)
                mDocumentList.remove(document);
        }
    }

    public void clean(Document document, boolean isRemoveInList) {
        cleanEditorCaret();
        if (document != null)
            unbindDocument(document, isRemoveInList);
    }

    private void cleanEditorCaret() {
        if (mCaretModel != null && mActivatePowerCaretListener != null) {
            mCaretModel.removeCaretListener(mActivatePowerCaretListener);
            mCaretModel = null;
        }
        mEditor = null;
    }

    public void destroy() {
        for (Document document : mDocumentList)
            clean(document, false);
        mDocumentList.clear();
        mActivatePowerCaretListener = null;
        mProject = null;
    }

    /**
     * 处理ActivatePower效果
     */
    private void handleActivatePower(FireBirdManager manage) {
        Editor selectedTextEditor = FileEditorManager.getInstance(mProject).getSelectedTextEditor();
        if (mEditor == null || mEditor != selectedTextEditor)
            mEditor = selectedTextEditor;
        if (mEditor != null) {
            manage.resetEditor(mEditor);

            if (state.IS_SHAKE) {
                if (ShakeManager.getInstance().isEnable() && !ShakeManager.getInstance().isShaking())
                    ShakeManager.getInstance().shake();
            }

            if (state.IS_SPARK) {
                CaretModel currentCaretModel = mEditor.getCaretModel();
                if (mCaretModel == null || mCaretModel != currentCaretModel) {
                    mCaretModel = currentCaretModel;
                    mCaretModel.addCaretListener(mActivatePowerCaretListener);
                }

                Color color;
                if (state.IS_COLORFUL) {
                    color = ColorFactory.gen(); // 生成一个随机颜色
                } else {
                    color = mEditor.getColorsScheme().getDefaultForeground();
                }

                int fontSize = mEditor.getContentComponent().getFont().getSize();

                Paper paper = Paper.getInstance();
                if (paper.isEnable()) {
                    paper.sparkAtPositionAction(color, fontSize);
                }
            }
        }
    }
}
