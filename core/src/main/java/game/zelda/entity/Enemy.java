package game.zelda.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import game.zelda.player.Player;

public abstract class Enemy 
{
    protected int healthPoints;
    protected int damagePoints;
    protected boolean alive;
    protected Vector2 position;
    private Animation<TextureRegion> animation;
    private float stateTime;

    public Enemy(int healthPoints, int damagePoints, Vector2 position, Animation<TextureRegion> animation) 
	{
        this.healthPoints = healthPoints;
        this.damagePoints = damagePoints;
        this.position = position;
        this.alive = true;

        this.animation = animation;
        this.stateTime = 0;
    }

    public void attackPlayer(Player player, float deltaTime) 
    {
        if (Vector2.dst(position.x, position.y, player.getPosition().x, player.getPosition().y) <= 75) 
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

    public void updateAnimation(float deltaTime) 
    {
        stateTime += deltaTime;
    }

    public void render(SpriteBatch batch) 
    {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        batch.draw(currentFrame, position.x, position.y);
    }

    public void takeDamage(int damage) 
    {
        healthPoints -= damage;

        if (healthPoints <= 0) 
        {
            die();
        }
    }

    public void die() 
    {
        System.out.println("Inimigo morreu!");
        alive = false;
    }

    public boolean isAlive()
    {
        if (alive) 
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}