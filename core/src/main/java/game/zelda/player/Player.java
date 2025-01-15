package game.zelda.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import game.zelda.AssetsManager;
import game.zelda.inventory.InventoryGame;
import game.zelda.inventory.Item;
import game.zelda.observer.Observer;

public class Player implements Observer
{
    private TiledMapTileLayer collisionLayer;
    private Sound itemCollectSound;
    private InventoryGame inventory;
    private Vector2 position;
    private Vector2 velocity;
    private boolean isJumping;
    public PlayerAnimationManager animationManager;
    public int maxHealth = 100; 
    public int currentHealth = 100; 
    public int damage = 5; 
    
    public Player(float startX, float startY, TiledMapTileLayer collisionLayer, InventoryGame inventory)  
    {
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(0, 0);
        this.animationManager = new PlayerAnimationManager();
        this.isJumping = false;
        this.collisionLayer = collisionLayer;
        this.inventory = inventory;
        inventory.registerObserver(this); 

        AssetsManager assetsManager = AssetsManager.getInstance();
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.IDLE, assetsManager.getAnimation("idle"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.JUMP, assetsManager.getAnimation("jump"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.ATTACK_NORMAL, assetsManager.getAnimation("attackNormal"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.TAKE_HIT, assetsManager.getAnimation("takeHit"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.DEATH, assetsManager.getAnimation("death"));

        itemCollectSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/collectSound.mp3"));
    }

    public Vector2 getPosition() 
    {
        return position;
    }

    public void move(float dx, float dy) 
    {
        position.add(dx, dy);
        if (!isJumping) 
        {
            animationManager.setState(PlayerAnimationManager.PlayerState.IDLE);
        }
    }

    public void stop() 
    {
        velocity.set(0, 0);
        if (!isJumping) 
        {
            animationManager.setState(PlayerAnimationManager.PlayerState.IDLE);
        }
    }
    
    public void attack() 
    {
        if (animationManager.getCurrentState() == PlayerAnimationManager.PlayerState.DEATH) 
            return;
    
        if (animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.ATTACK_NORMAL &&
            animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.TAKE_HIT &&
            animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.JUMP) 
        {
            animationManager.setState(PlayerAnimationManager.PlayerState.ATTACK_NORMAL);
        }
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

        // Restringe a posição dentro dos limites do mapa
        float mapWidth = collisionLayer.getWidth() * collisionLayer.getTileWidth();
        float mapHeight = collisionLayer.getHeight() * collisionLayer.getTileHeight();

        position.x = MathUtils.clamp(position.x, 0, mapWidth - 35); // Considerando a largura do player
        position.y = MathUtils.clamp(position.y, 0, mapHeight - 30); // Considerando a altura do player

        animationManager.update(deltaTime);
    }

    public void updateColisionMap() 
    {
        float lastX = position.x, lastY = position.y, tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
        boolean collidedX = false, collidedY = false;
        
        // Teste de colisão para esquerda
        if(velocity.x < 0) 
        {
        	// Diagonal superior esquerda
        	collidedX = collisionLayer.getCell((int)(lastX / tileWidth), (int)((lastY + 30) / tileHeight)).getTile().getProperties().containsKey("blocked");
        	System.out.println("Colidiu");
        	
        	// Esquerda
        	if(!collidedX) 
            {
	        	collidedX = collisionLayer.getCell((int)(lastX / tileWidth),(int)(((lastY + 30) / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        	
        	// Diagonal inferior esquerda
        	if(!collidedX) 
            {
	        	collidedX = collisionLayer.getCell((int)(lastX / tileWidth),(int)(lastY  / tileHeight)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        }
        
        // Teste de colisão para direita
        else if(velocity.x > 0) 
        {
        	// Diagonal superior direita
        	collidedX = collisionLayer.getCell((int)((lastX + 35)/ tileWidth),(int)((lastY + 30) / tileHeight)).getTile().getProperties().containsKey("blocked");
        	System.out.println("Colidiu");
        	
        	// Direita
        	if(!collidedX) 
            {
	        	collidedX = collisionLayer.getCell((int)((lastX + 35)/ tileWidth),(int)(((lastY + 30) / 2) / tileHeight)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        	
        	// Diagonal inferior direita
        	if(!collidedX) 
            {
	        	collidedX = collisionLayer.getCell((int)((lastX + 35)/ tileWidth),(int)(lastY  / tileHeight)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        }
        
        // Reação a colisão
        if(collidedX) 
        {
        	position.x = lastX;
        	velocity.x = 0;
        }
        
        // Teste de colisão para baixo
        if(velocity.y < 0) 
        {
        	// Diagonal superior de baixo
        	collidedY = collisionLayer.getCell((int)(lastX / tileWidth), (int)(lastY / tileHeight)).getTile().getProperties().containsKey("blocked");
        	System.out.println("Colidiu");
        	
        	// Baixo
        	if(!collidedY) 
            {
	        	collidedY = collisionLayer.getCell((int)(((lastX + 35) / 2) / tileWidth), (int)(lastY / tileHeight)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        	
        	// Diagonal inferior de baixo
        	if(!collidedY) 
            {
	        	collidedY = collisionLayer.getCell((int)((lastX + 35)/ tileWidth),(int)(lastY / tileHeight)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        }
        
        // Teste de colisão para cima
        else if(velocity.y > 0) 
        {
        	// Diagonal superior de cima
        	collidedY = collisionLayer.getCell((int)(lastX / tileWidth),(int)(lastY / tileHeight)).getTile().getProperties().containsKey("blocked");
        	System.out.println("Colidiu");
        	
        	// Cima
        	if(!collidedY) 
            {
	        	collidedY = collisionLayer.getCell((int)(((lastX + 35) / 2) / tileWidth),(int)((lastY + 30) / tileWidth)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        	
        	// Diagonal inferior de cima
        	if(!collidedY) 
            {
	        	collidedY = collisionLayer.getCell((int)((lastX + 35)/ tileWidth),(int)((lastY + 30) / tileHeight)).getTile().getProperties().containsKey("blocked");
	        	System.out.println("Colidiu");
        	}
        } 
        
        // Reação a colisão
        if(collidedY) 
        {
        	position.y = lastY;
        	velocity.y = 0;
        }
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