package game.zelda.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import game.zelda.AssetsManager;

/* Muitas coisas vao mudar aqui */
public class InventoryGame 
{
    private Texture inventoryIcon, emptySlot, itemTexture1, itemTexture2;
    private boolean isInventoryOpen = false;
    private Array<Item> itemsOnMap;
    private Array<Item> inventoryItems;
    private int inventorySize = 7;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    public InventoryGame() 
    {
        inventoryIcon = new Texture("assets/inventoryIcon.png");
        emptySlot = new Texture("assets/slot.png");
        itemTexture1 = new Texture("assets/itens/potion.png");
        itemTexture2 = new Texture("assets/itens/book.png");

        itemsOnMap = new Array<>();
        inventoryItems = new Array<>();

        font = AssetsManager.getInstance().getFont("storyFont");
        glyphLayout = new GlyphLayout();

        itemsOnMap.add(new Item(itemTexture1, new Rectangle(200, 200, 32, 32), "Item 1"));
        itemsOnMap.add(new Item(itemTexture2, new Rectangle(400, 400, 32, 32), "Item 2"));
    }

    public boolean isInventoryOpen() 
    {
        return isInventoryOpen;
    }

    public void toggleInventory() 
    {
        isInventoryOpen = !isInventoryOpen;
        inventoryIcon = new Texture(isInventoryOpen ? "assets/inventoryIconOpen.png" : "assets/inventoryIcon.png");
    }

    public void renderInventory(SpriteBatch batch) 
    {
        for (int i = 0; i < inventorySize; i++) 
        {
            float x = 100 + (i % 4) * 50;
            float y = 300 - (i / 4) * 50;
            if (i < inventoryItems.size) 
            {
                batch.draw(inventoryItems.get(i).texture, x, y, 40, 40);
            } 
            else 
            {
                batch.draw(emptySlot, x, y, 40, 40);
            }
        }
    }

    public void renderMapItems(SpriteBatch batch) 
    {
        for (Item item : itemsOnMap) 
        {
            batch.draw(item.texture, item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height);
        }
    }

    public void renderUI(SpriteBatch batch) 
    {
        float screenWidth = com.badlogic.gdx.Gdx.graphics.getWidth();
        float screenHeight = com.badlogic.gdx.Gdx.graphics.getHeight();

        float inventoryIconX = (screenWidth - 64) / 2f; 
        float inventoryIconY = 20; 

        batch.draw(inventoryIcon, inventoryIconX, inventoryIconY, 64, 64);

        String text = "Inventário";
        glyphLayout.setText(font, text);

        float textX = inventoryIconX + (64 - glyphLayout.width) / 2f;
        float textY = inventoryIconY + 80; 

        font.draw(batch, text, textX, textY);
    }

    public void collectItemNearPlayer(float playerX, float playerY, float radius) 
    {
        for (int i = itemsOnMap.size - 1; i >= 0; i--) 
        {
            Item item = itemsOnMap.get(i);
            if (item.isWithinRadius(playerX, playerY, radius) && inventoryItems.size < inventorySize) 
            {
                inventoryItems.add(item);
                itemsOnMap.removeIndex(i);
                break;
            }
        }
    }

    public void dispose() 
    {
        inventoryIcon.dispose();
        emptySlot.dispose();
        itemTexture1.dispose();
        itemTexture2.dispose();
        font.dispose();
    }
}