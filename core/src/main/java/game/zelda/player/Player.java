package game.zelda.player;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import game.zelda.AssetsManager;

public class Player 
{
    private Vector2 position;
    private Vector2 velocity;
    private boolean isJumping;
    private boolean isRunning;
    public PlayerAnimationManager animationManager;
    private List<TiledMapTileLayer> collisionLayers;
    private String blockedKey = "blocked";
    private Vector2 direction;
    
    
    public Player(float startX, float startY, List<TiledMapTileLayer> collisionLayers) 
    {
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(0, 0);
        this.animationManager = new PlayerAnimationManager();
        this.isJumping = false;
        this.isRunning = false;
        this.collisionLayers = collisionLayers;
        this.direction = new Vector2(0, 0);

        AssetsManager assetsManager = AssetsManager.getInstance();
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.IDLE, assetsManager.getAnimation("idle"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.JUMP, assetsManager.getAnimation("jump"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.ATTACK_NORMAL, assetsManager.getAnimation("attackNormal"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.ATTACK_POWERED, assetsManager.getAnimation("attackPowered"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.TAKE_HIT, assetsManager.getAnimation("takeHit"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.DEATH, assetsManager.getAnimation("death"));
    }

    public void setPosition(Vector2 position) {
		this.position = position;
	}
    
    public Vector2 getPosition() {
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

    public void takeHit() 
    {
        animationManager.setState(PlayerAnimationManager.PlayerState.TAKE_HIT);
        // Lógica para redução de vida ?
    }

    public void die() 
    {
        animationManager.setState(PlayerAnimationManager.PlayerState.DEATH);
        velocity.set(0, 0); 
    }

    public void update(float deltaTime) 
    {       
    	float lastX = getPositionX(), lastY = getPositionY();
    	boolean collidedX = false, collidedY = false;
    	
      
        if (isJumping) {
            velocity.y -= 980 * deltaTime; 
            
            if (position.y <= 0) {
                position.y = 0;
                isJumping = false;
        
                if (animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.DEATH) {
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
        if (direction.x  < 0) {
        	collidedX = collidesLeft();
        } 
        
      //colisão para a direita
        else if (direction.x > 0) {
        	collidedX = collidesRight();
        }
        
      //Reação a colisão: reposiciona o personagem apos colisão com o eixo x
        if(collidedX) {
        	Vector2 newPositionX;
        	newPositionX = new Vector2(lastX - 1, position.y);       	
        	setPosition(newPositionX); 	
        	velocity.x = 0;
        }
        
      //colisão para baixo
        if (direction.y < 0) {
        	collidedY = collidesBottom();
        } 
        
      //colisão para cima
        else if (direction.y > 0) {
        	collidedY = collidesTop();
        }
        
      //Reação a colisão: reposiciona o personagem apos colisão com o eixo y
        if(collidedY) {
        	Vector2 newPositionY;
        	newPositionY = new Vector2(position.x, lastY - 1);       	
        	setPosition(newPositionY);
        	velocity.y = 0;
        }      
    }
    
    private boolean isCellBlocked(float x, float y) {
    	   	
        for (TiledMapTileLayer layer : collisionLayers) {
            TiledMapTileLayer.Cell cell = layer.getCell((int) (x / layer.getTileWidth()), (int) (y / layer.getTileHeight()));
            if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked")) {
                return true;
            }
        }
        return false;
    }
    
    public boolean collidesRight() {
    	for (TiledMapTileLayer layer : collisionLayers) {
            for (float step = 0; step < 128; step += layer.getTileHeight() / 2) {
                if (isCellBlocked(getPositionX() + 228, getPositionY() + step)) {
                	return true;
                }
            }
        }
        return false;
    }

    public boolean collidesLeft() {
    	for (TiledMapTileLayer layer : collisionLayers) {
            for (float step = 0; step < 128; step += layer.getTileHeight() / 2) {
                if (isCellBlocked(getPositionX(), getPositionY() + step)) {
                    return true;
                    
                }
            }
        }
        return false;
    }

    public boolean collidesTop() {
    	for (TiledMapTileLayer layer : collisionLayers) {
            for (float step = 0; step < 228; step += layer.getTileWidth() / 2) {
                if (isCellBlocked(getPositionX() + step, getPositionY() + 128)) {
                	return true;
                }
            }
        }
        return false;
    }

    public boolean collidesBottom() {
    	for (TiledMapTileLayer layer : collisionLayers) {
            for (float step = 0; step < 228; step += layer.getTileWidth() / 2) {
                if (isCellBlocked(getPositionX() + step, getPositionY())) {
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
}

class RectangleToPolygon {
    public static Polygon convertRectangleToPolygon(Rectangle rectangle) {
        float[] vertices = new float[]{
            rectangle.x, rectangle.y, // canto inferior esquerdo
            rectangle.x + rectangle.width, rectangle.y, // canto inferior direito
            rectangle.x + rectangle.width, rectangle.y + rectangle.height, // canto superior direito
            rectangle.x, rectangle.y + rectangle.height // canto superior esquerdo
        };
        return new Polygon(vertices);
    }
}
