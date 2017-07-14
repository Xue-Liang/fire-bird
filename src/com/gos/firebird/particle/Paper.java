package com.gos.firebird.particle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 粒子容器
 * <p>
 */
public class Paper implements Runnable, Border {
    private static final int MAX_PARTICLE_COUNT = 256;

    private static Paper mPaper;

    private int mParticleIndex = 0;
    private ConcurrentHashMap<String, ParticleView> mParticleViews = new ConcurrentHashMap<>();

    private JComponent editorComponent;

    private int backgroundWidth, backgroundHeight;
    private BufferedImage backgroundImage;
    private Graphics2D backgroundGraphic;
    private Point cursorPoint = new Point();
    private Point mParticleAreaSpeed = new Point();
    private static final Object Locker = new Object();
    //当前光标的坐标点
    private Point cursorPosition = null;

    private Thread mPThread;

    private boolean isEnable = false;

    public static Paper getInstance() {
        if (mPaper == null) {
            mPaper = new Paper();
        }
        return mPaper;
    }

    private Paper() {
    }

    @Override
    public void run() {
        while (isEnable) {
            if (backgroundGraphic != null) {
                backgroundGraphic.setBackground(new Color(0x00FFFFFF, true));
                backgroundGraphic.clearRect(0, 0, backgroundWidth, backgroundHeight);

                for (String key : mParticleViews.keySet()) {
                    ParticleView particleView = mParticleViews.get(key);
                    if (particleView != null && particleView.isEnable()) {
                        backgroundGraphic.setColor(particleView.mColor);
                        backgroundGraphic.fillOval((int) particleView.x, (int) particleView.y, ParticleView.PARTICLE_WIDTH, ParticleView.PARTICLE_WIDTH);

                        update(particleView);
                    }
                }

                if (editorComponent != null)
                    editorComponent.repaint();
            }
            try {
                synchronized (Locker) {
                    Locker.wait(35);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (backgroundImage == null)
            return;

        Graphics2D graphics2 = (Graphics2D) g;
        graphics2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
        Point point = ParticlePositionCalculateUtil.getParticleAreaPositionOnEditorArea(cursorPoint, backgroundWidth, backgroundHeight);
        graphics2.drawImage(backgroundImage, point.x, point.y, c);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    public void init(JComponent jComponent) {

        if (mParticleViews == null) {
            mParticleIndex = 0;
            mParticleViews = new ConcurrentHashMap<>();
        }

        if (mPThread == null)
            mPThread = new Thread(this);

        if (editorComponent != null) {
            editorComponent.setBorder(null);
            editorComponent = null;
        }

        editorComponent = jComponent;

        updateDrawer(jComponent);
    }

    public void reset(JComponent jComponent) {
        clear();
        init(jComponent);
        setEnableAction(true);
    }

    public void clear() {
        isEnable = false;

        if (mPThread != null) {
            synchronized (Locker) {
                Locker.notifyAll();
            }
            mPThread = null;
        }

        if (editorComponent != null) {
            editorComponent.setBorder(null);
            editorComponent = null;
        }

        if (backgroundGraphic != null)
            backgroundGraphic = null;

        if (backgroundImage != null)
            backgroundImage = null;
    }

    public void destroy() {
        clear();

        if (mParticleViews != null)
            mParticleViews.clear();
        mParticleViews = null;
    }

    public void update(ParticleView particleView) {
        if (particleView.mAlpha <= 0.1) {
            particleView.setEnable(false);
            return;
        }

        particleView.update();
    }

    public void updateDrawer(Component jComponent) {
        backgroundWidth = ParticlePositionCalculateUtil.getParticleAreaWidth(jComponent.getFont().getSize());
        backgroundHeight = ParticlePositionCalculateUtil.getParticleAreaHeight(jComponent.getFont().getSize());

        backgroundImage = new BufferedImage(backgroundWidth, backgroundHeight, BufferedImage.TYPE_INT_BGR);
        backgroundGraphic = backgroundImage.createGraphics();
        /** 设置 透明窗体背景 */
        backgroundImage = backgroundGraphic.getDeviceConfiguration().createCompatibleImage(backgroundWidth, backgroundHeight, Transparency.TRANSLUCENT);
        backgroundGraphic.dispose();
        backgroundGraphic = backgroundImage.createGraphics();
        /** 设置 透明窗体背景 END */
    }

    private void particlesDeviation(Point speed) {
        for (String key : mParticleViews.keySet()) {
            ParticleView particle = mParticleViews.get(key);
            particle.setX(particle.x - speed.x);
            particle.setY(particle.y - speed.y);
        }
    }

    public void setEnableAction(boolean isEnable) {
        this.isEnable = isEnable;
        if (this.isEnable) {
            if (backgroundImage != null && backgroundGraphic != null && editorComponent != null) {
                if (mPThread == null) {
                    mPThread = new Thread(this);
                }
                mPThread.start();
            } else {
                this.isEnable = false;
                System.out.println("还没初始化 Paper");
            }
        } else {
            destroy();
        }
    }

    public void sparkAtPosition(Point position, Color color, int fontSize) {
        mParticleAreaSpeed.setLocation(position.x - cursorPoint.x, position.y - cursorPoint.y);
        particlesDeviation(mParticleAreaSpeed);
        cursorPoint = position;

        backgroundWidth = ParticlePositionCalculateUtil.getParticleAreaWidth(fontSize);
        backgroundHeight = ParticlePositionCalculateUtil.getParticleAreaHeight(fontSize);

        Point particlePoint = ParticlePositionCalculateUtil.getParticlePositionOnArea(backgroundWidth, backgroundHeight);

        int particleNumber = 5 + (int) Math.round(Math.random() * 5);

        for (int i = 0; i < particleNumber; i++) {

            if (mParticleIndex >= MAX_PARTICLE_COUNT) {
                mParticleViews.get(String.valueOf(mParticleIndex % MAX_PARTICLE_COUNT)).reset(particlePoint, color, true);
            } else {
                ParticleView particleView = new ParticleView(particlePoint, color, true);
                mParticleViews.put(String.valueOf(mParticleIndex), particleView);
            }

            if (mParticleIndex < MAX_PARTICLE_COUNT * 10)
                mParticleIndex++;
            else
                mParticleIndex = MAX_PARTICLE_COUNT;
        }
    }

    public void sparkAtPositionAction(Color color, int fontSize) {
        if (cursorPosition != null) {
            sparkAtPosition(cursorPosition, color, fontSize);
            cursorPosition = null;
        }
    }

    public boolean isEnable() {
        return isEnable;
    }

    public JComponent getNowEditorJComponent() {
        return editorComponent;
    }

    public void setCurrentCaretPosition(Point cusorPosition) {
        if (cursorPosition == null)
            cursorPosition = cusorPosition;
    }
}
