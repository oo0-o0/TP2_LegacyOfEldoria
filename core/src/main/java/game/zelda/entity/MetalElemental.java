package game.zelda.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MetalElemental extends Enemy 
{
    public MetalElemental(Vector2 position, Animation<TextureRegion> animation) 
    {
        super(100, 8, position, animation);
    }
}
