package com.gos.firebird.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.gos.firebird.FireBirdManager;
import com.gos.firebird.config.Config;


/**
 * ActivatePower 开启 Action
 */
public class FireBirdEnableAction extends AnAction {

    private Config.State state = Config.getInstance().state;

    public FireBirdEnableAction() {
        if (state.IS_ENABLE) {
            showEnable(getTemplatePresentation());
        } else {
            showDisable(getTemplatePresentation());
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (state.IS_ENABLE) {
            disable(e.getPresentation());
            FireBirdManager.getInstance().destroyAll();
        } else {
            enable(e.getPresentation());
            FireBirdManager.getInstance().init(e.getProject());
        }
    }

    private void disable(Presentation presentation) {
        state.IS_ENABLE = false;
        showDisable(presentation);
    }

    private void enable(Presentation presentation) {
        state.IS_ENABLE = true;
        showEnable(presentation);
    }

    private void showEnable(Presentation presentation) {
        presentation.setIcon(AllIcons.General.InspectionsOK);
        presentation.setDescription("enable");
        presentation.setText("enable");
    }

    private void showDisable(Presentation presentation) {
        presentation.setIcon(AllIcons.Actions.Cancel);
        presentation.setDescription("disable");
        presentation.setText("disable");
    }
}
