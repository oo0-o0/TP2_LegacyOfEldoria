package game.zelda.player;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import game.zelda.AssetsManager;
import game.zelda.entity.Enemy;
import game.zelda.inventory.InventoryGame;
import game.zelda.inventory.Item;
import game.zelda.observer.Observer;

public class Player implements Observer
{
    private Sound itemCollectSound;
    private InventoryGame inventory;
    private Vector2 position;
    private Vector2 velocity;
    private boolean isJumping;
    public PlayerAnimationManager animationManager;
    public int maxHealth = 100; 
    public int currentHealth = 100; 
    public int damage = 10; 
    private List<TiledMapTileLayer> collisionLayers;
    private String blockedKey = "blocked";
    private Vector2 direction;
    
    public Player(float startX, float startY, List<TiledMapTileLayer> collisionLayers, InventoryGame inventory)  
    {
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(0, 0);
        this.animationManager = new PlayerAnimationManager();
        this.isJumping = false;
        this.inventory = inventory;
        inventory.registerObserver(this); 
        this.collisionLayers = collisionLayers;
        this.direction = new Vector2(0, 0);

        AssetsManager assetsManager = AssetsManager.getInstance();
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.IDLE, assetsManager.getAnimation("idle"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.JUMP, assetsManager.getAnimation("jump"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.ATTACK_NORMAL, assetsManager.getAnimation("attackNormal"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.TAKE_HIT, assetsManager.getAnimation("takeHit"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.DEATH, assetsManager.getAnimation("death"));

        itemCollectSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/collectSound.mp3"));
    }

    public void setPosition(Vector2 position) 
    {
		this.position = position;
	}
    
    public Vector2 getPosition() 
    {
    	return position;
	}
	
	public float getPositionX() 
    {
        return position.x;
    }
	
	public float getPositionY() 
    {
        return position.y;
    }

    public void move(float dx, float dy) 
    {
        direction.set(dx, dy);
        position.add(dx, dy);
        if (!isJumping) 
        {
            animationManager.setState(PlayerAnimationManager.PlayerState.IDLE);           
        }
    }

    public void stop() 
    {
        direction.set(0, 0);
        velocity.set(0, 0);
        if (!isJumping) 
        {
            animationManager.setState(PlayerAnimationManager.PlayerState.IDLE);
        }
    }
    
    public void attack(List<Enemy> enemies) 
    {
        if (animationManager.getCurrentState() == PlayerAnimationManager.PlayerState.ATTACK_NORMAL) 
        {
            return; 
        }

        if (animationManager.getCurrentState() == PlayerAnimationManager.PlayerState.DEATH) 
            return;

        if (animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.ATTACK_NORMAL) 
        {
            animationManager.setState(PlayerAnimationManager.PlayerState.ATTACK_NORMAL);

            for (Enemy enemy : enemies) 
            {
                System.out.println("Distância para inimigo: " + position.dst(enemy.getPosition()));
                if (isEnemyInRange(enemy)) 
                {
                    enemy.takeDamage(damage);
                    //System.err.println("inimigo sofreu dano" + damage);
                }
            }
        }
    }

    private boolean isEnemyInRange(Enemy enemy) 
    {
        float attackRange = 150f; 
        return position.dst(enemy.getPosition()) <= attackRange;
    }

    public void jump() 
    {
        if (animationManager.getCurrentState() == PlayerAnimationManager.PlayerState.DEATH) 
            return;

        animationManager.setState(PlayerAnimationManager.PlayerState.JUMP);
    }

    public void collectItem() 
    {
        if (Gdx.input.isKeyPressed(Input.Keys.C)) 
        {
            inventory.collectItemNearPlayer(position.x, position.y, 120f); 
        }
    }

    @Override
    public void update(Object event) 
    {
        if (event instanceof Item) 
        {
            Item item = (Item) event;
            System.out.println("Item coletado: " + item.getName());
            if (itemCollectSound != null) 
            {
                itemCollectSound.play();
            }
        }
    }

   public void update(float deltaTime) 
   {
        float lastX = getPositionX(), lastY = getPositionY();
        boolean collidedX = false, collidedY = false;
        collectItem();
        
        if (isJumping)
        {
            velocity.y -= 980 * deltaTime;
            if (position.y <= 0) 
            {
                position.y = 0;
                isJumping = false;

                if (animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.DEATH) 
                {
                    animationManager.setState(PlayerAnimationManager.PlayerState.IDLE);
                }
            }
        }        

        if (animationManager.isAnimationFinished()) 
        {
            if (animationManager.getCurrentState() == PlayerAnimationManager.PlayerState.ATTACK_NORMAL || 
                animationManager.getCurrentState() == PlayerAnimationManager.PlayerState.JUMP || 
                animationManager.getCurrentState() == PlayerAnimationManager.PlayerState.TAKE_HIT) 
            {
                animationManager.setState(PlayerAnimationManager.PlayerState.IDLE);
            }
        }
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        animationManager.update(deltaTime);

        /*	seção de codigo (parte da colisão) fortemente inspirada 
         * em duas fontes, sendo elas:
         * 
         * 1 - https://github.com/MrBenC88/Simple-Tile-Map-ProgramLibGDX/tree/master
         * 2 - https://www.youtube.com/watch?v=DOpqkaX9844&list=PLXY8okVWvwZ0qmqSBhOtqYRjzWtUCWylb&index=4
         * 
         */
        
        //colisão para a esquerda
        if (direction.x  < 0) 
        {
        	collidedX = collidesLeft();
        } 
        
        //colisão para a direita
        else if (direction.x > 0) 
        {
        	collidedX = collidesRight();
        }
        
        //Reação a colisão: reposiciona o personagem apos colisão com o eixo x
        if(collidedX) 
        {
        	Vector2 newPositionX;
        	newPositionX = new Vector2(lastX - 1, position.y);       	
        	setPosition(newPositionX); 	
        	velocity.x = 0;
        }
        
        //colisão para baixo
        if (direction.y < 0) 
        {
        	collidedY = collidesBottom();
        } 
        
        //colisão para cima
        else if (direction.y > 0) 
        {
        	collidedY = collidesTop();
        }
        
        //Reação a colisão: reposiciona o personagem apos colisão com o eixo y
        if(collidedY) 
        {
        	Vector2 newPositionY;
        	newPositionY = new Vector2(position.x, lastY - 1);       	
        	setPosition(newPositionY);
        	velocity.y = 0;
        } 
        animationManager.update(deltaTime); 
    }

    private boolean isCellBlocked(float x, float y) 
    {
        for (TiledMapTileLayer layer : collisionLayers) 
        {
            TiledMapTileLayer.Cell cell = layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
            if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked")) 
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean collidesRight() 
    {
    	for (TiledMapTileLayer layer : collisionLayers) 
        {
            for (float step = 0; step < 128; step += layer.getTileHeight() / 2) 
            {
                if (isCellBlocked(getPositionX() + 228, getPositionY() + step)) 
                {
                	return true;
                }
            }
        }
        return false;
    }

    public boolean collidesLeft() 
    {
    	for (TiledMapTileLayer layer : collisionLayers) 
        {
            for (float step = 0; step < 128; step += layer.getTileHeight() / 2) 
            {
                if (isCellBlocked(getPositionX(), getPositionY() + step)) 
                {
                    return true; 
                }
            }
        }
        return false;
    }

    public boolean collidesTop() 
    {
    	for (TiledMapTileLayer layer : collisionLayers) 
        {
            for (float step = 0; step < 228; step += layer.getTileWidth() / 2) 
            {
                if (isCellBlocked(getPositionX() + step, getPositionY() + 128)) 
                {
                	return true;
                }
            }
        }
        return false;
    }

    public boolean collidesBottom() 
    {
    	for (TiledMapTileLayer layer : collisionLayers) 
        {
            for (float step = 0; step < 228; step += layer.getTileWidth() / 2) 
            {
                if (isCellBlocked(getPositionX() + step, getPositionY())) 
                {
                	return true;
                }
            }
        }
        return false;
    }
      
    public void render(Batch batch) 
    {
        batch.draw(animationManager.getCurrentFrame(), position.x, position.y);
    }

    public void dispose() 
    {
        itemCollectSound.dispose();
    }

    public Sound getitemCollectSound()
    {
        return itemCollectSound;
    }
}
