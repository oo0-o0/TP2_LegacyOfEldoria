package game.zelda.entity;

import com.badlogic.gdx.math.Vector2;

public class Bat extends Enemy 
{
    public Bat() 
    {
        super(60, 4, new Vector2(35, 60), "assets/Bat/BatIdleMoving/BatIdleMoving1.png");
    }
}