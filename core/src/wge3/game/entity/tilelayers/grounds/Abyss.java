package wge3.game.entity.tilelayers.grounds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import wge3.game.entity.tilelayers.Ground;

public final class Abyss extends Ground {

    public Abyss() {
        drainsHP = true;
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
