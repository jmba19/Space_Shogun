package org.academiadecodigo.argicultores;

import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.concurrent.TimeUnit;

public class Player extends Picture {
    public boolean isLookingToRight() {
        return lookingToRight;
    }

    private Picture menuGameOver = new Picture(10,10,"gameover.png");
    private boolean itsOver;
    private boolean jumping;

    private Sounds hitted = new Sounds("yeet.wav");
    private Sounds gameOverSound = new Sounds("endgame.wav");
    private Sounds gameSound = new Sounds("mulan.wav");


    /**
    **TESTAR MENU GAME OVER ACIMA
     */

    private Picture[] platform;
    private Picture[] healthpoints = new Picture[5];
    private Enemy[] enemies;

    private int hp;
    private int moveX;
    private double mapWidth;
    private double mapHeight;

    private boolean attacked;
    private boolean lookingToRight;

    public Player(double x, double y, Enemy[] enemies) {
        super(x/2, y-80, "player.png");
        draw();

        mapWidth = x;
        mapHeight = y;
        moveX = 0;
        hp = 5;
        lookingToRight = true;

        healthpoints[0] = new Picture(840,20,"health_heart.png");
        healthpoints[0].draw();
        healthpoints[1] = new Picture(780,20,"health_heart.png");
        healthpoints[1].draw();
        healthpoints[2] = new Picture(720,20,"health_heart.png");
        healthpoints[2].draw();
        healthpoints[3] = new Picture(660,20,"health_heart.png");
        healthpoints[3].draw();
        healthpoints[4] = new Picture(600,20,"health_heart.png");
        healthpoints[4].draw();

        gameSound.play();
    }

    public void setHealthpoints() {
        if (hp == 5) {
            healthpoints[4].delete();
            --hp;
        } else if (hp == 4) {
            healthpoints[3].delete();
            --hp;
        } else if (hp == 3) {
            healthpoints[2].delete();
            --hp;
        } else if (hp == 2) {
            healthpoints[1].delete();
            --hp;
        } else {
            healthpoints[0].delete();
            itsOver = true;
            gameSound.stop();
            menuGameOver.draw();
            gameOverSound.play();
            delete();
            try {
                Thread.sleep(4000);
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deleteEne();
        }
    }
    /**
     *
     * MAP + ENEMIES -> PLATFORM HERE
     *
     * **/

    public void setPlatform(Picture[] platform) {
        this.platform = platform;
    }
    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
    }
    public void deleteEne(){
        for (Enemy e : enemies) {
            e.delete();
        }
    }
    /**
     *
     * PLAYER MOVEMENT AND ACTIONS
     *
     * **/

    public void moveRight() {
        lookingToRight = true;
        load("player.png");
        if (getX() + getWidth() < mapWidth) {
            translate(20,0);
            if (getY() == 180 && getX() > 420) {
                translate(30,180);
            }
            if (getY() == 540 && getX() > 420) {
                translate(30,180);
            }
        }
    }
    public void moveLeft() {
        lookingToRight = false;
        load("playerleft.png");
        if (getX() - 20 > 10) {
            translate(-20,0);
            if (getY() == 360 && getX() < 440) {
                translate(-30,180);
            }
        }
    }


    public void jump(){
        if (lookingToRight) {
            moveX = 100;
        } else {
            moveX = -100;
        }

        if (!attacked) {
            Runnable task = () -> {
                try {

                    /**
                     *
                     * JUMP CONTROL ON 3RD PLATFORM
                     *
                     * **/

                    if ((getY() + getHeight()) == platform[2].getY()) {
                        if (!((moveX + getX()) < 10)) {//VALOR ORIGINAL 10
                            translate(moveX/2,-30);
                            TimeUnit.MILLISECONDS.sleep(50);
                            translate(moveX/2,-90);
                            translate(0,120);
                            if (getX() >= (platform[2].getX() + platform[2].getWidth())) {
                                translate(0,180);
                            }
                        }
                        translate(0,-30);
                        TimeUnit.MILLISECONDS.sleep(50); //original 50
                        translate(0,-90);
                        TimeUnit.MILLISECONDS.sleep(50); //origanl 50
                        translate(0,120);
                    }

                    /**
                     *
                     * TEST HIT 2ND PLATFORM (RIGHT)
                     *
                     * **/
                    if (!((moveX + getX()) > 880)) {//VALOR ORIGINAL 10
                        if ((getY() + getHeight()) == platform[1].getY()) { // player is in the 2nd platform
                            if ((getY() - 180 + getHeight()) == platform[2].getY()) { // TEST CHECK IF PLAYERS FEET TOUCH GROUND PLATFORM 2
                                if (getX() >= 780 && lookingToRight) {
                                    translate(0, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                    translate(0, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                } else {
                                    translate(moveX / 2, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                    translate(moveX / 2, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                }
                                if (!(getX() > 10 && getX() <= (platform[2].getX() - 45 + platform[2].getWidth()))) {
                                    translate(0, platform[1].getY() - getY() - getHeight());
                                }
                            }
                            if ((getY() == platform[1].getY()) && (getX() - getWidth() / 2) <= platform[1].getX()) {
                                translate(0, 180);
                            }
                        }
                    }

                    /**
                     *
                     * TEST HIT 1ST PLATFORM
                     *
                     * **/
                    if (!((moveX + getX()+20) < 10)) {//VALOR ORIGINAL 10
                        if ((getY() + getHeight()) == platform[0].getY()) { // player is in the 1st platform
                            if ((getY() - 180 + getHeight()) == platform[1].getY()) { // TEST CHECK IF PLAYERS FEET TOUCH GROUND PLATFORM 2
                                if (getX() <= 100 && !lookingToRight) {
                                    if (getX() > 50) {
                                        translate(-50, -90);
                                        TimeUnit.MILLISECONDS.sleep(50);
                                        translate(0, -90);
                                        TimeUnit.MILLISECONDS.sleep(50);
                                    } else {
                                        translate(0, -90);
                                        TimeUnit.MILLISECONDS.sleep(50);
                                        translate(0, -90);
                                        TimeUnit.MILLISECONDS.sleep(50);
                                    }
                                } else {
                                    translate(moveX / 2, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                    translate(moveX / 2, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                }
                                if (!(getX() < mapWidth - 10 && getX() >= platform[1].getX())) {
                                    translate(0, 180);
                                }
                            }
                        }
                    }

                    /**
                     *
                     * FLOOOR TEST
                     *
                     * **/
                    if (!((moveX + getX()) < 10 )) {//VALOR ORIGINAL 10
                        if (!((moveX + getX()) > 880)) {//VALOR ORIGINAL 10
                            if ((getY() - 90) == platform[0].getY()) {

                                if (getX() >= 800 && lookingToRight) {
                                    translate(0, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                    translate(0, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                } else {
                                    translate(moveX / 2, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                    translate(moveX / 2, -90);
                                    TimeUnit.MILLISECONDS.sleep(50);
                                }
                                if (!(getX() > 10 && getX() + getWidth() <= platform[0].getX() + platform[0].getWidth())) {
                                    translate(0, 180);
                                }
                            }

                            if (getX() > 440 && getY() > 720 && getY() < 640) {
                                translate(0, 180);
                            } else if (getX() > 440 && getY() < 180 && getY() > 100) {
                                translate(0, 180);
                            } else if (getX() < 470 && getY() > 360 && getY() < 400) {
                                translate(0, 180);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {

                }
            };
            Thread thread = new Thread(task);
            thread.start();
        }

    }
    public boolean isAttacked() {
        return attacked;
    }
    public void setAttacked(Boolean atk) {
        attacked = atk;
    }
    public boolean isItsOver() {
        return itsOver;
    }
    public void attack() {
        attacked = true;
        Runnable task = () -> {
            try {
                if(isLookingToRight()) {
                    if (getX() < 780) {
                        translate(31,0);
                        load("attack_right.png");
                        TimeUnit.MILLISECONDS.sleep(80);
                        load("player.png");
                        translate(-31,0);
                    }
                }else{
                    if (getX() > 70) {
                        translate(-31,0);
                        load("attack_left.png");
                        TimeUnit.MILLISECONDS.sleep(80);
                        load("playerleft.png");
                        translate(31,0);
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

}
