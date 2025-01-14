package game.zelda.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import game.zelda.AssetsManager;

public class InventoryGame 
{
    private Texture inventoryIcon, inventoryBackground;
    private Texture emptySlot, itemTexture1, itemTexture2;
    private boolean isInventoryOpen = false;
    private Array<Item> itemsOnMap;
    private Array<Item> inventoryItems;
    private int inventorySize = 8; // 8 slots
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    public InventoryGame() 
    {
        inventoryBackground = new Texture("assets/backUI.png"); // Fundo do ícone
        inventoryIcon = new Texture("assets/inventoryIcon.png");
        emptySlot = new Texture("assets/slot.png");
        itemTexture1 = new Texture("assets/itens/potion.png");
        itemTexture2 = new Texture("assets/itens/book.png");

        itemsOnMap = new Array<>();
        inventoryItems = new Array<>();

        font = AssetsManager.getInstance().getFont("inventoryFont");
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
        float screenWidth = com.badlogic.gdx.Gdx.graphics.getWidth();
        float screenHeight = com.badlogic.gdx.Gdx.graphics.getHeight();

        // Posição da área dos slots (logo acima do ícone)
        float slotAreaX = (screenWidth - 180) / 2f; // Centralizado horizontalmente
        float slotAreaY = 170; // Acima do ícone

        // Renderiza os slots e itens
        for (int i = 0; i < inventorySize; i++) 
        {
            float x = slotAreaX + (i % 4) * 45; 
            float y = slotAreaY - (i / 4) * 45; 

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

    public void renderUI(SpriteBatch batch) 
    {
        float screenWidth = com.badlogic.gdx.Gdx.graphics.getWidth();
        float screenHeight = com.badlogic.gdx.Gdx.graphics.getHeight();

        // Calcula as posições do ícone e do fundo
        float inventoryIconX = (screenWidth - 64) / 2f; 
        float inventoryIconY = 20; 

        // Renderiza o ícone do inventário
        batch.draw(inventoryIcon, inventoryIconX, inventoryIconY, 42, 42);

        // Renderiza o fundo do ícone sobre o ícone
        batch.draw(inventoryBackground, inventoryIconX - 169, inventoryIconY - 60, 380, 214);

        // Renderiza o texto "Inventário"
        String text = "Inventário";
        glyphLayout.setText(font, text);
        float textX = inventoryIconX + (50 - glyphLayout.width) / 2f;
        float textY = inventoryIconY + 100; 
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

    public void renderMapItems(SpriteBatch batch) 
    {
        for (Item item : itemsOnMap) 
        {
            batch.draw(item.texture, item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height);
        }
    }

    public void dispose() 
    {
        inventoryIcon.dispose();
        inventoryBackground.dispose();
        emptySlot.dispose();
        itemTexture1.dispose();
        itemTexture2.dispose();
        font.dispose();
    }
}
