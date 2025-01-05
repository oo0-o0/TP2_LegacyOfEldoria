package game.zelda;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameState 
{
    void enter(); 
    void update(float delta); 
    void render(SpriteBatch batch); 
    void exit(); 
}