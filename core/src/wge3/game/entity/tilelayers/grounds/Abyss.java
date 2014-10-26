package wge3.game.entity.tilelayers.grounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.EnumSet;
import static wge3.game.engine.constants.TilePropertyFlag.DRAINS_HP;
import wge3.game.entity.tilelayers.Ground;

public final class Abyss extends Ground {

    public Abyss() {
        propertyFlags = EnumSet.of(DRAINS_HP);
        HPDrainAmount = 1000;
    }

    @Override
    public void draw(Batch batch) {
    }

    @Override
    public void setPosition(int x, int y) {
    }

    @Override
    public void setLighting(Color color) {
    }
}
