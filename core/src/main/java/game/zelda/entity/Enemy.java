package game.zelda.entity;

import com.badlogic.gdx.math.Vector2;

import game.zelda.player.Player;

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

    public void attackPlayer(Player player, float deltaTime) 
    {
        if (Vector2.dst(position.x, position.y, player.getPosition().x, player.getPosition().y) <= 90) 
        { 
            player.currentHealth -= damagePoints * deltaTime;
            System.out.println("Inimigo atacou! Vida do jogador: " + player.currentHealth);
        }
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