package game.zelda;

public class GameContext 
{
    private GameState currentState;

    public void setState(GameState newState) 
    {
        if (currentState != null) 
        {
            currentState.exit();
        }
        currentState = newState;

        if (currentState != null) 
        {
            currentState.enter();
        }
    }

    public void update(float delta) 
    {
        if (currentState != null) 
        {
            currentState.update(delta);
        }
    }

    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) 
    {
        if (currentState != null) 
        {
            currentState.render(batch);
        }
    }
}