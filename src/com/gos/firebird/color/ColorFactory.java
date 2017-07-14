package com.gos.firebird.color;

import com.intellij.ui.JBColor;

import java.awt.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * 随机颜色工厂
 */
public class ColorFactory {

    private static final List<Color> COLORS = Arrays.asList(
            JBColor.RED,
            JBColor.ORANGE,
            JBColor.YELLOW,
            JBColor.GREEN,
            JBColor.CYAN,
            JBColor.BLUE,
            JBColor.MAGENTA);


    public static List<Color> getColors() {
        return COLORS;
    }

    private static final SecureRandom SRND = new SecureRandom();

    private static Color getOne() {
        int max = getColors().size();
        int min = 0;
        int index = SRND.nextInt(max) % (max - min + 1) + min;
        return COLORS.get(index);
    }

    /**
     * 获取一个随机色
     *
     * @return 随机Color对象
     */
    public static Color gen() {
        return getOne();
    }

}
