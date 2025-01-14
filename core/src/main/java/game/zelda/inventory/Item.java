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

    public boolean isWithinRadius(float x, float y, float radius) 
    {
        float dx = bounds.x - x;
        float dy = bounds.y - y;
        return Math.sqrt(dx * dx + dy * dy) <= radius;
    }
}