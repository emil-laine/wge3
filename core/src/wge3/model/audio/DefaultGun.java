/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wge3.model.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Teemu
 */
public class DefaultGun implements MySound {

    private Sound sound;

    public DefaultGun() {
        try {
            sound = Gdx.audio.newSound(Gdx.files.internal("sounds/defaultGun2-16.wav"));
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    @Override
    public void play() {
        sound.play();
    }
}
