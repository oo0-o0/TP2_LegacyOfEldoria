package game.zelda.entity;

import com.badlogic.gdx.math.Vector2;

public abstract class Enemy 
{
    protected int healthPoints;
    protected int damagePoints;
    protected Vector2 position;
    protected String imgPath;

    public Enemy(int healthPoints, int damagePoints, Vector2 position, String imgPath) 
	{
        this.healthPoints = healthPoints;
        this.damagePoints = damagePoints;
        this.position = position;
        this.imgPath = imgPath;
    }

    public int getHealthPoints() 
	{
        return healthPoints;
    }

    public int getDamagePoints() 
	{
        return damagePoints;
    }

    public Vector2 getPosition() 
	{
        return position;
    }

    public String getImgPath() 
	{
        return imgPath;
    }
}