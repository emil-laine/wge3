package wge3.entity.object;

import wge3.interfaces.Explosive;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class TimedBomb extends Item implements Explosive {

    private Timer timer;
    private Task task;
    private int time; // in seconds
    
    public TimedBomb() {
        name = "timed bomb";
        time = 3;
        timer = new Timer();
        task = new Task() {

            @Override
            public void run() {
                explode();
            }
        };
        timer.scheduleTask(task, time);
    }
    
   public void startTimer() {
       timer.start();
   }

    @Override
    public void explode() {
        System.out.println("*explosion*");
        tile.getArea().getTileAt(tile.getX(), tile.getY()).setObject(null);
    }
    
    @Override
    public void draw(Batch batch) {
        batch.draw(sprite, tile.getX(), tile.getY());
    }
    
    @Override
    public boolean needsToBeDrawn() {
        return needsToBeDrawn;
    }

    @Override
    public void use() {
        
    }
}
