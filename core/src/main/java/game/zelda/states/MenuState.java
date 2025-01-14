package game.zelda.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import game.zelda.AssetsManager;
import game.zelda.GameContext;

public class MenuState implements GameState 
{
    private GameContext gameContext;

    private Texture background;
    private Rectangle playButtonBounds;
    private BitmapFont font; 
    private BitmapFont fontButton; 
    private BitmapFont fontButtonHover; 
    private Music backgroundMusic; 
    private Animation<TextureRegion> cursorAnimation;
    private float animationTime;

    public MenuState(GameContext gameContext) 
    {
        this.gameContext = gameContext;
    }

    @Override
    public void enter() 
    {
        background = new Texture(Gdx.files.internal("../assets/backgroundImages/menuBackground.png"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("../assets/sounds/menuSound.mp3"));
        backgroundMusic.setLooping(true); 
        backgroundMusic.setVolume(0.5f); 
        backgroundMusic.play(); 
        
        font = AssetsManager.getInstance().getFont("titleFont");
        fontButton = AssetsManager.getInstance().getFont("buttonFont");
        fontButtonHover = AssetsManager.getInstance().getFont("buttonFontHover");

        playButtonBounds = new Rectangle
        (
            Gdx.graphics.getWidth() / 2f - 100, 
            Gdx.graphics.getHeight() / 2f - 190, 
            200, 
            40  
        );  

        String[] sparkFrames = 
        {
            "../assets/FallingStar_Sprites/sparkle1.png",
            "../assets/FallingStar_Sprites/sparkle2.png",
            "../assets/FallingStar_Sprites/sparkle3.png",
            "../assets/FallingStar_Sprites/sparkle4.png",
            "../assets/FallingStar_Sprites/sparkle5.png",
            "../assets/FallingStar_Sprites/sparkle6.png",
            "../assets/FallingStar_Sprites/sparkle7.png",
            "../assets/FallingStar_Sprites/sparkle8.png"
        };

        AssetsManager.getInstance().loadAnimationFromImages("cursorAnimation", sparkFrames, 0.1f, Animation.PlayMode.LOOP);
        cursorAnimation = AssetsManager.getInstance().getAnimation("cursorAnimation");
        animationTime = 0f;
    }

    @Override
    public void update(float delta) 
    {
        animationTime += delta; 

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
                  Gdx.graphics.getHeight() - 150, 
                  0, Align.center, false);
    
        font.draw(batch, "\"Artifacts of Light\"", 
                  Gdx.graphics.getWidth() / 2f, 
                  Gdx.graphics.getHeight() - 260, 
                  0, Align.center, false);
    
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    
        boolean isHovering = playButtonBounds.contains(mousePos);
        BitmapFont buttonFontToUse = isHovering ? fontButtonHover : fontButton;
    
        buttonFontToUse.draw(batch, " > PLAY ", 
                  playButtonBounds.x + playButtonBounds.width / 2, 
                  playButtonBounds.y + playButtonBounds.height / 2, 
                  0, Align.center, false);
    
        if (isHovering) 
        {
            TextureRegion currentFrame = cursorAnimation.getKeyFrame(animationTime);
            float animationX = playButtonBounds.x + playButtonBounds.width / 2 - currentFrame.getRegionWidth() / 2;
            float animationY = playButtonBounds.y + playButtonBounds.height + 10; 
            batch.draw(currentFrame, animationX, animationY);
        }
    
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