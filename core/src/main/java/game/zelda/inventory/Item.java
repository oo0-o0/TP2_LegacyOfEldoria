package game.zelda.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Item 
{
    Texture texture;
    Rectangle bounds;
    String name;

    public Item(Texture texture, Rectangle bounds, String name) 
    {
        this.texture = texture;
        this.bounds = bounds;
        this.name = name;
    }

    public boolean isWithinRadius(float playerX, float playerY, float radius) 
    {
        float dx = playerX - (bounds.x + bounds.width / 2);
        float dy = playerY - (bounds.y + bounds.height / 2);
        return Math.sqrt(dx * dx + dy * dy) <= radius;
    }

    public String getName() 
    {
        return name;
    }
}