package com.gos.firebird.shake;

import kotlin.Pair;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 震动控制器
 * <p>
 */
public class ShakeManager {
    private static final int SHAKE_MIN = 1;
    private static final int SHAKE_MAX = 3;

    private static ShakeManager mShakeManager;

    private boolean isShaking = false;
    private boolean isEnable = false;
    private java.util.Timer mTimer = new Timer();
    private JComponent mNowEditorJComponent;

    public static ShakeManager getInstance() {

        if (mShakeManager == null) {
            mShakeManager = new ShakeManager();
        }
        return mShakeManager;
    }

    public ShakeManager() {
    }

    public void init(JComponent jComponent) {
        this.mNowEditorJComponent = jComponent;
        if (mTimer == null)
            mTimer = new Timer();
        isEnable = true;
    }

    public void reset(JComponent jComponent) {
        clear();
        init(jComponent);
    }

    public void clear() {
        isEnable = false;
        mNowEditorJComponent = null;
    }

    public void destroy() {
        clear();
        mTimer = null;
    }

    public void shake() {
        if (isEnable) {
            isShaking = true;

            int x = shakeIntensity(SHAKE_MIN, SHAKE_MAX);
            int y = shakeIntensity(SHAKE_MIN, SHAKE_MAX);

            if (mTimer != null) {
                mTimer.schedule(new TimerTask() {
                    public void run() {
                        try {
                            SwingUtilities.invokeLater(new ChangingLocationOfComponent(mNowEditorJComponent, x, y));
                            Thread.sleep(75);
                            SwingUtilities.invokeLater(new ChangingLocationOfComponent(mNowEditorJComponent, 0, 0));
                            isShaking = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1);
            }
        } else {
            System.out.println("还没初始化 ShakeManager");
        }

    }

    /**
     * changing the layout location of the JComponent
     */
    private class ChangingLocationOfComponent implements Runnable {
        private final JComponent target;
        private final Pair<Integer, Integer> location;

        ChangingLocationOfComponent(JComponent component, int x, int y) {
            target = component;
            location = new Pair<>(x, y);
        }

        @Override
        public void run() {
            if (target != null) {
                target.setLocation(location.getFirst(), location.getSecond());
            }
        }
    }

    private int shakeIntensity(int min, int max) {
        int direction = Math.random() > 0.5 ? -1 : 1;
        return ((int) Math.round(Math.random() * (max - min) + min)) * direction;
    }

    public boolean isShaking() {
        return isShaking;
    }

    public JComponent getNowEditorJComponent() {
        return mNowEditorJComponent;
    }

    public boolean isEnable() {
        return isEnable;
    }

}
