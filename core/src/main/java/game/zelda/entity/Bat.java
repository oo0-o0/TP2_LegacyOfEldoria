package game.zelda.entity;

import com.badlogic.gdx.math.Vector2;

public class Bat extends Enemy 
{
    public Bat() 
    {
        super(2, 1, new Vector2(35, 60), "witch.png");
    }
}