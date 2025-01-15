package game.zelda.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import game.zelda.AssetsManager;

public class EnemyFactory 
{
	private EnemyFactory() {}
		
	public static Enemy enemyCreation(int type) 
    {
        AssetsManager assetsManager = AssetsManager.getInstance();
        Animation<TextureRegion> animation;

        switch (type) 
        {
            case 1:
                animation = assetsManager.getAnimation("bat");
                return new Bat(new Vector2(100, 100), animation);
            case 2:
                animation = assetsManager.getAnimation("crystalElemental");
                return new CrystalElemental(new Vector2(200, 200), animation);
            case 3:
                animation = assetsManager.getAnimation("metalElemental");
                return new MetalElemental(new Vector2(300, 300), animation);
            default:
                throw new IllegalArgumentException("Tipo de inimigo desconhecido.");
        }
    }
}