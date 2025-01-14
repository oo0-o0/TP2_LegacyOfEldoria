package game.zelda.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

public class PlayerAnimationManager 
{
    public enum PlayerState 
    {
        IDLE,
        JUMP,
        ATTACK_NORMAL,
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
        if (newState == PlayerState.ATTACK_NORMAL || newState == PlayerState.DEATH) 
        {
            this.currentState = newState;
            this.animationTimer = 0f;
            return;
        }

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
            // Ativa o looping apenas para estados contínuos
            boolean looping = (currentState == PlayerState.IDLE);
            return animation.getKeyFrame(animationTimer, looping);
        }
        return null;
    }

    public boolean isAnimationFinished() 
    {
        Animation<TextureRegion> animation = animations.get(currentState);
        if (animation != null) 
        {
            return animation.isAnimationFinished(animationTimer);
        }
        return true;
    }

    public PlayerState getCurrentState() 
    {
        return this.currentState;
    }


    public void update(float deltaTime)
    {
        this.animationTimer += deltaTime;
    }
}