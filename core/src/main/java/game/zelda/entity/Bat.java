package game.zelda.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bat extends Enemy 
{
    public Bat(Vector2 position, Animation<TextureRegion> animation) 
    {
        super(50, 4, position, animation);
    }
}