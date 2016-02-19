/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wge3.model.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 *
 * @author Teemu
 */
public class SoundTrack implements MySound {

    private Music music;

    public SoundTrack() {
        try {
            music = Gdx.audio.newMusic(Gdx.files.internal("sounds/soundtrack.mp3"));
        } catch (Exception ex) {
            System.out.println("Error with playing soundtrack.");
            ex.printStackTrace();
        }
    }

    @Override
    public void play() {
        music.setVolume(0.2f);
        music.play();
    }
}
