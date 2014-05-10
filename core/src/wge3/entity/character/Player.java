package wge3.entity.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import wge3.entity.object.TimedBomb;
import wge3.world.Area;

public class Player extends Character {
    
    private boolean goingForward;
    private boolean goingBackward;
    private boolean turningLeft;
    private boolean turningRight;
    
    public Player(Area area) {
        super(area);
        color = new Color(0x88ffff88);
        goingForward = false;
        goingBackward = false;
        turningLeft = false;
        turningRight = false;
    }
    
    public void goForward(boolean truth) {
        goingForward = truth;
    }
    
    public void goBackward(boolean truth) {
        goingBackward = truth;
    }
    
    public void turnLeft(boolean truth) {
        turningLeft = truth;
    }
    
    public void turnRight(boolean truth) {
        turningRight = truth;
    }

    public void updatePosition(float delta) {
        if (goingForward) {
            float dx = MathUtils.cos(direction) * currentSpeed * delta;
            float dy = MathUtils.sin(direction) * currentSpeed * delta;
            move(dx, dy);
        } else if (goingBackward) {
            float dx = -(MathUtils.cos(direction) * currentSpeed/1.5f * delta);
            float dy = -(MathUtils.sin(direction) * currentSpeed/1.5f * delta);
            move(dx, dy);
        }
        
        if (turningLeft) {
            turnLeft(delta);
        } else if (turningRight) {
            turnRight(delta);
        }
    }
}
