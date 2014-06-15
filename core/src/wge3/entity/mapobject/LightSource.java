package wge3.entity.mapobject;

import wge3.entity.terrainelement.MapObject;
import com.badlogic.gdx.graphics.Color;
import wge3.world.Area;

public class LightSource extends MapObject {
    
    private Area area;
    
    private Color color;
    private int range;

    public LightSource() {
        this(5);
    }
    
    public LightSource(int range) {
        
        this.range = range;
        
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
