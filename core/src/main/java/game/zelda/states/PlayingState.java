package game.zelda.states;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

import game.zelda.AssetsManager;
import game.zelda.GameContext;
import game.zelda.entity.Enemy;
import game.zelda.entity.EnemyFactory;
import game.zelda.inventory.InventoryGame;
import game.zelda.map.Map;
import game.zelda.player.commands.MoveCommand;
import game.zelda.player.Player;
import game.zelda.player.PlayerAnimationManager;

public class PlayingState implements GameState 
{
    private Player player;
    private List<Enemy> enemies;
    private BitmapFont font;
    private GameContext gameContext;
    private MoveCommand moveCommand;
    private ShapeRenderer shapeRenderer;
    private InventoryGame inventory;
    private Map map;

    public PlayingState(GameContext gameContext) 
    {
        this.gameContext = gameContext;
    }

    @Override
    public void enter() 
    {
        this.shapeRenderer = new ShapeRenderer();
        this.map = new Map();
        this.inventory = new InventoryGame();

        this.player = new Player(100, 100, (TiledMapTileLayer) map.getLayers(), inventory);
        this.moveCommand = new MoveCommand(player, 250f);

        this.enemies = new ArrayList<>();
        enemies.add(EnemyFactory.enemyCreation(1)); // Adiciona um Bat
        enemies.add(EnemyFactory.enemyCreation(2)); // Adiciona um CrystalElemental
        enemies.add(EnemyFactory.enemyCreation(3)); // Adiciona um MetalElemental

        font = AssetsManager.getInstance().getFont("healthBarFont");
    }

    @Override
    public void update(float deltaTime) 
    {
        handleInput(deltaTime);
        player.update(deltaTime);
    
        player.updateColisionMap();

        for (Enemy enemy : enemies) {
            enemy.attackPlayer(player, deltaTime);
            moveEnemyTowardsPlayer(enemy, deltaTime);
            enemy.updateAnimation(deltaTime);
        }

        // Morte
        if (player.currentHealth <= 0) 
        {
            gameContext.setState(new GameOverState(gameContext));
            return; 
        }

        // Vitória
        if (inventory.getInventoryItems().size == inventory.getInventorySize()) 
        {
            gameContext.setState(new WinningState(gameContext));
            return; 
        }
    }    

    private void handleInput(float deltaTime) 
    {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) dy += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) dy -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) dx -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) dx += 1;

        if (dx != 0 || dy != 0) 
        {
            moveCommand.setDirection(dx, dy);
            moveCommand.setDeltaTime(deltaTime);
            moveCommand.execute();
        } 
        else if (player.animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.ATTACK_NORMAL && player.animationManager.getCurrentState() != PlayerAnimationManager.PlayerState.JUMP) 
        {
            player.stop(); 
        }

        // Pulo
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) 
        {
            player.jump();
        }

        // Ataque
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) 
        {
            player.attack();
        }

        // Abrir/fechar o inventário
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) 
        {
            inventory.toggleInventory();
        }
    }

    private void moveEnemyTowardsPlayer(Enemy enemy, float deltaTime) 
    {
        Vector2 direction = player.getPosition().cpy().sub(enemy.getPosition()).nor();
        float speed = 20; 
        enemy.getPosition().add(direction.scl(speed * deltaTime));
    }

    @Override
    public void render(SpriteBatch batch) 
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
        map.renderMapOnScreen();

        batch.begin();

        player.render(batch);
        for (Enemy enemy : enemies) 
        {
            enemy.render(batch);
        }

        Texture healthBarBackground = new Texture("assets/ui/lifeUI.png");
        batch.draw(healthBarBackground, -70, Gdx.graphics.getHeight() - 120, 320, 180);
        font.draw(batch, + player.currentHealth + "/" + player.maxHealth, 60, Gdx.graphics.getHeight() - 20);
        inventory.renderMapItems(batch);

        batch.end();
    
        batch.begin();
        inventory.renderUI(batch);
        batch.end();

        if (inventory.isInventoryOpen()) 
        {
            batch.begin();
            inventory.renderInventory(batch);
            batch.end();
        }
    }    
    
    @Override
    public void exit() 
    {
        Gdx.app.log("PlayingState", "Exiting PlayingState.");
    }
}