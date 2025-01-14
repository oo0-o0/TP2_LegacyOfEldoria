package game.zelda.entity;

public class EnemyFactory 
{
	private EnemyFactory() {}
		
	public static Enemy enemyCreation(int enemyType) 
	{
		if(enemyType == 1) 
		{
			return new Bat();
		}
		
		else if(enemyType == 2) 
		{
			return new CrystalElemental();
		}
		
		else if(enemyType == 3) 
		{
			return new MetalElemental();
		}
		
		else 
		{
			return new Bat();
		}
	}
}