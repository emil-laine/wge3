package wge3.entity.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

public final class Player extends Creature {
    
    private boolean goingForward;
    private boolean goingBackward;
    private boolean turningLeft;
    private boolean turningRight;
    
    public Player() {
        super();
        texture = new Texture(Gdx.files.internal("graphics/player.png"));
        sprite = new Sprite(texture);
        updateSpriteRotation();
        
        goingForward = false;
        goingBackward = false;
        turningLeft = false;
        turningRight = false;
        
        defense = 10;
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
