package org.academiadecodigo.argicultores;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sounds {
    private Clip clip = null;
    private String path;
    public Sounds(String path){
        this.path = path;
    }

    public void play() {
        try {
            URL wavFile = getClass().getClassLoader().getResource(path);
            clip = null;
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            try {
                clip.open(AudioSystem.getAudioInputStream(wavFile));
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
            clip.start();
        }catch (Exception ex) {}
    }
    public void stop(){
        clip.stop();
    }
}