package game.zelda.entity;

import com.badlogic.gdx.math.Vector2;

public class EnemyFactory 
{
	private EnemyFactory() {}
		
	public static Enemy enemyCreation(int enemyType) 
	{
		if(enemyType == 1) 
		{
			return new Witch();
		}
		
		else if(enemyType == 2) 
		{
			return new Enemy2();
		}
		
		else if(enemyType == 3) 
		{
			return new Enemy3();
		}
		
		else if(enemyType == 4) 
		{
			return new Enemy4();
		}
		
		else if(enemyType == 5) 
		{
			return new Enemy5();
		}
		
		else 
		{
			return new Witch();
		}
	}
}

class Witch extends Enemy
{
	public Witch() 
	{
        this.healthPoints = 2;
        this.damagePoints = 1;
        this.position = new Vector2(35,60);
        this.imgPath = "";
    }
}

class Enemy2 extends Enemy
{
	public Enemy2() 
	{
        this.healthPoints = 3;
        this.damagePoints = 2;
        this.position = new Vector2(79,30);
        this.imgPath = "";
    }
}

class Enemy3 extends Enemy
{
	public Enemy3() 
	{
        this.healthPoints = 4;
        this.damagePoints = 2;
        this.position = new Vector2(79,30);
        this.imgPath = "";
    }
}

class Enemy4 extends Enemy
{
	public Enemy4() 
	{
        this.healthPoints = 6;
        this.damagePoints = 3;
        this.position = new Vector2(79,30);
        this.imgPath = "";
    }
}

class Enemy5 extends Enemy
{
	public Enemy5() 
	{
        this.healthPoints = 8;
        this.damagePoints = 4;
        this.position = new Vector2(79,30);
        this.imgPath = "";
    }
}