package game.zelda.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class CrystalElemental extends Enemy 
{
    public CrystalElemental(Vector2 position, Animation<TextureRegion> animation) 
    {
        super(80, 6, position, animation);
    }
}
