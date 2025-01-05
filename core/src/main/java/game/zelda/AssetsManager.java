package game.zelda;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import java.util.HashMap;

public class AssetsManager 
{
    private static AssetsManager instance;
    private final AssetManager assetManager;
    private final HashMap<String, BitmapFont> fonts;

    private AssetsManager() 
    {
        assetManager = new AssetManager();
        fonts = new HashMap<>();
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
        loadFont("titleFont", "assets/romantic.ttf", 120, com.badlogic.gdx.graphics.Color.WHITE);
        loadFont("buttonFont", "assets/pixelifySans.ttf", 56, com.badlogic.gdx.graphics.Color.WHITE);
        loadFont("buttonFontHover", "assets/pixelifySans.ttf", 60, new com.badlogic.gdx.graphics.Color(0.8549f, 1.0f, 0.6039f, 1.0f));

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
    }
}