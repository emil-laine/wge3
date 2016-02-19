/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wge3.model.audio;

/**
 * If playing wav file gives error 'WAV files must have 16 bits per sample: 8'
 * you must use audacity to convert the wav file to 16-bit
 */
public interface MySound {
    
    public void play();
}
