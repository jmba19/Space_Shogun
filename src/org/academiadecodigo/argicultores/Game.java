package org.academiadecodigo.argicultores;

import org.academiadecodigo.simplegraphics.keyboard.Keyboard;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEventType;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.simplegraphics.pictures.Picture;

public class Game implements KeyboardHandler {
    public final static int PADDING = 10;

    //menu stuff
    private Picture[] platform = new Picture[3];
    private Picture map = new Picture(PADDING,PADDING,"mapback.jpg");
    private Picture startButton = new Picture(900/2-230/2, 480, "start_button.png");
    private Picture quitButton = new Picture(900/2-150/2, 620, "quit_button.png");
    private Picture startMenu = new Picture(10, 10, "menu_background.jpeg");
    private Picture pointer = new Picture(170,520,"pointer_sword.png");

    //map dimensions
    private int width;
    private int height;
    private boolean gameStart = false;
    private boolean respawn = false;
    private int rounds = 1;
    private int enemiesLeft = 1;
    private boolean jumping = false;
    private boolean attacking = false;

    public Enemy[] enemies;
    private Player player;

    private Sounds attackSound = new Sounds("attack.wav");
    private Sounds zagen = new Sounds("zagen1.wav");
    private Sounds movePlayer = new Sounds("playerMove.wav");
    private Sounds menuSound = new Sounds("menu.wav");


    public Game(int width, int height, int padding) {
        this.width = width;
        this.height = height;
    }

    public void init() {
        keyboardInit();
        menuSound.play();
        while (!gameStart) {
            startMenu.draw();
            startButton.draw();
            quitButton.draw();
            pointer.draw();
        }
        menuSound.stop();
        startMenu.delete();
        startButton.delete();
        quitButton.delete();
        pointer.delete();

        map.draw();
        drawPlatform();
        drawPlayer();

        enemies = drawEnemies(1);

        while (true) {

            if (getPlayerAttack()) {
                playerAttack();
            } else {
                for (Enemy e : enemies) {
                    e.move(getRounds());
                }
            }

            if (isRespawn()) {
                setRespawn(false);
                enemies = null;
                enemies = drawEnemies(getRounds());
            }

            testCollision();

            try {
                Thread.sleep(10);
            } catch (Exception ex) {

            }
        }
    }
    public void keyboardInit() {
        Keyboard kb = new Keyboard(this);

        KeyboardEvent arrowUp = new KeyboardEvent();
        arrowUp.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        arrowUp.setKey(KeyboardEvent.KEY_UP);
        kb.addEventListener(arrowUp);

        KeyboardEvent arrowUpRelease = new KeyboardEvent();
        arrowUpRelease.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
        arrowUpRelease.setKey(KeyboardEvent.KEY_UP);
        kb.addEventListener(arrowUpRelease);

        KeyboardEvent arrowLeft = new KeyboardEvent();
        arrowLeft.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        arrowLeft.setKey(KeyboardEvent.KEY_LEFT);
        kb.addEventListener(arrowLeft);

        KeyboardEvent arrowRight = new KeyboardEvent();
        arrowRight.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        arrowRight.setKey(KeyboardEvent.KEY_RIGHT);
        kb.addEventListener(arrowRight);

        KeyboardEvent space = new KeyboardEvent();
        space.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        space.setKey(KeyboardEvent.KEY_SPACE);
        kb.addEventListener(space);

        KeyboardEvent spaceRelease = new KeyboardEvent();
        spaceRelease.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
        spaceRelease.setKey(KeyboardEvent.KEY_SPACE);
        kb.addEventListener(spaceRelease);


        KeyboardEvent arrowDown = new KeyboardEvent();
        arrowDown.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);
        arrowDown.setKey(KeyboardEvent.KEY_DOWN);
        kb.addEventListener(arrowDown);

        KeyboardEvent arrowDownRelease = new KeyboardEvent();
        arrowDownRelease.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);
        arrowDownRelease.setKey(KeyboardEvent.KEY_LEFT);
        kb.addEventListener(arrowDownRelease);
    }
    public Enemy[] drawEnemies(int numberOfEnemies) {
        int[] spawnX = {540,360,180,540,360,180,540,360,180};
        int[] spawnY = {11,840,11,71,71,780,131,720,131};
        enemies = new Enemy[rounds];
        for (int i = 0; i < enemies.length; i++) {
            int r = (int)(Math.random()*9);
            enemies[i] = new Enemy(spawnY[r],spawnX[r]);
        }
        return enemies;
    }
    public void drawPlayer() {
        player = new Player(width, height, enemies);
        player.setPlatform(platform);
        player.setEnemies(enemies);
    }
    public void drawPlatform() {
        platform[0] = new Picture(10,630,"platform.jpg");
        platform[0].draw();
        platform[1] = new Picture(470,450,"platform.jpg");
        platform[1].draw();
        platform[2] = new Picture(10,270,"platform.jpg");
        platform[2].draw();
    }
    public boolean testCollision() {
        for (Enemy e : enemies) {
            if (e.getY() == player.getY()) {
                if ((e.getX() + e.getWidth() >= player.getX()+30) && (e.getX()+30 <= (player.getX() + player.getWidth()))) {
                    e.delete();
                    e.translate(0, -1000);
                    if (!player.isItsOver()) {
                        player.setHealthpoints();
                        enemiesLeft--;

                    }

                }
                /*if (e.getX() == player.getX()) {
                    e.delete();
                    e.translate(0, -1000);
                    if (!player.isItsOver()) {
                        player.setHealthpoints();
                        enemiesLeft--;
                    }
                }*/
            }
        }
        if (enemiesLeft == 0) {
            respawn = true;
            rounds++;
            enemiesLeft = rounds;
        }

        return player.isItsOver();
    }
    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }
    public void playerAttack() {
        for (Enemy e : enemies) {
            if (player.isAttacked()) {
                if (e.getX() <= (player.getX() + 140) && (e.getX() + e.getWidth()) >= player.getX() - 70) {
                    if (e.getY() == player.getY()) {
                        e.translate(0,-1000);
                        enemiesLeft--;
                        if (enemiesLeft == 0) {
                            rounds++;
                            enemiesLeft = rounds;
                            respawn = true;
                        }
                    }
                }
            }
        }
        player.setAttacked(false);
    }
    public boolean isRespawn() {
        return respawn;
    }
    public int getRounds() {
        return rounds;
    }
    public boolean getPlayerAttack() {
        return player.isAttacked();
    }

    /**
     *
     * MENUS N STUFF
     *
     * */

    public void movePointerUp(){

        if((pointer.getY() == 645)){
            pointer.translate(-30,-125);
        }
    }
    public void movePointerDown(){

        if ((pointer.getY() == 520)){
            pointer.translate(30,+125);
        }
    }

    /**
     *
     * KEYBOARD KEYS N ACTIONS
     *
     * */

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_RIGHT:
                if (gameStart) {
                    if (!jumping) {
                        player.moveRight();
                        movePlayer.play();
                    }
                }
                break;
            case KeyboardEvent.KEY_LEFT:
                if (gameStart) {
                    if (!jumping) {
                        player.moveLeft();
                        movePlayer.play();
                    }
                }
                break;
            case KeyboardEvent.KEY_UP:
                if (gameStart) {
                    if (!jumping) {
                        jumping = true;
                        player.jump();
                        zagen.play();
                    }
                } else {
                    movePointerUp();
                }
                break;
            case KeyboardEvent.KEY_SPACE:
                if (!gameStart) {
                    if (pointer.getY() == 520){
                        gameStart = true;
                    } else {
                        System.exit(1);
                    }
                } else {
                    if (!attacking) {
                        if(!player.isItsOver()){
                            attacking = true;
                            player.attack();
                            attackSound.play();
                        }
                    }
                }

                break;
            case KeyboardEvent.KEY_DOWN:
                if (!gameStart) {
                    movePointerDown();
                }
                break;
        }
    }
    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_UP:
                jumping = false;
                break;
            case KeyboardEvent.KEY_SPACE:
                attacking = false;
                break;
            case KeyboardEvent.KEY_DOWN:
                jumping = false;
                break;

        }
    }
}
