package game.zelda;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.zelda.states.GameOverState;
import game.zelda.states.MenuState;
import game.zelda.states.PlayingState;
import game.zelda.states.StoryState;
import game.zelda.states.WinningState;

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

        gameContext.setState(new PlayingState(gameContext));
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