package wge3.entity.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import wge3.world.Area;
import wge3.world.Tile;

public class TimedBomb extends Item implements Explosive {

    private Timer timer;
    private Task task;
    private int time; // in seconds

    public TimedBomb() {
        color = new Color(0x222222ff);
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
        area.getTileAt(x, y).setObject(null);
    }
    
    @Override
    public void draw(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.circle(x + Tile.size/2, y + Tile.size/2, Tile.size/4);
        sr.end();
    }

    @Override
    public void use(Area area, float x, float y) {
        this.setArea(area);
        this.setX(area.getTileAt(x, y).getX());
        this.setY(area.getTileAt(x, y).getY());
        area.getTileAt(x, y).setObject(this);
        this.startTimer();
    }
}
