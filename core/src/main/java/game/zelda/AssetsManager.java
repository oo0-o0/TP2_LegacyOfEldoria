package game.zelda;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import java.util.ArrayList;
import java.util.HashMap;

public class AssetsManager 
{
    private static AssetsManager instance;
    private final AssetManager assetManager;
    private final HashMap<String, BitmapFont> fonts;
    private final HashMap<String, Animation<TextureRegion>> animations;

    private AssetsManager() 
    {
        assetManager = new AssetManager();
        fonts = new HashMap<>();
        animations = new HashMap<>();
    }

    public static synchronized AssetsManager getInstance() 
    {
        if (instance == null) 
        {
            instance = new AssetsManager();
        }
        return instance;
    }

    public void loadAssets() 
    {
        loadFont("titleFont", "assets/fonts/romantic.ttf", 120, com.badlogic.gdx.graphics.Color.WHITE);
        loadFont("buttonFont", "assets/fonts/pixelifySans.ttf", 56, com.badlogic.gdx.graphics.Color.WHITE);
        loadFont("buttonFontHover", "assets/fonts/pixelifySans.ttf", 60, new com.badlogic.gdx.graphics.Color(0.8549f, 1.0f, 0.6039f, 1.0f));
        
        loadFont("storyFont", "assets/fonts/pixelifySans.ttf", 21, com.badlogic.gdx.graphics.Color.WHITE);
        
        loadFont("finalMenu", "assets/fonts/romantic.ttf", 45, com.badlogic.gdx.graphics.Color.WHITE);
        loadFont("finalButton", "assets/fonts/pixelifySans.ttf", 46, com.badlogic.gdx.graphics.Color.WHITE);
        loadFont("finalButtonHover", "assets/fonts/pixelifySans.ttf", 48, com.badlogic.gdx.graphics.Color.WHITE);
        loadFont("subtitleFont", "assets/fonts/romantic.ttf", 48, com.badlogic.gdx.graphics.Color.WHITE);
    }

    private void loadFont(String fontKey, String fontPath, int size, com.badlogic.gdx.graphics.Color color) 
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;

        BitmapFont font = generator.generateFont(parameter);
        fonts.put(fontKey, font);
        generator.dispose();
    }

    public BitmapFont getFont(String fontKey) 
    {
        return fonts.get(fontKey);
    }

    public void loadAnimationFromImages(String animationKey, String[] imagePaths, float frameDuration, Animation.PlayMode playMode) 
    {
        ArrayList<TextureRegion> frames = new ArrayList<>();
        for (String path : imagePaths) 
        {
            if (!assetManager.isLoaded(path, Texture.class)) 
            {
                assetManager.load(path, Texture.class);
                assetManager.finishLoading();
            }
            Texture texture = assetManager.get(path, Texture.class);
            frames.add(new TextureRegion(texture));
        }
        
        Animation<TextureRegion> animation = new Animation<>(frameDuration, frames.toArray(new TextureRegion[0]));
        animation.setPlayMode(playMode);
        animations.put(animationKey, animation);
    }

    public Animation<TextureRegion> getAnimation(String animationKey) 
    {
        return animations.get(animationKey);
    }

    public void finishLoading() 
    {
        assetManager.finishLoading();
    }

    public <T> T get(String fileName, Class<T> type) 
    {
        return assetManager.get(fileName, type);
    }

    public boolean isLoaded(String fileName) 
    {
        return assetManager.isLoaded(fileName);
    }

    public void dispose() 
    {
        assetManager.dispose();
        for (BitmapFont font : fonts.values()) 
        {
            font.dispose();
        }
        fonts.clear();

        animations.clear();
    }
}