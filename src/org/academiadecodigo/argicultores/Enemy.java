package org.academiadecodigo.argicultores;

import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.concurrent.TimeUnit;

//public class Enemy extends Rectangle {
public class Enemy extends Picture {

    private boolean steps;
    private boolean goRight;
    private int platformN;

    public Enemy(double x, double y) {
        super(x,y, "AlienLeft1.png");
        draw();
        if (y == 540) {
            goRight = true;
            platformN = 0;
        } else if (y==360) {
            goRight = false;
            platformN = 1;
        } else {
            goRight = true;
            platformN = 2;
        }
        steps = false;
    }

    public void move(int rounds) {
        if (goRight) {
            if (steps) {
                load("AlienRight1.gif");
            }
            translate(Math.ceil(rounds/1.5),0);
        } else {
            if (steps) {
                load("AlienLeft1.png");
            }
            translate(-Math.ceil(rounds/1.5),0);
        }
        /**
         *
         * testar se cai das plataformas e cenas
         *
         * **/
        if ((getX()) > 450 && getY() == 180 && platformN == 2 && goRight) {
            translate(0,180);
            platformN--;
        }
        if ((getX()+getWidth() < 460 && getY() == 360 && platformN == 1 && !goRight)) {
            translate(0,180);
            platformN--;
        }
        if ((getX() > 450 && getY() == 540 && platformN == 0 && goRight)) {
            translate(0,180);
        }


        if (goRight) {
            if ((getX()+getWidth()) < 909) {
                try {
                    translate(0.3,0);
                    load("AlienRight1.gif");
                    TimeUnit.MILLISECONDS.sleep(1);
                    translate(0.3,0);
                    TimeUnit.MILLISECONDS.sleep(1);
                    load("AlienRight2.gif");
                    translate(0.4,0);
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (Exception ex) {

                }

            } else {
                goRight = false;
            }
        } else {
            if (getX() > 10) {
                try {
                translate(-0.3,0);
                load("AlienLeft2.png");
                TimeUnit.MILLISECONDS.sleep(1);
                translate(-0.3,0);
                TimeUnit.MILLISECONDS.sleep(1);
                load("AlienLeft1.png");
                TimeUnit.MILLISECONDS.sleep(1);
                translate(-0.4,0);}catch (Exception ex) {}
            } else {
                goRight = true;
            }
        }
    }
}
