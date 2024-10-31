package io.github.geniusay.core.builder;

import java.awt.*;
import java.util.Random;

/**
 * @Date 2023/12/5
 * @Author xiaochun
 */
public class Graphics2DBuilder {
    private final Graphics2D graphics2D;

    private String code;

    private int dotSize;

    public Graphics2DBuilder(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
    }

    public Graphics2DBuilder font(Color color, Font font){
        graphics2D.setColor(color);
        graphics2D.setFont(font);
        return this;
    }

    public Graphics2DBuilder backgroundColor(Color color, int width, int height){
        graphics2D.setColor(color);
        graphics2D.fillRect(0, 0, width, height);
        return this;
    }

    public Graphics2DBuilder code(String code){
        this.code = code;
        return this;
    }

    public Graphics2DBuilder dot(int size){
        this.dotSize = size;
        return this;
    }

    public Graphics2DBuilder draw(int width, int height){
        graphics2D.drawString(code, 25, 35);
        Random random = new Random();
        for (int i = 0; i < this.dotSize; i++) {
            int dotX = random.nextInt(width);
            int dotY = random.nextInt(height);
            graphics2D.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            graphics2D.fillRect(dotX, dotY, 2, 2);
        }
        return this;
    }

    public boolean done(){
        graphics2D.dispose();
        return true;
    }
}
