/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package wge3.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

public final class Audio {
    
    private static Music currentMusic;
    private static Map<String, Sound> sounds = new HashMap();
    private static String audioDirectory = "sounds/";
    
    public static void playMusic(String filePath) {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(audioDirectory + filePath));
        currentMusic.setVolume(0.2f);
        currentMusic.play();
    }
    
    public static void toggleMusic() {
        if (currentMusic.isPlaying()) {
            currentMusic.pause();
        } else {
            currentMusic.play();
        }
    }
    
    public static void playSound(String filePath) {
        Sound sound = sounds.get(filePath);
        
        if (sound == null) {
            sound = Gdx.audio.newSound(Gdx.files.internal(audioDirectory + filePath));
            sounds.put(filePath, sound);
        }
        
        sound.play();
    }
    
    public static void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
            currentMusic = null;
        }
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
    }
    
    private Audio() {}
}
