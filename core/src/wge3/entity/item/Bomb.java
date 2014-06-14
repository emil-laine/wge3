package wge3.entity.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import wge3.interfaces.Explosive;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import wge3.world.Item;

public class Bomb extends Item implements Explosive {

    private Timer timer;
    private Task task;
    private int time; // in seconds
    
    public Bomb() {
        texture = new Texture(Gdx.files.internal("graphics/bomb.png"));
        sprite = new Sprite(texture);
        name = "bomb";
        time = 3;
        
        timer = new Timer();
        task = new Task() {

            @Override
            public void run() {
                explode();
            }
        };
    }
    
    public void startTimer() {
        timer.scheduleTask(task, time);
    }

    @Override
    public void explode() {
        System.out.println("*explosion*");
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }

    @Override
    public void use() {
        
    }
}
