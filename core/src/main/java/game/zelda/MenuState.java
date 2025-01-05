package game.zelda;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

public class MenuState implements GameState 
{
    private Texture background;
    private Rectangle playButtonBounds;
    private GameContext gameContext;
    private BitmapFont font; 
    private BitmapFont fontButton; 
    private BitmapFont fontButtonHover; 
    private Music backgroundMusic; 

    public MenuState(GameContext gameContext) 
    {
        this.gameContext = gameContext;
    }

    @Override
    public void enter() 
    {
        background = new Texture(Gdx.files.internal("assets/menuBackground.png"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/menuSound.mp3"));
        backgroundMusic.setLooping(true); 
        backgroundMusic.setVolume(0.5f); 
        backgroundMusic.play(); 
        
        font = AssetsManager.getInstance().getFont("titleFont");
        fontButton = AssetsManager.getInstance().getFont("buttonFont");
        fontButtonHover = AssetsManager.getInstance().getFont("buttonFontHover");


        playButtonBounds = new Rectangle
        (
            Gdx.graphics.getWidth() / 2f - 100, 
            Gdx.graphics.getHeight() / 2f - 180, 
            200, 
            40  
        );        
    }

    @Override
    public void update(float delta) 
    {
        if (Gdx.input.justTouched()) 
        {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            if (playButtonBounds.contains(touchPos)) 
            {
                gameContext.setState(new StoryState(gameContext)); 
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) 
    {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font.draw(batch, "Legacy of Eldoria:", 
                  Gdx.graphics.getWidth() / 2f, 
                  Gdx.graphics.getHeight() - 90, 
                  0, Align.center, false);

        font.draw(batch, "\"Artifacts of Light\"", 
                  Gdx.graphics.getWidth() / 2f, 
                  Gdx.graphics.getHeight() - 200, 
                  0, Align.center, false);

        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        BitmapFont buttonFontToUse = playButtonBounds.contains(mousePos) ? fontButtonHover : fontButton;

        buttonFontToUse.draw(batch, " > PLAY ", 
                  playButtonBounds.x + playButtonBounds.width / 2, 
                  playButtonBounds.y + playButtonBounds.height / 2, 
                  0, Align.center, false);

        batch.end();
    }

    @Override
    public void exit() 
    {
        if (backgroundMusic != null) 
        {
            backgroundMusic.stop(); 
            backgroundMusic.dispose(); 
        }

        if (background != null) 
        {
            background.dispose();   
        }
    }
}
