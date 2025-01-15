package game.zelda;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.zelda.states.MenuState;

public class MainGame extends ApplicationAdapter  
{
    private SpriteBatch batch;
    private GameContext gameContext;

    @Override
    public void create() 
    {
        AssetsManager.getInstance().loadAssets();
        AssetsManager.getInstance().finishLoading();

        batch = new SpriteBatch();
        gameContext = new GameContext();

        gameContext.setState(new MenuState(gameContext));
    }

    @Override
    public void render() 
    {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        gameContext.update(delta);
        gameContext.render(batch);
    }

    @Override
    public void dispose() 
    {
        batch.dispose();
        AssetsManager.getInstance().dispose();
    }
}