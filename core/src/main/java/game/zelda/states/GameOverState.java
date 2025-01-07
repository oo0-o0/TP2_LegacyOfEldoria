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

public class GameOverState implements GameState 
{
    private final GameContext gameContext;

    private Music backgroundMusic; 
    private Texture background;
    private Rectangle playAgainButtonBounds;
    private Rectangle exitButtonBounds;
    private BitmapFont fontButton;
    private BitmapFont fontButtonHover;
    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private Animation<TextureRegion> losingAnimation;
    private float animationTime;

    public GameOverState(GameContext gameContext) 
    {
        this.gameContext = gameContext;
    }

    @Override
    public void enter() 
    {
        background = new Texture(Gdx.files.internal("assets/backgroundImages/gameOver.png"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/lostTheme.mp3"));
        backgroundMusic.setLooping(true); 
        backgroundMusic.setVolume(0.5f); 
        backgroundMusic.play(); 

        fontButton = AssetsManager.getInstance().getFont("finalButton");
        fontButtonHover = AssetsManager.getInstance().getFont("finalButtonHover");
        titleFont = AssetsManager.getInstance().getFont("titleFont");
        subtitleFont = AssetsManager.getInstance().getFont("subtitleFont");

        float buttonWidth = 200;
        float buttonHeight = 40;

        playAgainButtonBounds = new Rectangle
        (
            Gdx.graphics.getWidth() / 2f - 282, 
            Gdx.graphics.getHeight() / 2f - 215,
            buttonWidth,
            buttonHeight
        );

        exitButtonBounds = new Rectangle
        (
            Gdx.graphics.getWidth() / 2f + 85, 
            Gdx.graphics.getHeight() / 2f - 215,
            buttonWidth,
            buttonHeight
        );

        String[] animationFrames = new String[19];
        for (int i = 0; i < 19; i++) 
        {
            animationFrames[i] = "assets/losingAnimation/death_" + (i + 1) + ".png";
        }

        AssetsManager.getInstance().loadAnimationFromImages("losingAnimation", animationFrames, 0.1f, Animation.PlayMode.LOOP);
        losingAnimation = AssetsManager.getInstance().getAnimation("losingAnimation");
        animationTime = 0f;
    }

    @Override
    public void update(float delta) 
    {
        animationTime += delta;

        if (Gdx.input.justTouched()) 
        {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

            if (playAgainButtonBounds.contains(touchPos)) 
            {
                gameContext.setState(new MenuState(gameContext));
            } 

            else if (exitButtonBounds.contains(touchPos)) 
            {
                Gdx.app.exit();
            } 
        }
    }

    @Override
    public void render(SpriteBatch batch) 
    {
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        titleFont.draw(batch, "YOU LOSE :(", 
            Gdx.graphics.getWidth() / 2f, 
            Gdx.graphics.getHeight() - 100, 
            0, Align.center, false);

        subtitleFont.draw(batch, "\"Infelizmente, você não salvou Eldoria\"", 
            Gdx.graphics.getWidth() / 2f, 
            Gdx.graphics.getHeight() - 220, 
            0, Align.center, false);

        TextureRegion currentFrame = losingAnimation.getKeyFrame(animationTime);
        float scale = 2.25f; 
        float frameWidth = currentFrame.getRegionWidth() * scale;
        float frameHeight = currentFrame.getRegionHeight() * scale;
        float animationX = (Gdx.graphics.getWidth() - frameWidth) / 2 - 50; 
        float animationY = (Gdx.graphics.getHeight() - frameHeight) / 2;
        batch.draw(currentFrame, animationX, animationY, frameWidth, frameHeight);

        renderButton(batch, playAgainButtonBounds, "TRY AGAIN");
        renderButton(batch, exitButtonBounds, "EXIT");

        batch.end();
    }

    private void renderButton(SpriteBatch batch, Rectangle bounds, String text) 
    {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        BitmapFont buttonFontToUse = bounds.contains(mousePos) ? fontButtonHover : fontButton;

        buttonFontToUse.draw(batch, text,
            bounds.x + bounds.width / 2, 
            bounds.y + bounds.height / 2 + 10, 
            0, Align.center, false);
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