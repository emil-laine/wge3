package wge3.entity.object;

import wge3.world.MapObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import wge3.world.Area;

public class LightSource extends MapObject {
    
    private Area area;
    
    private Color color;
    private int range;

    public LightSource() {
        this(5);
    }
    
    public LightSource(int range) {
        texture = new Texture(Gdx.files.internal("graphics/lamppost.png"));
        sprite = new Sprite(texture);
        color = new Color(1f, 1f, 0f, 1f);
        blocksVision = false;
        this.range = range;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Area getArea() {
        return area;
    }

    public Color getColor() {
        return color;
    }

    public int getRange() {
        return range;
    }
}
