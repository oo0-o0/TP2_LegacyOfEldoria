package game.zelda.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import game.zelda.GameContext;
import game.zelda.camera.CameraController;
import game.zelda.inventory.InventoryGame;
import game.zelda.map.Map;
import game.zelda.player.commands.AttackCommand;
import game.zelda.player.commands.MoveCommand;
import game.zelda.player.Player;
import game.zelda.player.PlayerAnimationManager;

public class PlayingState implements GameState 
{
    private Player player;
    private GameContext gameContext;
    private MoveCommand moveCommand;
    private AttackCommand attackCommand;
    //private CameraController cameraController;
    private ShapeRenderer shapeRenderer;
    private InventoryGame inventory;
    private Map map;

    public PlayingState(GameContext gameContext) 
    {
        float viewportWidth = Gdx.graphics.getWidth();
        float viewportHeight = Gdx.graphics.getHeight();
        //this.cameraController = new CameraController(viewportWidth, viewportHeight);
        this.shapeRenderer = new ShapeRenderer();

        this.gameContext = gameContext;
        this.map = new Map();
        this.player = new Player(100, 100, (TiledMapTileLayer) map.getLayers());
        this.moveCommand = new MoveCommand(player, 250f);
        this.attackCommand = new AttackCommand(player);

        this.inventory = new InventoryGame();
    }

    @Override
    public void update(float deltaTime) 
    {
        handleInput(deltaTime);
        player.update(deltaTime);
    
        //cameraController.update(player.getPosition());
    
        player.updateColisionMap();
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

        // Coletar item próximo (nao ta funcionando ainda rsrsrs)
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) 
        {
            inventory.collectItemNearPlayer(player.getPosition().x, player.getPosition().y, 50);
        }

        // Abrir/fechar o inventário
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) 
        {
            inventory.toggleInventory();
        }
    }

    @Override
    public void render(SpriteBatch batch) 
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
        map.renderMapOnScreen();

        //batch.setProjectionMatrix(cameraController.getMainCamera().combined);
        batch.begin();
    
        player.render(batch);
        inventory.renderMapItems(batch);
    
        batch.end();
    
        //batch.setProjectionMatrix(cameraController.getUICamera().combined);
        batch.begin();
    
        inventory.renderUI(batch);
        batch.end();
    
        if (inventory.isInventoryOpen()) 
        {
            batch.begin();
            inventory.renderInventory(batch);
            batch.end();
        }
    
        //renderMiniMap(batch);
    }    

    private void renderMiniMap(SpriteBatch batch) 
    {
        int width = Gdx.graphics.getWidth() / 4;
        int height = Gdx.graphics.getHeight() / 4;
        int x = Gdx.graphics.getWidth() - width;
        int y = Gdx.graphics.getHeight() - height;
    
        batch.flush();
        Gdx.gl.glViewport(x, y, width, height);
    
       // batch.setProjectionMatrix(cameraController.getMiniMapCamera().combined);
        batch.begin();

        // Aqui tambem tudo o que for renderizado precisa ser de novo (sim nao sei pq, aaaaa)
        // tipo os inimigos, mapa, etc
        map.renderMapOnScreen();

        player.render(batch);
        inventory.renderMapItems(batch);

        batch.end();
    
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }
    
    @Override
    public void enter() 
    {
        Gdx.app.log("PlayingState", "Entering PlayingState.");
    }

    @Override
    public void exit() 
    {
        Gdx.app.log("PlayingState", "Exiting PlayingState.");
    }
}