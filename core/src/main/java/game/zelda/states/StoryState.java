package game.zelda.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import game.zelda.AssetsManager;
import game.zelda.GameContext;

public class StoryState implements GameState 
{
    private final GameContext gameContext;

    private Texture[] images;
    private Music backgroundMusic;
    private BitmapFont font;
    private String[] texts;
    private int currentTextIndex = 0;
    private String displayedText = "";
    private float textSpeed = 0.1f; 
    private float elapsedTime = 0f;
    private float switchTime = 38f; 
    private float totalElapsedTime = 0f; 

    public StoryState(GameContext gameContext)
    {
        this.gameContext = gameContext;
    }

    @Override
    public void enter() 
    {
        images = new Texture[]
        {
            new Texture(Gdx.files.internal("assets/backgroundImages/story1.png")),
            new Texture(Gdx.files.internal("assets/backgroundImages/story2.png")),
            new Texture(Gdx.files.internal("assets/backgroundImages/story3.png"))
        };

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/storyMusic.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        texts = new String[]
        {
            "-> A vida era pacífica em Eldoria, uma cidade mágica, repleta de mistérios e rodeada pela exuberante Floresta de Lumina. Conhecida por sua fonte mágica, a Árvore Eterna, Lumina, era o coração pulsante do reino, alimentando a terra com sua energia vital. O povo de Eldoria vivia em harmonia, protegido por essa força ancestral.",
            "Entre eles, havia um elfo arqueiro chamado Kael, que passava seus dias explorando os bosques e aperfeiçoando sua mira. Para Kael, a vida era cheia de beleza, até que uma escuridão começou a se espalhar. Uma força sombria surgiu do nada, drenando a energia de Lumina. A floresta tornou-se perigosa, e criaturas hostis começaram a surgir.",
            "Com o futuro do lugar ameaçado, Kael foi escolhido pelo espírito da Árvore para embarcar em uma missão: restaurar o equilíbrio da floresta. Sua tarefa é reunir os Artefatos Luminares, relíquias de imenso poder que foram espalhadas pelo mundo para protegê-las de mãos erradas. A sua missão, é controlá-lo nessa aventura!"
        };
        font = AssetsManager.getInstance().getFont("storyFont");
    }

    @Override
    public void update(float delta) 
    {
        totalElapsedTime += delta;
        elapsedTime += delta;

        // Efeito de digitação
        if (elapsedTime > textSpeed && displayedText.length() < texts[currentTextIndex].length()) 
        {
            displayedText += texts[currentTextIndex].charAt(displayedText.length());
            elapsedTime = 0f;
        }

        // Trocar para o próximo texto e imagem
        if (totalElapsedTime > switchTime) 
        {
            totalElapsedTime = 0f;
            currentTextIndex++;

            if (currentTextIndex < texts.length)
            {
                displayedText = "";
            } 
            else 
            {
               gameContext.setState(new PlayingState(gameContext));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) 
    {
        batch.begin();

        batch.setColor(0, 0, 0, 1);
        batch.draw(images[currentTextIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1, 1, 1, 1);

        float imageWidth = images[currentTextIndex].getWidth();
        float imageHeight = images[currentTextIndex].getHeight();
        float scale = 0.3f; 
        float drawWidth = imageWidth * scale;
        float drawHeight = imageHeight * scale;
        float imageX = (Gdx.graphics.getWidth() - drawWidth) / 2; 
        float imageY = Gdx.graphics.getHeight() - drawHeight - 35;     

        batch.draw(images[currentTextIndex], imageX, imageY, drawWidth, drawHeight);
        font.draw(batch, displayedText, 50, 150, Gdx.graphics.getWidth() - 100, Align.left, true);

        batch.end();
    }


    @Override
    public void exit() 
    {
        for (Texture image : images) 
        {
            if (image != null) 
            {
                image.dispose();
            }
        }

        if (backgroundMusic != null) 
        {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }
    }
}