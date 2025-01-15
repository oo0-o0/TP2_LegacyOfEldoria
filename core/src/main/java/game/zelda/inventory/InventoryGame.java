package game.zelda.inventory;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import game.zelda.AssetsManager;
import game.zelda.observer.Observer;
import game.zelda.observer.Subject;

public class InventoryGame implements Subject
{
    private Texture inventoryIcon, inventoryBackground;
    private Texture emptySlot, itemTexture1, itemTexture2, itemTexture3, itemTexture4, itemTexture5, itemTexture6, itemTexture7, itemTexture8;
    private boolean isInventoryOpen = false;
    private Array<Item> itemsOnMap;
    private Array<Item> inventoryItems;
    private Array<Observer> observers;
    private int inventorySize = 8; 
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    public InventoryGame() 
    {
        inventoryBackground = new Texture("assets/inventory/backUI.png");
        inventoryIcon = new Texture("assets//inventory/inventoryIcon.png");
        emptySlot = new Texture("assets/inventory/slot.png");

        itemTexture1 = new Texture("assets/itens/potion.png");
        itemTexture2 = new Texture("assets/itens/book.png");
        itemTexture3 = new Texture("assets/itens/goblet.png");
        itemTexture4 = new Texture("assets/itens/key.png");
        itemTexture5 = new Texture("assets/itens/charm.png");
        itemTexture6 = new Texture("assets/itens/healthPotion.png");
        itemTexture7 = new Texture("assets/itens/wand.png");
        itemTexture8 = new Texture("assets/itens/sword.png");

        font = AssetsManager.getInstance().getFont("inventoryFont");

        itemsOnMap = new Array<>();
        inventoryItems = new Array<>();
        observers = new Array<>();

        glyphLayout = new GlyphLayout();

        Texture[] itemTextures = {itemTexture1, itemTexture2, itemTexture3, itemTexture4, itemTexture5, itemTexture6, itemTexture7, itemTexture8};
        String[] itemNames = {"Poção", "Livro", "Cálice", "Chave", "Colar", "Poção da Vida", "Varinha", "Arma"};

        // Coloca eles em lugares aleatorios
        Random random = new Random();
        for (int i = 0; i < itemTextures.length; i++) 
        {
            float x = random.nextInt(Gdx.graphics.getWidth() - 32); 
            float y = random.nextInt(Gdx.graphics.getHeight() - 32); // Posição 
            itemsOnMap.add(new Item(itemTextures[i], new Rectangle(x, y, 32, 32), itemNames[i]));
        }

        // Outra opcao caso isso de merda pelo mapa (tipo agora eles vao em areas que o divo nao anda,kkkk) so mudar o x e y, esses 200 e 400
        /*itemsOnMap.add(new Item(itemTexture1, new Rectangle(200, 200, 32, 32), "Poção"));
        itemsOnMap.add(new Item(itemTexture2, new Rectangle(400, 400, 32, 32), "Livro"));
        itemsOnMap.add(new Item(itemTexture3, new Rectangle(300, 400, 32, 32), "Cálice"));
        itemsOnMap.add(new Item(itemTexture4, new Rectangle(100, 400, 32, 32), "Chave"));
        itemsOnMap.add(new Item(itemTexture5, new Rectangle(440, 400, 32, 32), "Colar"));
        itemsOnMap.add(new Item(itemTexture6, new Rectangle(400, 300, 32, 32), "Poção da Vida"));
        itemsOnMap.add(new Item(itemTexture7, new Rectangle(400, 100, 32, 32), "Varinha"));
        itemsOnMap.add(new Item(itemTexture8, new Rectangle(310, 150, 32, 32), "Arma"));*/
    }

    public boolean isInventoryOpen() 
    {
        return isInventoryOpen;
    }

    public void toggleInventory() 
    {
        isInventoryOpen = !isInventoryOpen;
        inventoryIcon = new Texture(isInventoryOpen ? "assets/inventory/inventoryIconOpen.png" : "assets/inventory/inventoryIcon.png");
    }

    public void renderInventory(SpriteBatch batch) 
    {
        float screenWidth = com.badlogic.gdx.Gdx.graphics.getWidth();
        float screenHeight = com.badlogic.gdx.Gdx.graphics.getHeight();

        float slotAreaX = (screenWidth - 180) / 2f; 
        float slotAreaY = 170; 

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

        float inventoryIconX = (screenWidth - 64) / 2f; 
        float inventoryIconY = 20; 

        batch.draw(inventoryIcon, inventoryIconX, inventoryIconY, 42, 42);
        batch.draw(inventoryBackground, inventoryIconX - 169, inventoryIconY - 60, 380, 214);

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
                notifyObservers(item);  
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

    @Override
    public void registerObserver(Observer observer) 
    {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) 
    {
        observers.removeValue(observer, false);
    }

    @Override
    public void notifyObservers(Object event) 
    {
        for (Observer observer : observers) 
        {
            try 
            {
                observer.update(event);
            } 
            catch (Exception e) 
            {
                System.err.println("Erro ao notificar observador: " + observer + ", " + e.getMessage());
            }
        }
    }   

    public Array<Item> getInventoryItems() 
    {
        return inventoryItems;
    }

    public int getInventorySize() 
    {
        return inventorySize;
    }
}