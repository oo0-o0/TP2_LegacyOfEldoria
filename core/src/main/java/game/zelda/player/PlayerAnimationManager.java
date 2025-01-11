package game.zelda.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

public class PlayerAnimationManager 
{
    public enum PlayerState 
    {
        IDLE,
        RUNNING,
        JUMP,
        ATTACK_NORMAL,
        ATTACK_POWERED,
        TAKE_HIT,
        DEATH
    }

    private final HashMap<PlayerState, Animation<TextureRegion>> animations;
    private PlayerState currentState;
    private float animationTimer;

    public PlayerAnimationManager() 
    {
        this.animations = new HashMap<>();
        this.currentState = PlayerState.IDLE; 
        this.animationTimer = 0f;
    }

    public void loadAnimation(PlayerState state, Animation<TextureRegion> animation) 
    {
        animations.put(state, animation);
    }

    public void setState(PlayerState newState) 
    {
        if (this.currentState != newState) 
        {
            this.currentState = newState;
            this.animationTimer = 0f; 
        }
    }

    public Animation<TextureRegion> getCurrentAnimation() 
    {
        return animations.get(currentState);
    }

    public TextureRegion getCurrentFrame() 
    {
        Animation<TextureRegion> animation = animations.get(currentState);
        if (animation != null) 
        {
            boolean looping = (currentState == PlayerState.RUNNING);
            return animation.getKeyFrame(animationTimer, looping);
        }
        return null;
    }

    public void update(float deltaTime)
    {
        this.animationTimer += deltaTime;
    }
}