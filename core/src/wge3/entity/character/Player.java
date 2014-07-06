package wge3.entity.character;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.world.Tile;

public final class Player extends Creature {
    
    private OrthographicCamera camera;
    
    public Player() {
        sprite = new Sprite(texture, 4*Tile.size, 3*Tile.size, Tile.size, Tile.size);
        updateSpriteRotation();
        
        team = 0;
        size = 14;
        maxHP = 100;
        HP = maxHP;
        strength = 10;
        defense = 5;
        
        picksUpItems = true;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
    
    @Override
    public void move(float dx, float dy) {
        float oldX = getX();
        float oldY = getY();
        super.move(dx, dy);
        camera.translate(getX() - oldX, getY() - oldY);
    }
}
