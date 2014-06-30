/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wge3.entity.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author root
 */
public abstract class MovingBullet extends Bullet {
    private float speed;
    private float angle;
    
    
    
    public MovingBullet() {}

    public float getSpeed() {
        return speed;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        sprite.setRotation(angle*MathUtils.radiansToDegrees);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    
    
}
