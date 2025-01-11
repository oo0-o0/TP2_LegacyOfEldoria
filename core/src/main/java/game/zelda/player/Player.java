package game.zelda.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import game.zelda.AssetsManager;

public class Player 
{
    private Vector2 position;
    private Vector2 velocity;
    private boolean isJumping;
    private boolean isRunning;
    private PlayerAnimationManager animationManager;
    
    public Player(float startX, float startY) 
    {
        this.position = new Vector2(startX, startY);
        this.velocity = new Vector2(0, 0);
        this.animationManager = new PlayerAnimationManager();
        this.isJumping = false;
        this.isRunning = false;

        AssetsManager assetsManager = AssetsManager.getInstance();
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.IDLE, assetsManager.getAnimation("idle"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.RUNNING, assetsManager.getAnimation("running"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.JUMP, assetsManager.getAnimation("jump"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.ATTACK_NORMAL, assetsManager.getAnimation("attackNormal"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.ATTACK_POWERED, assetsManager.getAnimation("attackPowered"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.TAKE_HIT, assetsManager.getAnimation("takeHit"));
        animationManager.loadAnimation(PlayerAnimationManager.PlayerState.DEATH, assetsManager.getAnimation("death"));
    }

    public void move(float dx, float dy) 
    {
        position.add(dx, dy);
        if (!isJumping) 
        {
            animationManager.setState(isRunning ? PlayerAnimationManager.PlayerState.RUNNING : PlayerAnimationManager.PlayerState.IDLE);
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
        animationManager.setState(PlayerAnimationManager.PlayerState.ATTACK_NORMAL);
    }

    public void jump() 
    {
        if (!isJumping) 
        {
            isJumping = true;
            animationManager.setState(PlayerAnimationManager.PlayerState.JUMP);
            velocity.y = 500; // Ajuste a altura do pulo conforme necessário
        }
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
        if (isJumping) 
        {
            velocity.y -= 980 * deltaTime;
            if (position.y <= 0) 
            {
                position.y = 0;
                isJumping = false;
                animationManager.setState(PlayerAnimationManager.PlayerState.IDLE);
            }
        }
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        animationManager.update(deltaTime);
    }

    public void setRunning(boolean isRunning) 
    {
        this.isRunning = isRunning;
    }

    public boolean isRunning() 
    {
        return isRunning;
    }

    public void render(Batch batch) 
    {
        batch.draw(animationManager.getCurrentFrame(), position.x, position.y);
    }
}