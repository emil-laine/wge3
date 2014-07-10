package wge3.game.entity.creatures.npcs;

import static com.badlogic.gdx.Gdx.files;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.badlogic.gdx.math.MathUtils.random;
import wge3.game.entity.creatures.NonPlayer;
import wge3.game.entity.Tile;

public final class Zombie extends NonPlayer {

    public Zombie() {
        sprite = new Sprite(new Texture(files.internal("graphics/creatures.png")), random(9)*Tile.size, 0, Tile.size, Tile.size);
        updateSpriteRotation();
        
        name = "zombie";
        HP.setMaximum(random(30, 60));
        strength = random(5, 12);
        defense = random(0, 7);
        defaultSpeed = random(20, 35);
        currentSpeed = defaultSpeed;
    }
    
    @Override
    public void dealDamage(int amount) {
        super.dealDamage(amount);
    }
}
